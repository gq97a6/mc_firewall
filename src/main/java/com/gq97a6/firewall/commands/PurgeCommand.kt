package com.gq97a6.firewall.commands

import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.DatabaseManager.execute
import com.gq97a6.firewall.command.LongOptionParam
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PurgeCommandParams : PluginCommandParams {
    @LongOptionParam("time", "hours", "time threshold")
    var hours = ""
}

class PurgeCommand : PluginCommand<PurgeCommandParams>() {
    override val name = "purge"
    override val description: String = "remove links older than time threshold from database"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: PurgeCommandParams
    ): Boolean = arguments.withArguments {
        val hours = hours.toIntOrNull()

        if (hours == null || hours !in 1..720) {
            sender.sendMessage("Incorrect number of hours")
            return false
        }

        DatabaseManager.runAction {
            execute("DELETE FROM links WHERE added < NOW() - INTERVAL $hours HOUR")
            sender.sendMessage("Database purged")
            true
        } ?: false
        return true
    }
}