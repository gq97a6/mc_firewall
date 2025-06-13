package com.gq97a6.firewall.commands

import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.DatabaseManager.execute
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.command.RequiredParam
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class UnlinkCommandParams : PluginCommandParams {
    @RequiredParam("id", "id of link to remove")
    var id = ""
}

class UnlinkCommand : PluginCommand<UnlinkCommandParams>() {
    override val name = "unlink"
    override val description: String = "remove a link"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: UnlinkCommandParams
    ): Boolean = arguments.withArguments {
        val result = if (id.isNotEmpty()) {
            DatabaseManager.runAction {
                execute("DELETE FROM links WHERE id = '$id'")
                true
            } ?: false
        } else false

        sender.sendMessage(if (result) "Unlink executed" else "Failed to link")
        return result
    }
}