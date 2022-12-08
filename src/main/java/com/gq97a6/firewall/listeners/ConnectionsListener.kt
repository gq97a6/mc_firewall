package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.*
import com.gq97a6.firewall.DB.execute
import com.gq97a6.firewall.DB.executeQuery
import com.gq97a6.firewall.Firewall.Companion.gpdwOpen
import com.gq97a6.firewall.Firewall.Companion.plugin
import com.gq97a6.firewall.classes.Link
import fr.xephi.authme.events.LoginEvent
import github.scarsz.discordsrv.util.DiscordUtil
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST
import kotlin.random.Random

class ConnectionsListener : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onLoginEvent(event: LoginEvent) {
        if (gpdwOpen && event.player.hasPermission("firewall.admin")) {
            event.player.send {
                add("Firewall:") { c(255, 174, 0).b() }
                add(" open") { c(255, 77, 77).b() }
            }
        }
    }

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {

        if (gpdwOpen) {
            event.allow()
            return
        }

        DB.runAction {
            val link = executeQuery("SELECT * FROM links WHERE mc_uuid = '${event.uniqueId}'")?.let {
                if (it.next()) Link(
                    it.getString("ip"),
                    it.getString("username"),
                    it.getString("dc_uuid"),
                    it.getString("mc_uuid")
                )
                else null
            }

            if (link != null) {
                try {
                    DiscordUtil.getJda().getGuildById("695225372265939015")?.getMemberById(link.dcUUID).let { m ->
                        if (m == null) {
                            event.disallow(
                                KICK_WHITELIST,
                                Component.text("Nie znajdujesz się na serwerze discord gigantów.")
                            )
                            return@runAction
                        } else if (m.roles.none { role -> role.id == "1006279637699154012" }) {
                            event.disallow(
                                KICK_WHITELIST,
                                Component.text("Nie posiadasz wymaganej roli na serwerze discord.")
                            )
                            return@runAction
                        }
                    }
                } catch (e: Exception) {
                    plugin.logger.info("|5| $e")
                }

                if (link.ip == event.address.hostAddress) {
                    event.allow()
                    return@runAction
                }
            }

            //Check if code already associated
            var code =
                executeQuery("SELECT code FROM codes WHERE ip = '${event.address.hostAddress}' AND mc_uuid = '${event.uniqueId}'")?.let {
                    if (it.next()) it.getString("code")
                    else null
                } ?: ""

            //Generate new if not
            if (code.isEmpty()) {
                code = List(5) { Random.nextInt(0, 9) }.joinToString("")
                execute("INSERT INTO codes (ip, username, code, mc_uuid) VALUES ('${event.address.hostAddress}', '${event.name}', '$code', '${event.uniqueId}')")
            }

            if (link != null) event.disallow(
                KICK_WHITELIST, Component.text(
                    "Twoje konto wymaga ponownej weryfikacji przez serwer Discord.\n" +
                            "Wyślij wiadomość do WhitelistBot o treści $code żeby połaczyć konta."
                )
            )
            else event.disallow(
                KICK_WHITELIST, Component.text(
                    "Twoje konto wymaga weryfikacji przez serwer Discord.\n" +
                            "Wyślij wiadomość do WhitelistBot o treści $code żeby połaczyć konta."
                )
            )
        } ?: run {
            event.disallow(KICK_WHITELIST, Component.text("Serwer tymczasowo niedostępny."))
        }
    }
}