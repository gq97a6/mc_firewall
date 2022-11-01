package com.gq97a6.firewall.listeners

import github.scarsz.discordsrv.api.ListenerPriority
import github.scarsz.discordsrv.api.Subscribe
import github.scarsz.discordsrv.api.events.DiscordPrivateMessageReceivedEvent
import github.scarsz.discordsrv.api.events.DiscordReadyEvent
import github.scarsz.discordsrv.util.DiscordUtil
import com.gq97a6.firewall.AuthManager
import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.executeQuery
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link

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
                event.message.reply("Nie należysz do serwera.").queue()
                return
            } else if (u.jda.roles.find { it.id == "1006279637699154012" } == null) {
                event.message.reply("Nie posiadasz wymaganej roli.").queue()
                return
            }
        }

        //Validate code
        val codeSend = event.message.contentStripped.filter { it.isDigit() }
        if (codeSend.length != 5) {
            event.message.reply("Nieprawidłowy kod.").queue()
            return
        }

        val r = DB.runAction {
            //Get code from database
            val code = executeQuery("SELECT * FROM CODES WHERE code = '$codeSend'")?.let {
                if (it.next()) {
                    Code(
                        it.getString("ip"),
                        it.getString("username"),
                        it.getString("mc_uuid"),
                        codeSend,
                    )
                } else null
            } ?: return@runAction Pair(null, null)

            //Get link from database
            val links =
                executeQuery("SELECT * FROM LINKS WHERE dc_uuid = '$dcUUID'")?.let {
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
                } ?: return@runAction Pair(null, code)

            return@runAction Pair(links, code)
        }

        val code = r?.second
        val dcLinks = r?.first?.filter { it.dcUUID == dcUUID } ?: listOf()
        val mcLinks = r?.first?.filter { it.mcUUID == code?.mcUUID } ?: listOf()

        if (code == null) event.message.reply("❌ Nie znaleziono takiego kodu.").queue()
        else if (dcLinks.isEmpty() && mcLinks.isEmpty() && AuthManager.link(code, dcUUID))
            event.message.reply("✅ Twoje konto zostało połączone z ${code.username}.").queue()
        else if (mcLinks.isNotEmpty()) {
            AuthManager.changeIp(code)
            event.message.reply("✅ Odnowiono połączenie z ${code.username}.").queue()
        } else event.message.reply("❌ Nie znaleziono takiego kodu.").queue()
    }

//@Subscribe
//fun accountsLinked(event: AccountLinkedEvent) {
//    // Example of broadcasting a message when a new account link has been made
//    //Bukkit.broadcastMessage(event.player.name + " just linked their MC account to their Discord user " + event.user + "!")
//    plugin.logger.info("LINKED: ${event.player.player?.address}")
//}

//@Subscribe
//fun accountUnlinked(event: AccountUnlinkedEvent) {
//
//    DiscordUtil.getJda().getUserById(event.discordId)?.openPrivateChannel()?.queue { privateChannel ->
//        privateChannel.sendMessage("Your account has been unlinked").queue()
//    }
//
//    //Example of sending a message to a channel called "unlinks" (defined in the config.yml using the Channels option) when a user unlinks
//    //val textChannel = DiscordSRV.getPlugin().getDestinationTextChannelForGameChannelName("unlinks")
//
//    //if (textChannel != null) {
//    //    textChannel.sendMessage(
//    //        event.player.name + " (" + event.player.uniqueId + ") has unlinked their associated Discord account: "
//    //                + (if (event.discordUser != null) event.discordUser.name else "<not available>") + " (" + event.discordId + ")"
//    //    ).queue()
//    //} else {
//    //    plugin.logger.warning("Channel called \"unlinks\" could not be found in the DiscordSRV configuration")
//    //}
//}

//@Subscribe(priority = ListenerPriority.NORMAL)
//fun discordGuildMessageReceivedEvent(event: DiscordGuildMessageReceivedEvent) {
//    // Example of logging a message sent in Discord
//    plugin.logger.info("DiscordGuildMessageReceivedEvent")
//}
//
//@Subscribe(priority = ListenerPriority.NORMAL)
//fun discordGuildMessageSentEvent(event: DiscordGuildMessageSentEvent) {
//    // Example of logging a message sent in Minecraft (being sent to Discord)
//    plugin.logger.info("DiscordGuildMessageSentEvent")
//}

//@Subscribe(priority = ListenerPriority.NORMAL)
//fun discordPrivateMessageSentEvent(event: DiscordPrivateMessageSentEvent) {
//    // Example of logging a message sent in Discord
//    plugin.logger.info("DiscordPrivateMessageSentEvent")
//}
}