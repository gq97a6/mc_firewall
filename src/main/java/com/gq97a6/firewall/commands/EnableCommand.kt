package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall
import com.gq97a6.firewall.add
import com.gq97a6.firewall.colour
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class EnableCommand : PluginCommand<PluginCommandParams>() {
    override val name = "enable"
    override val description: String = "enable the firewall"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: PluginCommandParams
    ): Boolean {
        Firewall.isFirewallOpen = false
        if (sender is ConsoleCommandSender) sender.sendMessage("Firewall enabled")
        else sender.send { add("Firewall enabled") { colour(101, 173, 0) } }

        return true
    }
}