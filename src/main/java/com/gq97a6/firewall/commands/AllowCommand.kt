package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Manager
import com.gq97a6.firewall.Manager.CodeResolveResult.Reason.*
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class AllowCommand : FirewallCommand("allow") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean =
        if (args?.size == 3) {
            //Resolve code
            val result = Manager.resolveCode(args[1], args[2])

            //Reply
            sender.sendMessage(
                when (result.reason) {
                    LINKED -> "Linked created with ${result.code?.username ?: "???"}"
                    RELINKED -> "Relinked with ${result.code?.username ?: "???"}"
                    NOT_FOUND -> "Code not found"
                    INVALID -> "Invalid code"
                    FAILED -> "Err"
                }
            )

            true
        } else {
            sender.sendMessage("Err")
            false
        }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): MutableList<String> = when (args?.size) {
        2 -> mutableListOf("<code>")
        3 -> mutableListOf("<dc_uuid>")
        else -> mutableListOf()
    }
}