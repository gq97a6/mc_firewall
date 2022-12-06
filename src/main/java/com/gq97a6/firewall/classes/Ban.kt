package com.gq97a6.firewall.classes

import com.gq97a6.firewall.b
import com.gq97a6.firewall.c
import com.gq97a6.firewall.he
import com.gq97a6.firewall.se
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

data class Ban(val ip: String?, val username: String?, val dcUUID: String?, val mcUUID: String?, val id: Int? = null) :
    Printable {
    override fun printPlayer(sender: CommandSender) {
        sender.sendMessage(Component.text().apply { c ->
            c.append(Component.text("BAN").c(252, 73, 3).b())

            c.append(Component.text(" WHOIS").b().c(0, 127, 212).se("/fw whois $dcUUID"))
            c.append(
                Component.text(" PARDON").b().c(0, 127, 212)
                    .se("/fw pardon --username $username --dc_uuid $dcUUID --ip $ip --mc_uuid $mcUUID")
            )

            c.append(Component.text("===============").c(252, 73, 3))

            c.append(Component.text("\nID: ").b().c(255, 174, 0).se((id ?: -1).toString()))
            c.append(Component.text(id ?: -1))

            c.append(Component.text("\nIP: ").b().c(255, 174, 0).se("$ip"))
            c.append(Component.text("$ip"))

            c.append(Component.text("\nUSERNAME: ").b().c(255, 174, 0).se("$username"))
            c.append(Component.text("$username"))

            c.append(Component.text("\nDC_UUID: ").b().c(255, 174, 0).se("$dcUUID"))
            c.append(Component.text("$dcUUID"))

            c.append(Component.text("\nMC_UUID: ").b().c(255, 174, 0).se("$mcUUID"))
            c.append(Component.text("$mcUUID"))
        })
    }

    override fun printPlayerCompact(sender: CommandSender) {
        sender.sendMessage(Component.text().apply { c ->
            c.append(Component.text("B ").c(252, 73, 3).b())

            c.append(Component.text(" ID").se((id ?: -1).toString()).he((id ?: -1).toString()))
            c.append(Component.text(" IP").se("$ip").he("$ip"))
            c.append(Component.text(" UN").se("$username").he("$username"))
            c.append(Component.text(" DC").se("$dcUUID").he("$dcUUID"))
            c.append(Component.text(" MC ").se("$mcUUID").he("$mcUUID"))

            c.append(Component.text("\t"))

            c.append(Component.text("WHOIS").c(255, 174, 0).b().se("/fw whois $dcUUID"))
            c.append(
                Component.text("PARDON").c(255, 174, 0).b()
                    .se("/fw pardon --username $username --dc_uuid $dcUUID --ip $ip --mc_uuid $mcUUID")
            )

            c.append(Component.text("\n--------------------------------------------------"))
        })
    }

    override fun printConsole(sender: CommandSender, commands: Boolean) {
        sender.sendMessage(Component.text().apply { c ->
            c.append(Component.text("BAN  ========================"))

            if (commands) {
                c.append(Component.text("\n\t@WHOIS: fw whois $dcUUID"))
                c.append(Component.text("\n\t@PARDON: fw pardon --username $username --dc_uuid $dcUUID --ip $ip --mc_uuid $mcUUID"))
            }

            c.append(Component.text("\n\tID: ${id ?: -1}"))
            c.append(Component.text("\n\tIP: $ip"))
            c.append(Component.text("\n\tUSERNAME: $username"))
            c.append(Component.text("\n\tDC_UUID: $dcUUID"))
            c.append(Component.text("\n\tMC_UUID: $mcUUID"))
        })
    }
}