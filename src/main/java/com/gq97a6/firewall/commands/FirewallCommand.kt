package com.gq97a6.firewall.commands

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

sealed class FirewallCommand(val name: String) {
    abstract fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean
    abstract fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<out String>?): MutableList<String>?
}