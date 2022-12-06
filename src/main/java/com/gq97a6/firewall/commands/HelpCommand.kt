package com.gq97a6.firewall.commands

import com.gq97a6.firewall.b
import com.gq97a6.firewall.c
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.TextComponent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpCommand : FirewallCommand("help") {
    
    
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: CommandArguments): Boolean {
        sender.sendMessage(Component.text().apply { c ->
            c.append(Component.text("LINK").c(252, 73, 3).b())
            c.append(Component.text(" WHOIS").c(0, 127, 212).b())
        })

        return true
    }


    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if (args?.size == 2) mutableListOf("<id>") else mutableListOf()
}