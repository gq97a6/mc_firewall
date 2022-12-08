package com.gq97a6.firewall.classes

import net.kyori.adventure.audience.Audience
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

interface Printable {
    fun printPlayer(audience: Audience, vararg args: Boolean) {}
    fun printPlayerCompact(audience: Audience, vararg args: Boolean) {}
    fun printConsole(audience: Audience, vararg args: Boolean) {}

    companion object {
        fun List<Printable>?.print(audience: Audience, vararg args: Boolean) {
            this?.forEach(
                if (audience is ConsoleCommandSender)
                    { p: Printable -> p.printConsole(audience, args[0]) }
                else if (args[1])
                    { p: Printable -> p.printPlayerCompact(audience) }
                else
                    { p: Printable -> p.printPlayer(audience) }
            )
        }
    }
}