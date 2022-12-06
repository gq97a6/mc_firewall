package com.gq97a6.firewall.commands

import com.gq97a6.firewall.AuthManager
import com.gq97a6.firewall.classes.Code
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class AllowCommand : FirewallCommand("allow") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean =
        if (args?.size == 5 && AuthManager.link(Code(args[2], args[1], args[3], ""), args[4])) {
            sender.sendMessage("Link created")
            true
        } else {
            sender.sendMessage("Unlink failed")
            false
        }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): MutableList<String> = when (args?.size) {
        2 -> mutableListOf("<username>")
        3 -> mutableListOf("<ip>")
        4 -> mutableListOf("<mc_uuid>")
        5 -> mutableListOf("<dc_uuid>")
        else -> mutableListOf()
    }
}