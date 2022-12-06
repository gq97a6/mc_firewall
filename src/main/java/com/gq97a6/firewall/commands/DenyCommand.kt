package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class DenyCommand : FirewallCommand("deny") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?) =
        DB.runAction {
            if (args?.size == 2 && args[1].isNotBlank())
                execute("DELETE FROM codes WHERE code = '${args[1]}'")
            sender.sendMessage("Deny executed")
            true
        } ?: false

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if(args?.size == 2) mutableListOf("<code>") else mutableListOf()
}