package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.Firewall.Companion.botName
import com.gq97a6.firewall.Firewall.Companion.discordRequiredRoleID
import com.gq97a6.firewall.Firewall.Companion.discordServerId
import com.gq97a6.firewall.Firewall.Companion.isFirewallOpen
import com.gq97a6.firewall.add
import com.gq97a6.firewall.bold
import com.gq97a6.firewall.colour
import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.DatabaseManager.execute
import com.gq97a6.firewall.managers.DatabaseManager.executeQuery
import com.gq97a6.firewall.managers.StaticText.onError
import com.gq97a6.firewall.managers.StaticText.onLinkRequired
import com.gq97a6.firewall.managers.StaticText.onNotOnServer
import com.gq97a6.firewall.managers.StaticText.onRelinkRequired
import com.gq97a6.firewall.managers.StaticText.onRequiredRoleMissing
import com.gq97a6.firewall.other.Link
import github.scarsz.discordsrv.util.DiscordUtil
import net.kyori.adventure.text.Component
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerPreLoginEvent
import org.bukkit.event.player.AsyncPlayerPreLoginEvent.Result.KICK_WHITELIST
import kotlin.random.Random

class ConnectionsListener : Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    fun onAsyncPlayerPreLoginEvent(event: AsyncPlayerPreLoginEvent) {

        if (isFirewallOpen) {
            event.allow()
            return
        }

        DatabaseManager.runAction {
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
                    DiscordUtil.getJda().getGuildById(discordServerId)?.getMemberById(link.dcUUID).let { m ->
                        if (m == null) {
                            event.disallow(
                                KICK_WHITELIST,
                                Component.text(onNotOnServer)
                            )
                            return@runAction
                        } else if (m.roles.none { role -> role.id == discordRequiredRoleID }) {
                            event.disallow(
                                KICK_WHITELIST,
                                Component.text(onRequiredRoleMissing)
                            )
                            return@runAction
                        }
                    }
                } catch (_: Exception) {
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
                KICK_WHITELIST,
                Component.text(onRelinkRequired.replace("<botName>", botName).replace("<code>", code))
            )
            else event.disallow(
                KICK_WHITELIST,
                Component.text(onLinkRequired.replace("<botName>", botName).replace("<code>", code))
            )
        } ?: run {
            event.disallow(KICK_WHITELIST, Component.text(onError))
        }
    }
}