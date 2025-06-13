package com.gq97a6.firewall.managers

import com.gq97a6.firewall.managers.DatabaseManager.execute
import com.gq97a6.firewall.managers.DatabaseManager.executeQuery
import com.gq97a6.firewall.managers.LinkManager.CodeResolveResult.Reason.*
import com.gq97a6.firewall.other.Ban
import com.gq97a6.firewall.other.Code
import com.gq97a6.firewall.other.Link

object LinkManager {

    fun resolveCode(
        code: String,
        dcUUID: String,
        ignoreDC: Boolean = false,
        ignoreMC: Boolean = false
    ): CodeResolveResult {

        if (code.length != 5) return CodeResolveResult(INVALID)

        var codeParsed: Code? = null
        var links: MutableList<Link>? = null
        var bans: MutableList<Ban>? = null

        DatabaseManager.runAction {
            //Get code
            codeParsed = executeQuery("SELECT * FROM codes WHERE code = '$code'")?.let {
                if (it.next()) {
                    Code(
                        it.getString("ip"),
                        it.getString("username"),
                        it.getString("mc_uuid"),
                        code,
                        it.getString("id").toInt()
                    )
                } else null
            }

            //Get links
            links =
                executeQuery("SELECT * FROM links WHERE dc_uuid = '$dcUUID' OR mc_uuid = '${codeParsed?.mcUUID ?: ""}'")?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid"),
                                    it.getString("id").toInt()
                                )
                            )
                        }
                    }
                }

            //Get bans
            bans =
                executeQuery("SELECT * FROM bans WHERE dc_uuid = '$dcUUID' OR mc_uuid = '${codeParsed?.mcUUID ?: ""}' OR ip = '${codeParsed?.ip ?: ""}' OR username = '${codeParsed?.username ?: ""}'")?.let {
                    mutableListOf<Ban>().apply {
                        while (it.next()) {
                            add(
                                Ban(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid"),
                                    it.getString("id").toInt()
                                )
                            )
                        }
                    }
                }
        }

        val discordExists = links?.find { it.dcUUID == dcUUID } != null
        val minecraftExists = links?.find { it.mcUUID == codeParsed?.mcUUID } != null
        val bothMatch = links?.find { it.mcUUID == codeParsed?.mcUUID && it.dcUUID == dcUUID } != null

        //Code not found
        return if (codeParsed == null) CodeResolveResult(NOT_FOUND)

        //Banned
        else if (bans?.isNotEmpty() == true) CodeResolveResult(BANNED, codeParsed, links, bans)

        //This minecraft account and discord account are both not yet linked
        else if ((!discordExists || ignoreDC) && (!minecraftExists || ignoreMC) && codeParsed?.link(dcUUID) == true)
            CodeResolveResult(LINKED, codeParsed)

        //This discord account is associated with this code
        else if (bothMatch && codeParsed?.changeIp() == true) CodeResolveResult(RELINKED, codeParsed)

        //Fail
        else CodeResolveResult(FAILED, codeParsed, links, bans)
    }

    fun Code.link(uuid: String) = DatabaseManager.runAction {
        execute("INSERT INTO links (ip, username, dc_uuid, mc_uuid) VALUES ('${this@link.ip}', '${this@link.username}', '$uuid', '${this@link.mcUUID}')")
        if (this@link.code.length == 5) execute("DELETE FROM codes WHERE code = '${this@link.code}'")
        true
    } ?: false

    fun unlink(code: Code, uuid: String) = DatabaseManager.runAction {
        execute("INSERT INTO links (ip, username, dc_uuid, mc_uuid) VALUES ('${code.ip}', '${code.username}', '$uuid', '${code.mcUUID}')")
        if (code.code.length == 5) execute("DELETE FROM codes WHERE code = '${code.code}'")
        true
    } ?: false

    private fun Code.changeIp() = DatabaseManager.runAction {
        execute("UPDATE links SET ip = '${this@changeIp.ip}' WHERE mc_uuid = '${this@changeIp.mcUUID}'")
        execute("DELETE FROM codes WHERE code = '${this@changeIp.code}'")
        true
    } ?: false

    data class CodeResolveResult(
        val reason: Reason,
        val code: Code? = null,
        val links: List<Link>? = null,
        val bans: List<Ban>? = null
    ) {
        enum class Reason {
            INVALID, NOT_FOUND, FAILED, LINKED, RELINKED, BANNED
        }
    }
}