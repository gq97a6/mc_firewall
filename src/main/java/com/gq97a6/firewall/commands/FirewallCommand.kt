package com.gq97a6.firewall.commands

import com.gq97a6.firewall.*
import com.gq97a6.firewall.classes.Printable
import net.kyori.adventure.audience.Audience
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

sealed class FirewallCommand(val name: String) {
    open val help: Help = Help()

    abstract fun onCommand(sender: CommandSender, command: Command, label: String, args: Arguments): Boolean

    fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ): MutableList<String> {
        val index = (args?.size ?: 0) - 2

        return if (help.parameterless) mutableListOf()
        else if (index < help.nones.size) mutableListOf(help.nones[index].let { "<$it>" })
        else mutableListOf<String>()
            .apply { help.values.forEach { add("--$it") } }
            .apply { help.flags.forEach { add("-$it") } }
            .apply { removeAll(args?.toList() ?: listOf()) }
    }

    inner class Help(
        val flags: List<String> = listOf(),
        val values: List<String> = listOf(),
        val nones: List<String> = listOf(),
        private val explanation: String = ""
    ) : Printable {

        var parameterless = flags.size + values.size + nones.size == 0

        override fun printPlayer(audience: Audience, vararg args: Boolean) {
            if (args[0] && !parameterless) {
                audience.send {
                    add(name.capt()) { b().c(255, 174, 0) }
                    nones.forEach { add(" <$it>") { c(74, 140, 255) } }
                    values.forEach { add(" --$it") { c(255, 164, 54) } }
                    flags.forEach { add(" -$it") { c(255, 107, 38) } }
                }
            } else if (!args[0]) {
                audience.send {
                    add(name.capt()) { b().c(255, 174, 0) }
                    add(": $explanation") { c(74, 140, 255) }
                }
            }
        }

        override fun printConsole(audience: Audience, vararg args: Boolean) {
            if (args[0] && !parameterless) {
                audience.send {
                    add(name.capt())
                    nones.forEach { add(" <$it>") }
                    values.forEach { add(" --$it") }
                    flags.forEach { add(" -$it") }
                }
            } else if (!args[0]) {
                audience.send {
                    add(name.capt())
                    add(": $explanation")
                }
            }
        }
    }

    class Arguments(args: Array<out String>?) {
        val flags = mutableListOf<Char>()
        val values = mutableMapOf<String, String>()
        val none = mutableListOf<String>()

        fun f(f: Char) = flags.contains(f)
        fun v(v: String): String? = values[v]
        fun n(i: Int): String = none[i]

        init {
            args?.let { a ->
                var i = 0

                while (i < a.size) {
                    if (a[i].startsWith("--") && i < a.lastIndex) {
                        values[a[i].substring(2)] = a[i + 1]
                        i += 1
                    } else if (a[i].startsWith("-")) {
                        a[i].substring(1).forEach { flags.add(it) }
                    } else none.add(a[i])

                    i += 1
                }

                none.removeFirstOrNull()
            }
        }
    }
}