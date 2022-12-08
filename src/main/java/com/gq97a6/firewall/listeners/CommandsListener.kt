package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.commands.FirewallCommand
import com.gq97a6.firewall.commands.FirewallCommand.Arguments
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import kotlin.reflect.full.primaryConstructor

object CommandsListener {
    private val commands = buildList {
        FirewallCommand::class.sealedSubclasses.forEach {
            it.primaryConstructor?.call()?.let { command ->
                add(command)
            }
        }
    }

    fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if (args?.size == 1)
        mutableListOf(
            "help",
            "allow",
            "deny",
            "link",
            "unlink",
            "pardon",
            "ban",
            "open",
            "shut",
            "whois",
            "select",
            "purge"
        )
    else commands.find { it.name == args?.get(0) }?.onTabComplete(sender, command, alias, args)

    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?) =
        if (args?.size != 0) commands.find { it.name == args?.get(0) }
            ?.onCommand(sender, command, label, Arguments(args)) ?: true
        else true
}