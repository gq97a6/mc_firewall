package com.gq97a6.firewall.command

import com.gq97a6.firewall.other.Printable
import com.gq97a6.firewall.add
import com.gq97a6.firewall.send
import net.kyori.adventure.audience.Audience

//Allow - allow player in by the code
//  <code>: Player's code
//  <dc_uuid>: Discord UUID of the player
//  -a: Override links limit per Discord account
//  -i: Override links limit per IP

//Pardon - lift every ban that matches all filters
//  --dc <uuid>: Discord UUID of the player
//  --mc <uuid>: Minecraft UUID of the player
//  --username <username>: Username of the player
//  --ip <address>: IP address of the player
fun PluginCommand<*>.buildHelp(paramPattern: PluginParamPattern): Printable {
    val command = this

    return object : Printable {
        override fun printPlayer(audience: Audience) {
            audience.send {
                add("${command.name} - ${command.description}")

                paramPattern.apply {
                    perRequiredParam { _, requiredParam ->
                        add("\n    <${requiredParam.placeholder}>: ${requiredParam.description}")
                    }

                    perLongOption { _, longOption ->
                        add("\n    --${longOption.tag} <${longOption.placeholder}>: ${longOption.description}")
                    }

                    perShortOption { _, shortOption ->
                        add("\n    -${shortOption.tag} <${shortOption.placeholder}>: ${shortOption.description}")
                    }

                    perLongFlag { _, longFlag ->
                        add("\n    --${longFlag.tag}: ${longFlag.description}")
                    }

                    perShortFlag { _, shortFlag ->
                        add("\n    -${shortFlag.tag}: ${shortFlag.description}")
                    }
                }
            }
        }

        override fun printConsole(audience: Audience) = printPlayer(audience)
    }
}