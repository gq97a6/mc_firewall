package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ShutCommand : FirewallCommand("shut") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        Firewall.gpdwOpen = false
        sender.sendMessage(Component.text("Firewall shut").color(TextColor.color(101, 173, 0)))

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = mutableListOf<String>()
}