package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BanCommand : FirewallCommand("ban") {
    override val help = Help(
        listOf(),
        listOf("dc_uuid", "mc_uuid", "username", "ip"),
        listOf(),
        "ban credential"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {
        val result = DB.runAction {
            execute("INSERT INTO bans (ip, username, dc_uuid, mc_uuid) VALUES (" +
                    "${args.v("ip")?.let { "'$it'" } ?: "NULL"}, " +
                    "${args.v("username")?.let { "'$it'" } ?: "NULL"}, " +
                    "${args.v("dc_uuid")?.let { "'$it'" } ?: "NULL"}, " +
                    "${args.v("mc_uuid")?.let { "'$it'" } ?: "NULL"})")

            true
        } ?: false

        sender.sendMessage(if (result) "Ban executed" else "Failed to ban")
        return result
    }
}