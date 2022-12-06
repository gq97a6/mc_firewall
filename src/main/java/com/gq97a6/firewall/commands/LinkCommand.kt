package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Manager.link
import com.gq97a6.firewall.classes.Code
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LinkCommand : FirewallCommand("link") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: CommandArguments): Boolean {
        val result = args.none.size == 5 && Code(args.n(2), args.n(1), args.n(3), "").link(args.n(4))
        sender.sendMessage(if (result) "Link created" else "Unlink failed")
        return result
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