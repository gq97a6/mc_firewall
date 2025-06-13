package com.gq97a6.firewall.commands

import com.gq97a6.firewall.other.Code
import com.gq97a6.firewall.managers.LinkManager.link
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.command.RequiredParam
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class LinkCommandParams : PluginCommandParams {
    @RequiredParam("username", "Player's username")
    var username = ""

    @RequiredParam("ip", "Player's IP address")
    var ip = ""

    @RequiredParam("mc_uuid", "Player's Minecraft account UUID")
    var mcUuid = ""

    @RequiredParam("dc_uuid", "Player's Discord account UUID")
    var dcUuid = ""
}

class LinkCommand : PluginCommand<LinkCommandParams>() {
    override val name = "link"
    override val description: String = "create new link"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: LinkCommandParams
    ): Boolean = arguments.withArguments {

        if (username.isEmpty()) {
            sender.sendMessage("Username not provided")
            return false
        }

        if (ip.isEmpty()) {
            sender.sendMessage("IP address not provided")
            return false
        }

        if (mcUuid.isEmpty()) {
            sender.sendMessage("Minecraft account UUID not provided")
            return false
        }

        if (dcUuid.isEmpty()) {
            sender.sendMessage("Discord account UUID not provided")
            return false
        }


        Code(username, ip, mcUuid, "").link(dcUuid).let {
            sender.sendMessage(if (it) "Link created" else "Failed to link")
            return it
        }
    }
}