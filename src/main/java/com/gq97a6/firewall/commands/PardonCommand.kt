package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PardonCommand : FirewallCommand("pardon") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: CommandArguments): Boolean {
        val result = DB.runAction {
            execute(
                if (args.f('a'))
                    "DELETE FROM bans WHERE " +
                            "(ip = '${args.v("ip")}' AND ip IS NOT NULL) OR " +
                            "(username = '${args.v("username")}' AND username IS NOT NULL) OR " +
                            "(dc_uuid = '${args.v("dc_uuid")}' AND dc_uuid IS NOT NULL) OR " +
                            "(mc_uuid = '${args.v("mc_uuid")}' AND mc_uuid IS NOT NULL)"
                else
                    "DELETE FROM bans WHERE " +
                            "ip = '${args.v("ip")}' AND " +
                            "username = '${args.v("username")}' AND " +
                            "dc_uuid = '${args.v("dc_uuid")}' AND " +
                            "mc_uuid = '${args.v("mc_uuid")}'"
            )
            true
        } ?: false

        sender.sendMessage(if (result) "Pardon executed" else "Failed to pardon")
        return result
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = mutableListOf("--dc_uuid", "--mc_uuid", "--username", "--ip")
        .apply { removeAll(args?.toList() ?: listOf()) }
}