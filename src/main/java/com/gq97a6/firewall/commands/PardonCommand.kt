package com.gq97a6.firewall.commands

import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.DatabaseManager.execute
import com.gq97a6.firewall.command.LongOptionParam
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PardonCommandParams : PluginCommandParams {
    @LongOptionParam("dc", "uuid", "Player's Discord account UUID")
    var dcUuid = ""
    @LongOptionParam("mc", "uuid", "Player's Minecraft account UUID")
    var mcUuid = ""
    @LongOptionParam("username", "username", "Player's username")
    var username = ""
    @LongOptionParam("ip", "address", "Player's IP address")
    var ip = ""
}

class PardonCommand : PluginCommand<PardonCommandParams>() {
    override val name = "pardon"
    override val description: String = "lift every ban that matches all filters"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: PardonCommandParams
    ): Boolean = arguments.withArguments {

        if (username.isEmpty() && ip.isEmpty() && mcUuid.isEmpty() && dcUuid.isEmpty()) {
            sender.sendMessage("No search parameters were provided")
            return false
        }

        val result = DatabaseManager.runAction {
            buildString {
                append("DELETE FROM bans WHERE ")
                if (ip.isNotBlank()) append("ip = '$ip' AND ")
                if (username.isNotBlank()) append("username = '$username' AND ")
                if (dcUuid.isNotBlank()) append("dc_uuid = '$dcUuid' AND ")
                if (mcUuid.isNotBlank()) append("mc_uuid = '$mcUuid' AND ")
                append("1=1")
            }.let { execute(it) }

            return@runAction true
        } ?: false

        sender.sendMessage(if (result) "Pardon executed" else "Failed to pardon")
        return result
    }
}