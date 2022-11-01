package com.gq97a6.firewall

import com.gq97a6.firewall.DB.execute
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link

object AuthManager {

    fun link(code: Code, uuid: String) = DB.runAction {
        execute("INSERT INTO links (ip, username, dc_uuid, mc_uuid) VALUES ('${code.ip}', '${code.username}', '$uuid', '${code.mcUUID}')")
        if (code.code.length == 5) execute("DELETE FROM codes WHERE code = '${code.code}'")
        true
    } ?: false

    fun unlink(link: Link) = DB.runAction {
        execute("DELETE FROM links WHERE mc_uuid = '${link.mcUUID}' AND dc_uuid = '${link.dcUUID}' AND ip = '${link.ip}'")
        true
    } ?: false

    fun unlinkTry(value: String, limit: String) = DB.runAction {
        execute("DELETE FROM links WHERE mc_uuid = '$value' OR dc_uuid = '$value' OR ip = '$value' LIMIT $limit")
        true
    } ?: false

    fun unlinkUsername(username: String) {

    }

    fun unlinkDiscordUUID(uuid: String) {

    }

    fun unlinkMinecraftUUID(uuid: String) {

    }

    fun changeIp(code: Code) {
        execute("UPDATE links SET ip = '${code.ip}' WHERE mc_uuid = '${code.mcUUID}'")
        execute("DELETE FROM codes WHERE code = '${code.code}'")
    }

    fun purge() = execute("DELETE FROM LINKS WHERE added < NOW() - INTERVAL 100 HOUR")
}