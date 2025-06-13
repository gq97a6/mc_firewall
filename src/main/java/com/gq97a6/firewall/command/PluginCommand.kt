package com.gq97a6.firewall.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

abstract class PluginCommand<T : PluginCommandParams> {
    abstract val name: String
    abstract val description: String

    inline fun T.withArguments(action: T.() -> Boolean): Boolean = this.action()

    abstract fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: T
    ): Boolean

    open fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        arguments: Array<out String>
    ): List<String> = listOf()
}