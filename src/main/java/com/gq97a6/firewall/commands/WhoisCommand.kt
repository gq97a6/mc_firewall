package com.gq97a6.firewall.commands

import github.scarsz.discordsrv.util.DiscordUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class WhoisCommand : FirewallCommand("whois") {
    override val help = Help(
        "",
        listOf(),
        listOf("dc_uuid"),
        "check discord user"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        if (args.none.isEmpty()) {
            sender.sendMessage("Invalid arguments")
            return false
        }

        val id = args.n(0).toLongOrNull() ?: run {
            sender.sendMessage("Invalid arguments")
            return false
        }

        try {
            DiscordUtil.getJda().getUserById(id).let { u ->
                sender.sendMessage("Discord name:  ${u?.name ?: "???"}")
            }
        } catch (e: Exception) {
            sender.sendMessage("Invalid arguments")
            return false
        }

        return true
    }
}