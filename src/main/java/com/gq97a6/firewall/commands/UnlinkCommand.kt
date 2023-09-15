package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class UnlinkCommand : FirewallCommand("unlink") {
    override val help = Help(
        listOf(""),
        listOf(),
        listOf("id"),
        "unlink credentials"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        val result = if (args.none.isNotEmpty() && args.n(0).isNotBlank()) {
            DB.runAction {
                execute("DELETE FROM links WHERE id = '${args.n(0)}'")
                true
            } ?: false
        } else false

        sender.sendMessage(if (result) "Unlink executed" else "Failed to link")
        return result
    }
}