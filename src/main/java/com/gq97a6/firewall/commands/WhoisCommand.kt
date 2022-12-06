package com.gq97a6.firewall.commands

import github.scarsz.discordsrv.util.DiscordUtil
import net.kyori.adventure.text.Component
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class WhoisCommand : FirewallCommand("whois") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: CommandArguments): Boolean {
        if (args.none.isNotEmpty()) {
            sender.sendMessage("Invalid arguments")
            return false
        }

        val id = args.n(1).toLongOrNull() ?: run {
            sender.sendMessage("Invalid arguments")
            return false
        }

        try {
            DiscordUtil.getJda().getUserById(id).let { u ->
                sender.sendMessage(Component.text().apply { c ->
                    if (sender is ConsoleCommandSender) {
                        c.append(Component.text("Discord name: "))
                        c.append(Component.text(u?.name ?: "???"))
                    } else {
                        c.append(Component.text("Discord name: "))
                        c.append(Component.text(u?.name ?: "???"))
                    }
                })
            }
        } catch (e: Exception) {
            sender.sendMessage("Invalid arguments")
            return false
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) =
        if (args?.size == 2) mutableListOf("<dc_uuid>") else if (args?.size == 3) mutableListOf("-f") else mutableListOf()
}