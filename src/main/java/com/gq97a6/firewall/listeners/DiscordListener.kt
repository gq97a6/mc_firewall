package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.Manager
import com.gq97a6.firewall.Manager.CodeResolveResult.Reason.*
import github.scarsz.discordsrv.api.ListenerPriority
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordPrivateMessageReceivedEvent
import github.scarsz.discordsrv.api.events.DiscordReadyEvent
import github.scarsz.discordsrv.util.DiscordUtil

open class DiscordListener {

    @Subscribe
    fun onDiscordReady(event: DiscordReadyEvent?) {
        DiscordUtil.getJda().addEventListener(JDAListener())
        //plugin.logger.info("Chatting on Discord with " + DiscordUtil.getJda().users.size + " users!")
    }

    @Subscribe(priority = ListenerPriority.NORMAL)
    fun onDiscordPrivateMessageReceived(event: DiscordPrivateMessageReceivedEvent) {

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

        //Resolve code
        val result = event.message.contentStripped.filter { it.isDigit() }.let {
            Manager.resolveCode(it, dcUUID)
        }

        //Replay
        event.message.reply(
            when (result.reason) {
                LINKED -> "✅ Twoje konto zostało połączone z ${result.code?.username ?: "❌"}."
                RELINKED -> "✅ Odnowiono połączenie z ${result.code?.username ?: "❌"}."
                NOT_FOUND, FAILED, INVALID, BANNED -> "❌ Nie znaleziono takiego kodu."
            }
        ).queue()
    }
}