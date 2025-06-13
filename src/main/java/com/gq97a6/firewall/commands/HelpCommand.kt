package com.gq97a6.firewall.commands

import com.gq97a6.firewall.command.*
import com.gq97a6.firewall.listeners.CommandsListener
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class HelpCommandParams : PluginCommandParams {
    @RequiredParam("command", "command")
    var subCommand = ""
}

class HelpCommand : PluginCommand<HelpCommandParams>() {
    override val name: String = "help"
    override val description: String = "print help"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: HelpCommandParams
    ): Boolean = arguments.withArguments {
        if (subCommand.isNotBlank()) {
            val paramPattern = CommandsListener.paramPatterns[subCommand]
            val cmd = CommandsListener.commands[subCommand]

            if (paramPattern == null || cmd == null) {
                sender.sendMessage("This command does not exist")
                return false
            }

            cmd.buildHelp(paramPattern).print(sender)
        } else CommandsListener.commands.forEach {
            sender.sendMessage("${it.value.name}: ${it.value.description}")
        }

        return true
    }
}