package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.Firewall.Companion.plugin
import github.scarsz.discordsrv.dependencies.jda.api.events.guild.GuildUnavailableEvent
import github.scarsz.discordsrv.dependencies.jda.api.hooks.ListenerAdapter
import org.jetbrains.annotations.NotNull

class JDAListener : ListenerAdapter() {
    override fun onGuildUnavailable(@NotNull event: GuildUnavailableEvent) {
        plugin.logger.severe("Oh no " + event.guild.name + " went unavailable :(")
    }
}