package com.gq97a6.firewall

import com.gq97a6.firewall.DB.execute
import com.gq97a6.firewall.DB.executeQuery
import com.gq97a6.firewall.Manager.CodeResolveResult.Reason.*
import com.gq97a6.firewall.Manager.changeIp
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link

object Manager {

    fun resolveCode(c: String, dcUUID: String): CodeResolveResult {

        if (c.length != 5) return CodeResolveResult(INVALID)

        var code: Code? = null
        var links: MutableList<Link>? = null
        var bans: MutableList<Link>? = null

        DB.runAction {
            //Get code
            code = executeQuery("SELECT * FROM codes WHERE code = '$c'")?.let {
                if (it.next()) {
                    Code(
                        it.getString("ip"),
                        it.getString("username"),
                        it.getString("mc_uuid"),
                        c,
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
                                    it.getString("mc_uuid")
                                )
                            )
                        }
                    }
                }

            //Get bans
            bans =
                executeQuery("SELECT * FROM bans WHERE dc_uuid = '$dcUUID' OR mc_uuid = '${code?.mcUUID ?: "null"}' OR ip = '${code?.ip ?: "null"}' OR username = '${code?.username ?: "null"}'")?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid")
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
        return if (code == null || bans?.isNotEmpty() == true) CodeResolveResult(NOT_FOUND)

        //This minecraft account and discord account are both not yet linked
        else if (!discordExists && !minecraftExists && code?.link(dcUUID) == true) CodeResolveResult(LINKED)

        //This discord account is associated with this code
        else if (bothMatch && code?.changeIp() == true) CodeResolveResult(RELINKED)

        //Fail
        else CodeResolveResult(FAILED)
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
        val link: Link? = null,
    ) {
        enum class Reason {
            INVALID, NOT_FOUND, FAILED, LINKED, RELINKED
        }
    }
}