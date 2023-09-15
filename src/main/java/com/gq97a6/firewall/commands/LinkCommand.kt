package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Manager.link
import com.gq97a6.firewall.classes.Code
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LinkCommand : FirewallCommand("link") {
    override val help = Help(
        listOf(""),
        listOf(),
        listOf("username", "ip", "mc_uuid", "dc_uuid"),
        "link credentials"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        val result = args.none.size >= 3 && Code(args.n(1), args.n(0), args.n(2), "").link(args.n(3))
        sender.sendMessage(if (result) "Link created" else "Failed to link")
        return result
    }
}