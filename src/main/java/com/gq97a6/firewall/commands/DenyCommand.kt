package com.gq97a6.firewall.commands

import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.DatabaseManager.execute
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.command.RequiredParam
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class DenyCommandParams : PluginCommandParams {
    @RequiredParam("code", "Player's code")
    var code = ""
}

class DenyCommand : PluginCommand<DenyCommandParams>() {
    override val name = "deny"
    override val description: String = "remove code from database"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: DenyCommandParams
    ): Boolean = arguments.withArguments {
        if (code.isEmpty()) {
            sender.sendMessage("Invalid player's code")
            return false
        }

        val result = DatabaseManager.runAction {
            execute("DELETE FROM codes WHERE code = '$code'")
            true
        } ?: false

        sender.sendMessage(if (result) "Deny executed" else "Failed to deny")
        return result
    }
}