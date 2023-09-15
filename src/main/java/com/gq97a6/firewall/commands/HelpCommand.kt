package com.gq97a6.firewall.commands

import com.gq97a6.firewall.classes.Printable.Companion.print
import com.gq97a6.firewall.listeners.CommandsListener
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpCommand : FirewallCommand("help") {
    override val help = Help(explanation = "print help")

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        val isFull = args.flags.isNotEmpty() || args.none.isNotEmpty() || args.values.isNotEmpty()
        CommandsListener.commands.map { it.help }.print(sender, isFull, false)
        return true
    }
}