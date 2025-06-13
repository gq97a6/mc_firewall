package com.gq97a6.firewall.commands

import com.gq97a6.firewall.Firewall.Companion.isFirewallOpen
import com.gq97a6.firewall.add
import com.gq97a6.firewall.bold
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.command.RequiredParam
import com.gq97a6.firewall.send
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import kotlin.random.Random
import kotlin.random.nextInt

class DisableCommandParams : PluginCommandParams {
    @RequiredParam("confirm_code", "confirm code")
    var confirmCode = ""
}

class DisableCommand : PluginCommand<DisableCommandParams>() {
    override val name = "disable"
    override val description: String = "temporary disable the firewall"

    companion object {
        var currentConfirmCode = ""
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: DisableCommandParams
    ): Boolean = arguments.withArguments {

        if (currentConfirmCode.isNotBlank() && confirmCode == currentConfirmCode) {
            isFirewallOpen = true
            currentConfirmCode = ""
            sender.sendMessage("Firewall disabled")
        } else {
            currentConfirmCode = Random.nextInt(1000..9999).toString()
            sender.send {
                add("Code: ")
                add(currentConfirmCode) { bold() }
            }
        }

        return true
    }
}