package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.Firewall.Companion.discordRequiredRoleID
import com.gq97a6.firewall.managers.LinkManager
import com.gq97a6.firewall.managers.LinkManager.CodeResolveResult.Reason.*
import com.gq97a6.firewall.managers.StaticText.onBotCodeNotFound
import com.gq97a6.firewall.managers.StaticText.onBotError
import com.gq97a6.firewall.managers.StaticText.onBotInvalidCodeSupplied
import com.gq97a6.firewall.managers.StaticText.onBotLinkSuccessful
import com.gq97a6.firewall.managers.StaticText.onBotMinecraftAlreadyLinked
import com.gq97a6.firewall.managers.StaticText.onBotNotOnServer
import com.gq97a6.firewall.managers.StaticText.onBotPlayerBanned
import com.gq97a6.firewall.managers.StaticText.onBotRelinkSuccessful
import com.gq97a6.firewall.managers.StaticText.onBotRequiredRoleMissing
import github.scarsz.discordsrv.api.ListenerPriority
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordPrivateMessageReceivedEvent
import github.scarsz.discordsrv.api.events.DiscordReadyEvent
import github.scarsz.discordsrv.util.DiscordUtil

open class DiscordListener {

    @Subscribe
    fun onDiscordReady(event: DiscordReadyEvent?) {
        DiscordUtil.getJda().addEventListener(JDAListener())
    }

    @Subscribe(priority = ListenerPriority.NORMAL)
    fun onDiscordPrivateMessageReceived(event: DiscordPrivateMessageReceivedEvent) {
        val dcUUID = event.author.id

        //Check discord
        DiscordUtil.getJda().getUserById(dcUUID).let { user ->
            if (user == null) {
                event.message.reply(onBotNotOnServer).queue()
                return
            } else if (user.jda.roles.find { it.id == discordRequiredRoleID } == null) {
                event.message.reply(onBotRequiredRoleMissing).queue()
                return
            }
        }

        //Resolve code
        val result = event.message.contentStripped.filter { it.isDigit() }.let {
            LinkManager.resolveCode(it, dcUUID)
        }

        //Replay
        event.message.reply(
            when (result.reason) {
                LINKED -> "$onBotLinkSuccessful ${result.code?.username ?: "❌"}."
                RELINKED -> "$onBotRelinkSuccessful ${result.code?.username ?: "❌"}."
                INVALID -> onBotInvalidCodeSupplied
                BANNED -> onBotPlayerBanned
                NOT_FOUND -> onBotCodeNotFound
                FAILED -> {
                    val linked = result.links?.filter { it.dcUUID == dcUUID } ?: listOf()

                    //Found that dc account is already linked to another mc
                    if (linked.isNotEmpty()) onBotMinecraftAlreadyLinked

                    //Did not find this dc account in result
                    //but links are not empty so this mc account is already linked
                    else if (result.links?.isNotEmpty() == true) onBotMinecraftAlreadyLinked
                    else onBotError
                }
            }
        ).queue()
    }
}