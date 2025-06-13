package com.gq97a6.firewall.other

import com.gq97a6.firewall.*
import net.kyori.adventure.audience.Audience

data class Ban(
    val ip: String?,
    val username: String?,
    val dcUUID: String?,
    val mcUUID: String?,
    val id: Int? = null
) : Printable {
    override fun printPlayer(audience: Audience) = printConsole(audience)
    override fun printConsole(audience: Audience) {
        audience.send {
            add("BAN  ========================")
            add("\n\tID: ${id ?: -1}")
            add("\n\tIP: $ip")
            add("\n\tUSERNAME: $username")
            add("\n\tDC_UUID: $dcUUID")
            add("\n\tMC_UUID: $mcUUID")
        }
    }
}