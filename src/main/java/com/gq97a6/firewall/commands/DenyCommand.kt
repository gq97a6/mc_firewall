package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class DenyCommand : FirewallCommand("deny") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: CommandArguments): Boolean {
        val result = if (args.none.isNotEmpty()) {
            DB.runAction {
                execute("DELETE FROM codes WHERE code = '${args.n(0)}'")
                true
            } ?: false
        } else false

        sender.sendMessage(if (result) "Deny executed" else "Deny failed")
        return result
    }


    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if (args?.size == 2) mutableListOf("<code>") else mutableListOf()
}