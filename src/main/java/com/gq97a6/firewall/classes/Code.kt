package com.gq97a6.firewall.classes

import com.gq97a6.firewall.*
import net.kyori.adventure.audience.Audience

data class Code(val ip: String, val username: String, val mcUUID: String, val code: String, val id: Int? = null) :
    Printable {

    override fun printPlayer(audience: Audience, vararg args: Boolean) {
        audience.send {
            add("CODE") { c(252, 73, 3).b() }
            add(" DENY") { c(0, 127, 212).b().se("/fw deny $code") }
            add("")
            add(" ALLOW") { c(0, 127, 212).b().se("/fw allow $code ") }
            add("===============") { c(252, 73, 3) }


            add("\nID: ") { b().c(255, 174, 0).se((id ?: -1).toString()) }
            add((id ?: -1).toString())

            add("\nIP: ") { b().c(255, 174, 0).se(ip) }
            add(ip)

            add("\nUSERNAME: ") { b().c(255, 174, 0).se(username) }
            add(username)

            add("\nCODE: ") { b().c(255, 174, 0).se(code) }
            add(code)

            add("\nMC_UUID: ") { b().c(255, 174, 0).se(mcUUID) }
            add(mcUUID)
        }
    }

    override fun printPlayerCompact(audience: Audience, vararg args: Boolean) {
        audience.send {
            add("C ") { c(252, 73, 3).b() }

            add(" ID") { se((id ?: -1).toString()).he((id ?: -1).toString()) }
            add(" IP") { se(ip).he(ip) }
            add(" UN") { se(username).he(username) }
            add(" CO") { se(code).he(code) }
            add(" MC ") { se(mcUUID).he(mcUUID) }

            add("\t")

            add("DENY") { c(255, 174, 0).b().se("/fw deny $code") }
            add("")
            add("ALLOW") { c(255, 174, 0).b().se("/fw allow $code ") }

            add("\n--------------------------------------------------")
        }
    }

    override fun printConsole(audience: Audience, vararg args: Boolean) {
        audience.send {
            add("CODE  ========================")

            if (args[0]) {
                add("\n\t@DENY: fw deny $code")
                add("\n\t@ALLOW: fw allow $code ")
                add("\n\t@ALLOW: fw allow $code ")
            }

            add("\n\tID: ${id ?: -1}")
            add("\n\tIP: $ip")
            add("\n\tUSERNAME: $username")
            add("\n\tCODE: $code")
            add("\n\tMC_UUID: $mcUUID")
        }
    }
}