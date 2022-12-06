package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class UnlinkCommand : FirewallCommand("unlink") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?) =
        DB.runAction {
            if (args?.size == 2 && args[1].isNotBlank())
                execute("DELETE FROM links WHERE id = '${args[1]}'")
                sender.sendMessage("Unlink executed")
            true
        } ?: false

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if (args?.size == 2) mutableListOf("<id>") else mutableListOf()
}