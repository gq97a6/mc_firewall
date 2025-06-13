package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.command.PluginCommandListener
import com.gq97a6.firewall.commands.*

object CommandsListener: PluginCommandListener() {
    fun initialize() {
        registerCommand(AllowCommand())
        registerCommand(BanCommand())
        registerCommand(DenyCommand())
        registerCommand(HelpCommand())
        registerCommand(LinkCommand())
        registerCommand(DisableCommand())
        registerCommand(PardonCommand())
        registerCommand(PurgeCommand())
        registerCommand(SelectCommand())
        registerCommand(EnableCommand())
        registerCommand(UnlinkCommand())
        registerCommand(WhoisCommand())
    }
}