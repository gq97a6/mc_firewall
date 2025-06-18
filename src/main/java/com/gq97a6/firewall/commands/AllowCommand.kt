package com.gq97a6.firewall.commands

import com.gq97a6.firewall.managers.LinkManager
import com.gq97a6.firewall.managers.LinkManager.CodeResolveResult.Reason.*
import com.gq97a6.firewall.other.Printable.Companion.print
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.command.RequiredParam
import com.gq97a6.firewall.command.ShortFlagParam
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class AllowCommandParams : PluginCommandParams {
    @RequiredParam("code", "Player's code")
    var code = ""

    @RequiredParam("dc_uuid", "Player's Discord UUID")
    var dcUuid = ""

    @ShortFlagParam('a', "Override links limit per Discord account")
    var overrideAccountLimit = false

    @ShortFlagParam('i', "Overrides links limit per IP")
    var overrideIpLimit = false
}

class AllowCommand : PluginCommand<AllowCommandParams>() {
    override val name = "allow"
    override val description = "allow player in by the code"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: AllowCommandParams
    ): Boolean = arguments.withArguments {
        if (code.isEmpty()) {
            sender.sendMessage("Invalid player's code")
            return false
        }

        if (dcUuid.isEmpty()) {
            sender.sendMessage("Invalid player's Discord UUID")
            return false
        }

        //Resolve code
        val result = LinkManager.resolveCode(code, dcUuid, overrideAccountLimit, overrideIpLimit)

        //Reply
        sender.sendMessage(
            when (result.reason) {
                LINKED -> "Successfully linked accounts for ${result.code?.username ?: "???"}"
                RELINKED -> "Successfully relinked accounts for ${result.code?.username ?: "???"}"
                NOT_FOUND -> "Code not found"
                INVALID -> "Invalid player's code"
                FAILED -> "Failed to resole the code"
                BANNED -> "Credentials banned"
            }
        )

        if (result.reason == FAILED || result.reason == BANNED) {
            result.links.print(sender)
            result.bans.print(sender)
        }

        return true
    }
}