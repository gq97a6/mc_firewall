package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.AuthManager
import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.executeQuery
import com.gq97a6.firewall.Firewall.Companion.plugin
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link
import github.scarsz.discordsrv.api.ListenerPriority
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordPrivateMessageReceivedEvent
import github.scarsz.discordsrv.api.events.DiscordReadyEvent
import github.scarsz.discordsrv.util.DiscordUtil

open class DiscordListener {

    @Subscribe
    fun discordReadyEvent(event: DiscordReadyEvent?) {
        DiscordUtil.getJda().addEventListener(JDAListener())
        //plugin.logger.info("Chatting on Discord with " + DiscordUtil.getJda().users.size + " users!")
    }

    @Subscribe(priority = ListenerPriority.NORMAL)
    fun discordPrivateMessageReceivedEvent(event: DiscordPrivateMessageReceivedEvent) {

        val dcUUID = event.author.id

        //Check discord
        DiscordUtil.getJda().getUserById(dcUUID).let { u ->
            if (u == null) {
                event.message.reply("❌ Nie należysz do serwera.").queue()
                return
            } else if (u.jda.roles.find { it.id == "1006279637699154012" } == null) {
                event.message.reply("❌ Nie posiadasz wymaganej roli.").queue()
                return
            }
        }

        //Validate code
        val codeSend = event.message.contentStripped.filter { it.isDigit() }
        if (codeSend.length != 5) {
            event.message.reply("❌ Nieprawidłowy kod.").queue()
            return
        }

        var code: Code? = null
        var links: MutableList<Link>? = null
        var bans: MutableList<Link>? = null

        DB.runAction {
            //Get code
            code = executeQuery("SELECT * FROM codes WHERE code = '$codeSend'")?.let {
                if (it.next()) {
                    Code(
                        it.getString("ip"),
                        it.getString("username"),
                        it.getString("mc_uuid"),
                        codeSend,
                    )
                } else null
            }

            //Get links
            links =
                executeQuery("SELECT * FROM links WHERE dc_uuid = '$dcUUID' OR mc_uuid = '${code?.mcUUID ?: ""}'")?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid")
                                )
                            )
                        }
                    }
                }

            //Get bans
            bans =
                executeQuery("SELECT * FROM bans WHERE dc_uuid = '$dcUUID' OR mc_uuid = '${code?.mcUUID ?: "null"}' OR ip = '${code?.ip ?: "null"}' OR username = '${code?.username ?: "null"}'")?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid")
                                )
                            )
                        }
                    }
                }
        }

        val discordExists = links?.find { it.dcUUID == dcUUID } != null
        val minecraftExists = links?.find { it.mcUUID == code?.mcUUID } != null
        val bothMatch = links?.find { it.mcUUID == code?.mcUUID && it.dcUUID == dcUUID } != null

        //Code not found
        if (code == null || bans?.isNotEmpty() == true)
            event.message.reply("❌ Nie znaleziono takiego kodu.").queue()

        //This minecraft account and discord account are both not yet linked
        else if (!discordExists && !minecraftExists && AuthManager.link(code!!, dcUUID))
            event.message.reply("✅ Twoje konto zostało połączone z ${code!!.username}.").queue()

        //This discord account is associated with this code
        else if (bothMatch && AuthManager.changeIp(code!!))
            event.message.reply("✅ Odnowiono połączenie z ${code!!.username}.").queue()

        //Fail
        else event.message.reply("❌ Nie znaleziono takiego kodu. ").queue()
    }
}