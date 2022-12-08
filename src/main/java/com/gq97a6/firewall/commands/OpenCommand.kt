package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall.Companion.gpdwOpen
import com.gq97a6.firewall.add
import com.gq97a6.firewall.b
import com.gq97a6.firewall.c
import com.gq97a6.firewall.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender
import kotlin.random.Random
import kotlin.random.nextInt

class OpenCommand : FirewallCommand("open") {
    override val help = Help(
        "",
        listOf(),
        listOf("confirmCode"),
        "disable firewall"
    )

    companion object {
        var confirmCode = ""
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        if (args.none.contains(confirmCode) && confirmCode.isNotBlank()) {
            gpdwOpen = true
            confirmCode = ""

            if (sender is ConsoleCommandSender) sender.sendMessage("Firewall opened")
            else sender.send { add("Firewall opened") { c(255, 77, 77) } }
        } else {
            confirmCode = Random.nextInt(1000..9999).toString()
            sender.send {
                add("Code: ")
                add(confirmCode) { b() }
            }
        }

        return true
    }
}