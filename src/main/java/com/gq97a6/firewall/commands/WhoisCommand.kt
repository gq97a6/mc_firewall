package com.gq97a6.firewall.commands

import github.scarsz.discordsrv.util.DiscordUtil
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class WhoisCommand : FirewallCommand("whois") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        DiscordUtil.getJda().getUserById(args?.get(1)?.toLong() ?: 0L).let { u ->
            sender.sendMessage(Component.text().apply { c ->
                c.append(Component.text("Discord name: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0)))
                c.append(Component.text(u?.name ?: "NULL"))
            })
        }

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if (args?.size == 1) mutableListOf("<dc_uuid>") else if (args?.size == 3) mutableListOf("-f") else mutableListOf()
}