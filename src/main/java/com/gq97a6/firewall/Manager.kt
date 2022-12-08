package com.gq97a6.firewall

import com.gq97a6.firewall.DB.execute
import com.gq97a6.firewall.DB.executeQuery
import com.gq97a6.firewall.Manager.CodeResolveResult.Reason.*
import com.gq97a6.firewall.classes.Ban
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link

object Manager {

    fun resolveCode(
        c: String,
        dcUUID: String,
        ignoreDC: Boolean = false,
        ignoreMC: Boolean = false
    ): CodeResolveResult {

        if (c.length != 5) return CodeResolveResult(INVALID)

        var code: Code? = null
        var links: MutableList<Link>? = null
        var bans: MutableList<Ban>? = null

        DB.runAction {
            //Get code
            code = executeQuery("SELECT * FROM codes WHERE code = '$c'")?.let {
                if (it.next()) {
                    Code(
                        it.getString("ip"),
                        it.getString("username"),
                        it.getString("mc_uuid"),
                        c,
                        it.getString("id").toInt()
                    )
                } else null
            }

            //Get links
            links =
                executeQuery("SELECT * FROM links WHERE dc_uuid = '$dcUUID' OR mc_uuid = '${code?.mcUUID ?: ""}'")?.let {
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
                executeQuery("SELECT * FROM bans WHERE dc_uuid = '$dcUUID' OR mc_uuid = '${code?.mcUUID ?: ""}' OR ip = '${code?.ip ?: ""}' OR username = '${code?.username ?: ""}'")?.let {
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
        val minecraftExists = links?.find { it.mcUUID == code?.mcUUID } != null
        val bothMatch = links?.find { it.mcUUID == code?.mcUUID && it.dcUUID == dcUUID } != null

        //Code not found
        return if (code == null) CodeResolveResult(NOT_FOUND)

        //Banned
        else if (bans?.isNotEmpty() == true) CodeResolveResult(BANNED, code, links, bans)

        //This minecraft account and discord account are both not yet linked
        else if ((!discordExists || ignoreDC) && (!minecraftExists || ignoreMC) && code?.link(dcUUID) == true) CodeResolveResult(
            LINKED,
            code
        )

        //This discord account is associated with this code
        else if (bothMatch && code?.changeIp() == true) CodeResolveResult(RELINKED, code)

        //Fail
        else CodeResolveResult(FAILED, code, links, bans)
    }

    fun Code.link(uuid: String) = DB.runAction {
        execute("INSERT INTO links (ip, username, dc_uuid, mc_uuid) VALUES ('${this@link.ip}', '${this@link.username}', '$uuid', '${this@link.mcUUID}')")
        if (this@link.code.length == 5) execute("DELETE FROM codes WHERE code = '${this@link.code}'")
        true
    } ?: false

    fun unlink(code: Code, uuid: String) = DB.runAction {
        execute("INSERT INTO links (ip, username, dc_uuid, mc_uuid) VALUES ('${code.ip}', '${code.username}', '$uuid', '${code.mcUUID}')")
        if (code.code.length == 5) execute("DELETE FROM codes WHERE code = '${code.code}'")
        true
    } ?: false

    private fun Code.changeIp() = DB.runAction {
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