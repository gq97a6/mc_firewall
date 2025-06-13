package com.gq97a6.firewall.other

import com.gq97a6.firewall.*
import net.kyori.adventure.audience.Audience

data class Code(
    val ip: String,
    val username: String,
    val mcUUID: String,
    val code: String,
    val id: Int? = null
) : Printable {
    override fun printPlayer(audience: Audience)  = printConsole(audience)
    override fun printConsole(audience: Audience) {
        audience.send {
            add("CODE  ========================")
            add("\n\tID: ${id ?: -1}")
            add("\n\tIP: $ip")
            add("\n\tUSERNAME: $username")
            add("\n\tCODE: $code")
            add("\n\tMC_UUID: $mcUUID")
        }
    }
}