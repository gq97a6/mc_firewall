package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall
import com.gq97a6.firewall.c
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class ShutCommand : FirewallCommand("shut") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: CommandArguments): Boolean {
        Firewall.gpdwOpen = false
        if (sender is ConsoleCommandSender) sender.sendMessage(Component.text("Firewall shut"))
        else sender.sendMessage(Component.text("Firewall shut").c(101, 173, 0))

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = mutableListOf<String>()
}