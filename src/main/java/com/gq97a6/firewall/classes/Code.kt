package com.gq97a6.firewall.classes

import com.gq97a6.firewall.b
import com.gq97a6.firewall.c
import com.gq97a6.firewall.he
import com.gq97a6.firewall.se
import net.kyori.adventure.text.Component
import org.bukkit.command.CommandSender

data class Code(val ip: String, val username: String, val mcUUID: String, val code: String, val id: Int? = null) :
    Printable {

    override fun printPlayer(sender: CommandSender) {
        sender.sendMessage(Component.text().apply { c ->
            c.append(Component.text("CODE").c(252, 73, 3).b())
            c.append(Component.text(" DENY").c(0, 127, 212).b().se("/fw deny $code"))
            c.append(Component.space())
            c.append(Component.text(" ALLOW").c(0, 127, 212).b().se("/fw allow $code "))
            c.append(Component.text("===============").c(252, 73, 3))


            c.append(Component.text("\nID: ").b().c(255, 174, 0).se((id ?: -1).toString()))
            c.append(Component.text(id ?: -1))

            c.append(Component.text("\nIP: ").b().c(255, 174, 0).se(ip))
            c.append(Component.text(ip))

            c.append(Component.text("\nUSERNAME: ").b().c(255, 174, 0).se(username))
            c.append(Component.text(username))

            c.append(Component.text("\nCODE: ").b().c(255, 174, 0).se(code))
            c.append(Component.text(code))

            c.append(Component.text("\nMC_UUID: ").b().c(255, 174, 0).se(mcUUID))
            c.append(Component.text(mcUUID))
        })
    }

    override fun printPlayerCompact(sender: CommandSender) {
        sender.sendMessage(Component.text().apply { c ->
            c.append(Component.text("C ").c(252, 73, 3).b())

            c.append(Component.text(" ID").se((id ?: -1).toString()).he((id ?: -1).toString()))
            c.append(Component.text(" IP").se(ip).he(ip))
            c.append(Component.text(" UN").se(username).he(username))
            c.append(Component.text(" CO").se(code).he(code))
            c.append(Component.text(" MC ").se(mcUUID).he(mcUUID))

            c.append(Component.text("\t"))

            c.append(Component.text("DENY").c(255, 174, 0).b().se("/fw deny $code"))
            c.append(Component.space())
            c.append(Component.text("ALLOW").c(255, 174, 0).b().se("/fw allow $code "))

            c.append(Component.text("\n--------------------------------------------------"))
        })
    }

    override fun printConsole(sender: CommandSender, commands: Boolean) {
        sender.sendMessage(Component.text().apply { c ->
            c.append(Component.text("CODE  ========================"))

            if (commands) {
                c.append(Component.text("\n\t@DENY: fw deny $code"))
                c.append(Component.text("\n\t@ALLOW: fw allow $code "))
                c.append(Component.text("\n\t@ALLOW: fw allow $code "))
            }

            c.append(Component.text("\n\tID: ${id ?: -1}"))
            c.append(Component.text("\n\tIP: $ip"))
            c.append(Component.text("\n\tUSERNAME: $username"))
            c.append(Component.text("\n\tCODE: $code"))
            c.append(Component.text("\n\tMC_UUID: $mcUUID"))
        })
    }
}