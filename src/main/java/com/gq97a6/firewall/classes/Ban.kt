package com.gq97a6.firewall.classes

import com.gq97a6.firewall.*
import net.kyori.adventure.audience.Audience

data class Ban(val ip: String?, val username: String?, val dcUUID: String?, val mcUUID: String?, val id: Int? = null) :
    Printable {
    override fun printPlayer(audience: Audience, vararg args: Boolean) {
        audience.send {
            add("BAN") { c(252, 73, 3).b() }

            add(" WHOIS") { b().c(0, 127, 212).se("/fw whois $dcUUID") }
            add(" PARDON") {
                b().c(0, 127, 212).se("/fw pardon --username $username --dc_uuid $dcUUID --ip $ip --mc_uuid $mcUUID")
            }

            add("===============") { c(252, 73, 3) }

            add("\nID: ") { b().c(255, 174, 0).se((id ?: -1).toString()) }
            add((id ?: -1).toString())

            add("\nIP: ") { b().c(255, 174, 0).se("$ip") }
            add("$ip")

            add("\nUSERNAME: ") { b().c(255, 174, 0).se("$username") }
            add("$username")

            add("\nDC_UUID: ") { b().c(255, 174, 0).se("$dcUUID") }
            add("$dcUUID")

            add("\nMC_UUID: ") { b().c(255, 174, 0).se("$mcUUID") }
            add("$mcUUID")
        }
    }

    override fun printPlayerCompact(audience: Audience, vararg args: Boolean) {
        audience.send {
            add("B ") { c(252, 73, 3).b() }

            add(" ID") { se((id ?: -1).toString()).he((id ?: -1).toString()) }
            add(" IP") { se("$ip").he("$ip") }
            add(" UN") { se("$username").he("$username") }
            add(" DC") { se("$dcUUID").he("$dcUUID") }
            add(" MC ") { se("$mcUUID").he("$mcUUID") }

            add("\t")

            add("WHOIS") { c(255, 174, 0).b().se("/fw whois $dcUUID") }
            add(" PARDON") {
                c(255, 174, 0).b().se("/fw pardon --username $username --dc_uuid $dcUUID --ip $ip --mc_uuid $mcUUID")
            }

            add("\n--------------------------------------------------")
        }
    }

    override fun printConsole(audience: Audience, vararg args: Boolean) {
        audience.send {
            add("BAN  ========================")

            if (args[0]) {
                add("\n\t@WHOIS: fw whois $dcUUID")
                add("\n\t@PARDON: fw pardon --username $username --dc_uuid $dcUUID --ip $ip --mc_uuid $mcUUID")
            }

            add("\n\tID: ${id ?: -1}")
            add("\n\tIP: $ip")
            add("\n\tUSERNAME: $username")
            add("\n\tDC_UUID: $dcUUID")
            add("\n\tMC_UUID: $mcUUID")
        }
    }
}