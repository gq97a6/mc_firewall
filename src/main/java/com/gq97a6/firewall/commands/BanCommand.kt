package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.execute
import com.gq97a6.firewall.param
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class BanCommand : FirewallCommand("ban") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {

        val p = mutableMapOf<String, String>()
        args?.param("--dc_uuid")?.let { p.put("dc", it) }
        args?.param("--mc_uuid")?.let { p.put("mc", it) }
        args?.param("--username")?.let { p.put("un", it) }
        args?.param("--ip")?.let { p.put("ip", it) }

        return DB.runAction {
            execute(
                "INSERT INTO links (ip, username, dc_uuid, mc_uuid) VALUES (" +
                        "ip = '${p["ip"]}', " +
                        "username = '${p["un"]}', " +
                        "dc_uuid = '${p["dc"]}', " +
                        "mc_uuid = '${p["mc"]}'" +
                        ")"
            )

            sender.sendMessage("Ban executed")
            true
        } ?: false
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = mutableListOf("--dc_uuid", "--mc_uuid", "--username", "--ip")
        .apply { removeAll(args?.toList() ?: listOf()) }
}