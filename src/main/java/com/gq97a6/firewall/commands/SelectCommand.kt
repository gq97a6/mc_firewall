package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.executeQuery
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link
import com.gq97a6.firewall.classes.Printable.Companion.print
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SelectCommand : FirewallCommand("select") {
    override val help = Help(
        "cblf",
        listOf("dc_uuid", "mc_uuid", "username", "ip", "code"),
        listOf(),
        "select from database"
    )

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean {

        var codes: MutableList<Code>? = null
        var links: MutableList<Link>? = null
        var bans: MutableList<Link>? = null

        DB.runAction {
            //Get code
            if (args.f('c')) codes =
                executeQuery("SELECT * FROM codes WHERE " +
                        "${args.v("ip")?.let { "ip = '$it'" } ?: "false"} OR " +
                        "${args.v("username")?.let { "username = '$it'" } ?: "false"} OR " +
                        "${args.v("code")?.let { "code = '$it'" } ?: "false"} OR " +
                        "${args.v("mc_uuid")?.let { "mc_uuid = '$it'" } ?: "false"} " +
                        "LIMIT 5")
                    ?.let {
                        mutableListOf<Code>().apply {
                            while (it.next()) {
                                add(
                                    Code(
                                        it.getString("ip"),
                                        it.getString("username"),
                                        it.getString("mc_uuid"),
                                        it.getString("code"),
                                        it.getInt("id")
                                    )
                                )
                            }
                        }
                    }

            //Get links
            if (args.f('l')) links =
                executeQuery(
                    "SELECT * FROM links WHERE " +
                            "${args.v("ip")?.let { "ip = '$it'" } ?: "false"} OR " +
                            "${args.v("username")?.let { "username = '$it'" } ?: "false"} OR " +
                            "${args.v("dc_uuid")?.let { "mc_uuid = '$it'" } ?: "false"} OR " +
                            "${args.v("mc_uuid")?.let { "mc_uuid = '$it'" } ?: "false"} " +
                            "LIMIT 5"
                )?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid"),
                                    it.getInt("id")
                                )
                            )
                        }
                    }
                }


            //Get bans
            if (args.f('b')) bans =
                executeQuery(
                    "SELECT * FROM bans WHERE " +
                            "${args.v("ip")?.let { "ip = '$it'" } ?: "false"} OR " +
                            "${args.v("username")?.let { "username = '$it'" } ?: "false"} OR " +
                            "${args.v("dc_uuid")?.let { "mc_uuid = '$it'" } ?: "false"} OR " +
                            "${args.v("mc_uuid")?.let { "mc_uuid = '$it'" } ?: "false"} " +
                            "LIMIT 5"
                )?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid"),
                                    it.getInt("id")
                                )
                            )
                        }
                    }
                }
        }

        if (links?.isEmpty() == true && bans?.isEmpty() == true && codes?.isEmpty() == true) {
            sender.sendMessage("Found none")
            return true
        }

        listOf(links, codes, bans).forEach {
            it.print(sender, args.f('f'), !args.f('f'))
        }

        return true
    }
}

