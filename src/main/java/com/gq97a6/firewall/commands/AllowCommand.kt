package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Manager
import com.gq97a6.firewall.Manager.CodeResolveResult.Reason.*
import com.gq97a6.firewall.classes.Printable.Companion.print
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class AllowCommand : FirewallCommand("allow") {
    override val help = Help(
        listOf("d(ignore dc)", "m(ignore mc)", "r(reason)", "f(full)"),
        listOf(),
        listOf("code", "dc_uuid"),
        "parse code"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        if (args.none.size >= 2) {
            //Resolve code
            val result = Manager.resolveCode(args.n(0), args.n(1), args.f('d'), args.f('m'))

            //Reply
            sender.sendMessage(
                when (result.reason) {
                    LINKED -> "Linked created with ${result.code?.username ?: "???"}"
                    RELINKED -> "Relinked with ${result.code?.username ?: "???"}"
                    NOT_FOUND -> "Code not found"
                    INVALID -> "Invalid code"
                    FAILED -> "Credentials linked or crash"
                    BANNED -> "Credentials banned"
                }
            )

            if ((result.reason == FAILED || result.reason == BANNED) && args.f('r')) {
                listOf(result.links, result.bans).forEach {
                    it.print(
                        sender,
                        args.f('f'),
                        args.f('f')
                    )
                }
            }

            return true
        } else {
            sender.sendMessage("Invalid arguments")
            return false
        }
    }
}