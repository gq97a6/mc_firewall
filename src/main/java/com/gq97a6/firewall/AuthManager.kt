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

    fun changeIp(code: Code) = DB.runAction {
        execute("UPDATE links SET ip = '${code.ip}' WHERE mc_uuid = '${code.mcUUID}'")
        execute("DELETE FROM codes WHERE code = '${code.code}'")
        true
    } ?: false
}