package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall.Companion.gpdwOpen
import com.gq97a6.firewall.b
import com.gq97a6.firewall.c
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import kotlin.random.Random
import kotlin.random.nextInt

class OpenCommand : FirewallCommand("open") {
    companion object {
        var confirmCode = ""
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: CommandArguments): Boolean {
        if (args.none.contains(confirmCode) && confirmCode.isNotBlank()) {
            gpdwOpen = true
            confirmCode = ""

            if (sender is ConsoleCommandSender) sender.sendMessage(Component.text("Firewall opened"))
            else sender.sendMessage(Component.text("Firewall opened").c(255, 77, 77))
        } else {
            confirmCode = Random.nextInt(1000..9999).toString()
            sender.sendMessage(Component.text().apply { c ->
                c.append(Component.text("Code: "))
                c.append(Component.text(confirmCode).b())
            })
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if (args?.size == 2) mutableListOf("<confirmCode>") else mutableListOf()
}