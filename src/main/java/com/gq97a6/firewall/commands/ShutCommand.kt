package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall
import com.gq97a6.firewall.add
import com.gq97a6.firewall.c
import com.gq97a6.firewall.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class ShutCommand : FirewallCommand("shut") {
    override val help = Help(
        listOf(),
        listOf(),
        listOf(),
        "activate firewall"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        Firewall.isOpen = false
        if (sender is ConsoleCommandSender) sender.sendMessage("Firewall shut")
        else sender.send { add("Firewall shut") { c(101, 173, 0) } }

        return true
    }
}