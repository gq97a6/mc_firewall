package com.gq97a6.firewall.other

import net.kyori.adventure.audience.Audience
import org.bukkit.command.ConsoleCommandSender

interface Printable {
    fun printPlayer(audience: Audience) {}
    fun printConsole(audience: Audience) {}
    fun print(audience: Audience) {
        if (audience is ConsoleCommandSender) printConsole(audience)
        else printPlayer(audience)
    }

    companion object {
        fun List<Printable>?.print(audience: Audience) {
            if (audience is ConsoleCommandSender) this?.forEach { it.printConsole(audience) }
            else this?.forEach { it.printPlayer(audience) }
        }
    }
}