package com.gq97a6.firewall.classes

import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

interface Printable {
    fun printPlayer(sender: CommandSender)
    fun printPlayerCompact(sender: CommandSender)
    fun printConsole(sender: CommandSender, commands: Boolean)

    companion object {
        fun List<Printable>?.print(sender: CommandSender, commands: Boolean, compact: Boolean) {
            this?.forEach(
                if (sender is ConsoleCommandSender)
                    { p: Printable -> p.printConsole(sender, commands) }
                else if (compact)
                    { p: Printable -> p.printPlayer(sender) }
                else
                    { p: Printable -> p.printPlayerCompact(sender) }
            )
        }
    }
}