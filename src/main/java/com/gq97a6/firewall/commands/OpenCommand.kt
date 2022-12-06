package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall.Companion.gpdwOpen
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import kotlin.random.Random
import kotlin.random.nextInt

class OpenCommand : FirewallCommand("open") {
    companion object {
        var confirmCode = ""
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args?.contains(confirmCode) == true && confirmCode.isNotBlank()) {
            gpdwOpen = true
            confirmCode = ""
            sender.sendMessage(Component.text("Firewall opened").color(TextColor.color(255, 77, 77)))
        } else {
            confirmCode = Random.nextInt(1000..9999).toString()
            sender.sendMessage(Component.text().apply { c ->
                c.append(Component.text("Code: "))
                c.append(Component.text(confirmCode).decorate(TextDecoration.BOLD))
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