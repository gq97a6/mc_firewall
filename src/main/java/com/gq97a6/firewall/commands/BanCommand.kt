package com.gq97a6.firewall.commands

import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.DatabaseManager.execute
import com.gq97a6.firewall.command.LongOptionParam
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BanCommandParams : PluginCommandParams {
    @LongOptionParam("username", "username", "Player's username")
    var username = ""

    @LongOptionParam("ip", "address", "Player's IP address")
    var ip = ""

    @LongOptionParam("mc", "uuid", "Player's Minecraft account UUID")
    var mcUuid = ""

    @LongOptionParam("dc", "uuid", "Player's Discord account UUID")
    var dcUuid = ""
}

class BanCommand : PluginCommand<BanCommandParams>() {
    override val name = "ban"
    override val description: String = "ban player by selector"


    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: BanCommandParams
    ): Boolean = arguments.withArguments {
        if (username.isEmpty() && ip.isEmpty() && mcUuid.isEmpty() && dcUuid.isEmpty()) {
            sender.sendMessage("No search parameters were provided")
            return false
        }

        val result = DatabaseManager.runAction {
            buildString {
                append("INSERT INTO bans (ip, username, dc_uuid, mc_uuid) VALUES (")
                append("${ip.ifEmpty { "NULL" }}, ")
                append("${username.ifEmpty { "NULL" }}, ")
                append("${mcUuid.ifEmpty { "NULL" }}, ")
                append("${dcUuid.ifEmpty { "NULL" }})")
            }.let { execute(it) }
            true
        } ?: false

        sender.sendMessage(if (result) "Ban executed" else "Failed to ban")
        return result
    }
}