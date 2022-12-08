package com.gq97a6.firewall.commands

import com.gq97a6.firewall.listeners.CommandsListener
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class HelpCommand : FirewallCommand("help") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        //CommandsListener.commands.forEach(
        //    if (sender is ConsoleCommandSender)
        //        { c: FirewallCommand -> c.help.printConsole(sender, args.f('c')) }
        //    else
        //        { c: FirewallCommand -> c.help.printPlayer(sender, args.f('c')) }
        //)
        return true
    }
}