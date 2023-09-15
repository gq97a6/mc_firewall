package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PurgeCommand : FirewallCommand("purge") {
    override val help = Help(
        listOf(""),
        listOf(),
        listOf(),
        "purge database"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments) =
        DB.runAction {
            execute("DELETE FROM links WHERE added < NOW() - INTERVAL 100 HOUR")
            sender.sendMessage("Database purged")
            true
        } ?: false
}