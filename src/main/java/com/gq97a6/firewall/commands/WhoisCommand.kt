package com.gq97a6.firewall.commands

import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.command.RequiredParam
import github.scarsz.discordsrv.util.DiscordUtil
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class WhoisCommandParams : PluginCommandParams {
    @RequiredParam("dc_uuid", "discord UUID")
    var dcUuid = ""
}

class WhoisCommand : PluginCommand<WhoisCommandParams>() {
    override val name = "whois"
    override val description: String = "print discord username"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: WhoisCommandParams
    ): Boolean = arguments.withArguments {
        if (dcUuid.isEmpty()) {
            sender.sendMessage("Invalid arguments")
            return false
        }

        val id = dcUuid.toLongOrNull() ?: run {
            sender.sendMessage("Invalid arguments")
            return false
        }

        try {
            DiscordUtil.getJda().getUserById(id).let { u ->
                sender.sendMessage("Discord name:  ${u?.name ?: "???"}")
            }
        } catch (e: Exception) {
            sender.sendMessage("Invalid arguments")
            return false
        }

        return true
    }
}