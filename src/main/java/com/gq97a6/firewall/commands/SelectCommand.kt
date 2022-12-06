package com.gq97a6.firewall.commands

import com.gq97a6.firewall.DB
import com.gq97a6.firewall.DB.executeQuery
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link
import com.gq97a6.firewall.param
import net.kyori.adventure.text.Component
import net.kyori.adventure.text.event.ClickEvent
import net.kyori.adventure.text.event.HoverEvent
import net.kyori.adventure.text.format.TextColor
import net.kyori.adventure.text.format.TextDecoration
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.command.ConsoleCommandSender

class SelectCommand : FirewallCommand("select") {
    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        val sql = mutableMapOf<String, String>()

        args?.param("--dc_uuid")?.let { sql.put("dc", "dc_uuid = '$it'") }
        args?.param("--mc_uuid")?.let { sql.put("mc", "mc_uuid = '$it'") }
        args?.param("--username")?.let { sql.put("un", "username = '$it'") }
        args?.param("--ip")?.let { sql.put("ip", "ip = '$it'") }
        args?.param("--code")?.let { sql.put("co", "code = '$it'") }

        var codes: MutableList<Code>? = null
        var links: MutableList<Link>? = null
        var bans: MutableList<Link>? = null

        DB.runAction {
            //Get code
            if (args?.get(1)?.contains('c') == true) codes =
                executeQuery("SELECT * FROM codes WHERE ${sql["ip"] ?: "false"} OR ${sql["un"] ?: "false"} OR ${sql["co"] ?: "false"} OR ${sql["mc"] ?: "false"} LIMIT 5")?.let {
                    mutableListOf<Code>().apply {
                        while (it.next()) {
                            add(
                                Code(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("mc_uuid"),
                                    it.getString("code"),
                                    it.getInt("id")
                                )
                            )
                        }
                    }
                }

            //Get links
            if (args?.get(1)?.contains('l') == true) links =
                executeQuery("SELECT * FROM links WHERE ${sql["ip"] ?: "false"} OR ${sql["un"] ?: "false"} OR ${sql["dc"] ?: "false"} OR ${sql["mc"] ?: "false"} LIMIT 5")?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid"),
                                    it.getInt("id")
                                )
                            )
                        }
                    }
                }


            //Get bans
            if (args?.get(1)?.contains('b') == true) bans =
                executeQuery("SELECT * FROM bans WHERE ${sql["ip"] ?: "false"} OR ${sql["un"] ?: "false"} OR ${sql["dc"] ?: "false"} OR ${sql["mc"] ?: "false"} LIMIT 5")?.let {
                    mutableListOf<Link>().apply {
                        while (it.next()) {
                            add(
                                Link(
                                    it.getString("ip"),
                                    it.getString("username"),
                                    it.getString("dc_uuid"),
                                    it.getString("mc_uuid"),
                                    it.getInt("id")
                                )
                            )
                        }
                    }
                }
        }

        if (links?.isEmpty() == true && bans?.isEmpty() == true && codes?.isEmpty() == true) {
            sender.sendMessage("Found none")
            return true
        }

        //==============================================================================================================
        // Response for console
        //==============================================================================================================

        fun respondConsole(commands: Boolean) {
            links?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(Component.text("LINK  ========================"))

                    if (commands) {
                        c.append(Component.text("\n\t@WHOIS: fw whois ${it.dcUUID}"))
                        c.append(Component.text("\n\t@UNLINK: fw unlink ${it.id}"))
                    }

                    c.append(Component.text("\n\tID: ${it.id ?: -1}"))
                    c.append(Component.text("\n\tIP: ${it.ip}"))
                    c.append(Component.text("\n\tUSERNAME: ${it.username}"))
                    c.append(Component.text("\n\tDC_UUID: ${it.dcUUID}"))
                    c.append(Component.text("\n\tMC_UUID: ${it.mcUUID}"))
                })
            }

            codes?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(Component.text("CODE  ========================"))

                    if (commands) {
                        c.append(Component.text("\n\t@DENY: fw deny ${it.code}"))
                        c.append(Component.text("\n\t@ALLOW: fw unlink ${it.id}"))
                    }

                    c.append(Component.text("\n\tID: ${it.id ?: -1}"))
                    c.append(Component.text("\n\tIP: ${it.ip}"))
                    c.append(Component.text("\n\tUSERNAME: ${it.username}"))
                    c.append(Component.text("\n\tCODE: ${it.code}"))
                    c.append(Component.text("\n\tMC_UUID: ${it.mcUUID}"))
                })
            }

            bans?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(Component.text("BAN  ========================"))

                    if (commands) {
                        c.append(Component.text("\n\t@WHOIS: fw whois ${it.dcUUID}"))
                        c.append(Component.text("\n\t@PARDON: fw pardon ${it.id}"))
                    }

                    c.append(Component.text("\n\tID: ${it.id ?: -1}"))
                    c.append(Component.text("\n\tIP: ${it.ip}"))
                    c.append(Component.text("\n\tUSERNAME: ${it.username}"))
                    c.append(Component.text("\n\tDC_UUID: ${it.dcUUID}"))
                    c.append(Component.text("\n\tMC_UUID: ${it.mcUUID}"))
                })
            }
        }

        //==============================================================================================================
        // Response for player
        //==============================================================================================================

        fun respondPlayer() {
            links?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(Component.text("L ").color(TextColor.color(252, 73, 3)).decorate(TextDecoration.BOLD))

                    c.append(
                        Component.text(" ID")
                            .clickEvent(ClickEvent.suggestCommand((it.id ?: -1).toString()))
                            .hoverEvent(HoverEvent.showText(Component.text(it.id ?: -1)))
                    )
                    c.append(
                        Component.text(" IP")
                            .clickEvent(ClickEvent.suggestCommand(it.ip))
                            .hoverEvent(HoverEvent.showText(Component.text(it.ip)))
                    )
                    c.append(
                        Component.text(" UN")
                            .clickEvent(ClickEvent.suggestCommand(it.username))
                            .hoverEvent(HoverEvent.showText(Component.text(it.username)))
                    )
                    c.append(
                        Component.text(" DC")
                            .clickEvent(ClickEvent.suggestCommand(it.dcUUID))
                            .hoverEvent(HoverEvent.showText(Component.text(it.dcUUID)))
                    )
                    c.append(
                        Component.text(" MC ")
                            .clickEvent(ClickEvent.suggestCommand(it.mcUUID))
                            .hoverEvent(HoverEvent.showText(Component.text(it.mcUUID)))
                    )

                    c.append(Component.text("  "))

                    c.append(
                        Component.text("WHOIS")
                            .color(TextColor.color(255, 174, 0))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw whois ${it.dcUUID}"))
                    )
                    c.append(
                        Component.text(" UNLINK")
                            .color(TextColor.color(255, 174, 0))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw unlink ${it.id}"))
                    )

                    c.append(Component.text("\n--------------------------------------------------"))
                })
            }

            codes?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(Component.text("C ").color(TextColor.color(252, 73, 3)).decorate(TextDecoration.BOLD))

                    c.append(
                        Component.text(" ID")
                            .clickEvent(ClickEvent.suggestCommand((it.id ?: -1).toString()))
                            .hoverEvent(HoverEvent.showText(Component.text(it.id ?: -1)))
                    )
                    c.append(
                        Component.text(" IP")
                            .clickEvent(ClickEvent.suggestCommand(it.ip))
                            .hoverEvent(HoverEvent.showText(Component.text(it.ip)))
                    )
                    c.append(
                        Component.text(" UN")
                            .clickEvent(ClickEvent.suggestCommand(it.username))
                            .hoverEvent(HoverEvent.showText(Component.text(it.username)))
                    )
                    c.append(
                        Component.text(" CO")
                            .clickEvent(ClickEvent.suggestCommand(it.code))
                            .hoverEvent(HoverEvent.showText(Component.text(it.code)))
                    )
                    c.append(
                        Component.text(" MC ")
                            .clickEvent(ClickEvent.suggestCommand(it.mcUUID))
                            .hoverEvent(HoverEvent.showText(Component.text(it.mcUUID)))
                    )

                    c.append(Component.text("  "))

                    c.append(
                        Component.text("DENY")
                            .color(TextColor.color(255, 174, 0))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw deny ${it.code}"))
                    )
                    c.append(Component.space())
                    c.append(
                        Component.text("ALLOW")
                            .color(TextColor.color(255, 174, 0))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw link ${it.username}  ${it.ip}  ${it.mcUUID} "))
                    )

                    c.append(Component.text("\n--------------------------------------------------"))
                })
            }

            bans?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(Component.text("B ").color(TextColor.color(252, 73, 3)).decorate(TextDecoration.BOLD))

                    c.append(
                        Component.text(" ID")
                            .clickEvent(ClickEvent.suggestCommand((it.id ?: -1).toString()))
                            .hoverEvent(HoverEvent.showText(Component.text(it.id ?: -1)))
                    )
                    c.append(
                        Component.text(" IP")
                            .clickEvent(ClickEvent.suggestCommand(it.ip))
                            .hoverEvent(HoverEvent.showText(Component.text(it.ip)))
                    )
                    c.append(
                        Component.text(" UN")
                            .clickEvent(ClickEvent.suggestCommand(it.username))
                            .hoverEvent(HoverEvent.showText(Component.text(it.username)))
                    )
                    c.append(
                        Component.text(" DC")
                            .clickEvent(ClickEvent.suggestCommand(it.dcUUID))
                            .hoverEvent(HoverEvent.showText(Component.text(it.dcUUID)))
                    )
                    c.append(
                        Component.text(" MC ")
                            .clickEvent(ClickEvent.suggestCommand(it.mcUUID))
                            .hoverEvent(HoverEvent.showText(Component.text(it.mcUUID)))
                    )

                    c.append(Component.text("  "))

                    c.append(
                        Component.text("WHOIS")
                            .color(TextColor.color(255, 174, 0))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw whois ${it.dcUUID}"))
                    )
                    c.append(
                        Component.text("PARDON")
                            .color(TextColor.color(255, 174, 0))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw pardon ${it.id}"))
                    )

                    c.append(Component.text("\n--------------------------------------------------"))
                })
            }
        }

        //==============================================================================================================
        // Full response for player
        //==============================================================================================================

        fun respondPlayerFull() {
            links?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(
                        Component.text("LINK")
                            .color(TextColor.color(252, 73, 3)).decorate(TextDecoration.BOLD)
                    )
                    c.append(
                        Component.text(" WHOIS")
                            .color(TextColor.color(0, 127, 212))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw whois ${it.dcUUID}"))
                    )
                    c.append(
                        Component.text(" UNLINK")
                            .color(TextColor.color(0, 127, 212))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw unlink ${it.id}"))
                    )
                    c.append(Component.text("===============").color(TextColor.color(252, 73, 3)))

                    c.append(
                        Component.text("\nID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand((it.id ?: -1).toString()))
                    )
                    c.append(Component.text(it.id ?: -1))

                    c.append(
                        Component.text("\nIP: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.ip))
                    )
                    c.append(Component.text(it.ip))

                    c.append(
                        Component.text("\nUSERNAME: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.username))
                    )
                    c.append(Component.text(it.username))

                    c.append(
                        Component.text("\nDC_UUID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.dcUUID))
                    )
                    c.append(Component.text(it.dcUUID))

                    c.append(
                        Component.text("\nMC_UUID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.mcUUID))
                    )
                    c.append(Component.text(it.mcUUID))
                })
            }

            codes?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(
                        Component.text("CODE")
                            .color(TextColor.color(252, 73, 3)).decorate(TextDecoration.BOLD)
                    )
                    c.append(
                        Component.text(" DENY")
                            .color(TextColor.color(0, 127, 212))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw deny ${it.code}"))
                    )
                    c.append(Component.space())
                    c.append(
                        Component.text(" ALLOW")
                            .color(TextColor.color(0, 127, 212))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw link ${it.username} ${it.ip} ${it.mcUUID} "))
                    )
                    c.append(Component.text("===============").color(TextColor.color(252, 73, 3)))


                    c.append(
                        Component.text("\nID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand((it.id ?: -1).toString()))
                    )
                    c.append(Component.text(it.id ?: -1))

                    c.append(
                        Component.text("\nIP: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.ip))
                    )
                    c.append(Component.text(it.ip))

                    c.append(
                        Component.text("\nUSERNAME: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.username))
                    )
                    c.append(Component.text(it.username))

                    c.append(
                        Component.text("\nCODE: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.code))
                    )
                    c.append(Component.text(it.code))

                    c.append(
                        Component.text("\nMC_UUID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.mcUUID))
                    )
                    c.append(Component.text(it.mcUUID))
                })
            }

            bans?.forEach {
                sender.sendMessage(Component.text().apply { c ->
                    c.append(
                        Component.text("BAN")
                            .color(TextColor.color(252, 73, 3)).decorate(TextDecoration.BOLD)
                    )
                    c.append(
                        Component.text(" WHOIS")
                            .color(TextColor.color(0, 127, 212))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw whois ${it.dcUUID}"))
                    )
                    c.append(
                        Component.text(" PARDON")
                            .color(TextColor.color(0, 127, 212))
                            .decorate(TextDecoration.BOLD)
                            .clickEvent(ClickEvent.suggestCommand("/fw pardon ${it.id}"))
                    )
                    c.append(Component.text("===============").color(TextColor.color(252, 73, 3)))

                    c.append(
                        Component.text("\nID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand((it.id ?: -1).toString()))
                    )
                    c.append(Component.text(it.id ?: -1))

                    c.append(
                        Component.text("\nIP: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.ip))
                    )
                    c.append(Component.text(it.ip))

                    c.append(
                        Component.text("\nUSERNAME: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.username))
                    )
                    c.append(Component.text(it.username))

                    c.append(
                        Component.text("\nDC_UUID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.dcUUID))
                    )
                    c.append(Component.text(it.dcUUID))

                    c.append(
                        Component.text("\nMC_UUID: ").decorate(TextDecoration.BOLD).color(TextColor.color(255, 174, 0))
                            .clickEvent(ClickEvent.suggestCommand(it.mcUUID))
                    )
                    c.append(Component.text(it.mcUUID))
                })
            }
        }

        //==============================================================================================================
        //==============================================================================================================

        if (sender is ConsoleCommandSender) respondConsole(args?.get(1)?.contains('c') == true)
        else if (args?.get(1)?.contains('f') == true) respondPlayerFull()
        else respondPlayer()

        return true
    }

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = if (args?.size == 2) mutableListOf("-cblf")
    else mutableListOf("--dc_uuid", "--mc_uuid", "--username", "--ip", "--code")
        .apply { removeAll(args?.toList() ?: listOf()) }
}

