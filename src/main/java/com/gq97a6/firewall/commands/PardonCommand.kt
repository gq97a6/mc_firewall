package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import com.gq97a6.firewall.param
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PardonCommand : FirewallCommand("link") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val p = mutableMapOf<String, String>()
        args?.param("--dc_uuid")?.let { p.put("dc", "dc_uuid = '$it'") }
        args?.param("--mc_uuid")?.let { p.put("mc", "mc_uuid = '$it'") }
        args?.param("--username")?.let { p.put("un", "username = '$it'") }
        args?.param("--ip")?.let { p.put("ip", "ip = '$it'") }

        val result = DB.runAction {
            execute(
                if (args?.contains("-a") == true)
                    "DELETE FROM bans WHERE " +
                            "(ip = '${p["ip"]} AND ip != NULL)' OR " +
                            "(username = '${p["un"]} AND username != NULL)' OR " +
                            "(dc_uuid = '${p["dc"]} AND dc_uuid != NULL)' OR " +
                            "(mc_uuid = '${p["mc"]} AND mc_uuid != NULL)'"
                else
                    "DELETE FROM bans WHERE " +
                            "ip = '${p["ip"]}' AND " +
                            "username = '${p["un"]}' AND " +
                            "dc_uuid = '${p["dc"]}' AND " +
                            "mc_uuid = '${p["mc"]}'"
            )
            true
        } ?: false

        sender.sendMessage(if (result) "Ban executed" else "Failed to ban")
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