package com.gq97a6.firewall.commands

import com.gq97a6.firewall.command.LongOptionParam
import com.gq97a6.firewall.command.PluginCommand
import com.gq97a6.firewall.command.PluginCommandParams
import com.gq97a6.firewall.command.ShortFlagParam
import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.DatabaseManager.executeQuery
import com.gq97a6.firewall.other.Ban
import com.gq97a6.firewall.other.Code
import com.gq97a6.firewall.other.Link
import com.gq97a6.firewall.other.Printable.Companion.print
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class SelectCommandParams : PluginCommandParams {
    @LongOptionParam("dc", "uuid", "Player's Discord account UUID")
    var dcUuid = ""

    @LongOptionParam("mc", "uuid", "Player's Minecraft account UUID")
    var mcUuid = ""

    @LongOptionParam("username", "username", "Player's username")
    var username = ""

    @LongOptionParam("ip", "address", "Player's IP address")
    var ip = ""

    @LongOptionParam("code", "digits", "Player's code")
    var code = ""

    @ShortFlagParam('c', "do select codes")
    var doSelectCodes = false

    @ShortFlagParam('b', "do select bans")
    var doSelectBans = false

    @ShortFlagParam('l', "do select links")
    var doSelectLinks = false
}

class SelectCommand : PluginCommand<SelectCommandParams>() {
    override val name = "select"
    override val description: String = "fetch a record from the database"

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String, arguments: SelectCommandParams
    ): Boolean = arguments.withArguments {
        if (username.isEmpty() && ip.isEmpty() && mcUuid.isEmpty() && dcUuid.isEmpty()) {
            sender.sendMessage("No search parameters were provided")
            return false
        }

        val codes = mutableListOf<Code>()
        val links = mutableListOf<Link>()
        val bans = mutableListOf<Ban>()

        DatabaseManager.runAction {
            //Get code
            if (doSelectCodes) {
                val result = buildString {
                    append("SELECT * FROM codes WHERE ")
                    if (ip.isNotBlank()) append("ip = '$ip' OR ")
                    if (username.isNotBlank()) append("username = '$username' OR ")
                    if (code.isNotBlank()) append("code = '$code' OR ")
                    if (mcUuid.isNotBlank()) append("mc_uuid = '$mcUuid' OR ")
                    append("false ")
                    append("LIMIT 5")
                }.let { executeQuery(it) }

                result?.let {
                    while (it.next()) {
                        codes.add(
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
            if (doSelectLinks) {
                val result = buildString {
                    append("SELECT * FROM codes WHERE ")
                    if (ip.isNotBlank()) append("ip = '$ip' OR ")
                    if (username.isNotBlank()) append("username = '$username' OR ")
                    if (dcUuid.isNotBlank()) append("dc_uuid = '$dcUuid' OR ")
                    if (mcUuid.isNotBlank()) append("mc_uuid = '$mcUuid' OR ")
                    append("false ")
                    append("LIMIT 5")
                }.let { executeQuery(it) }

                result?.let {
                    while (it.next()) {
                        links.add(
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
            if (doSelectBans) {
                val result = buildString {
                    append("SELECT * FROM bans WHERE ")
                    if (ip.isNotBlank()) append("ip = '$ip' OR ")
                    if (username.isNotBlank()) append("username = '$username' OR ")
                    if (dcUuid.isNotBlank()) append("dc_uuid = '$dcUuid' OR ")
                    if (mcUuid.isNotBlank()) append("mc_uuid = '$mcUuid' OR ")
                    append("false ")
                    append("LIMIT 5")
                }.let { executeQuery(it) }

                result?.let {
                    while (it.next()) {
                        bans.add(
                            Ban(
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

        if (links.isEmpty() && bans.isEmpty() && codes.isEmpty()) {
            sender.sendMessage("Found none")
            return true
        }

        listOf(links, codes, bans).forEach {
            it.print(sender)
        }

        return true
    }
}

