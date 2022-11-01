package com.gq97a6.firewall

import github.scarsz.discordsrv.DiscordSRV
import com.gq97a6.firewall.listeners.AuthListener
import com.gq97a6.firewall.listeners.CommandsListener
import com.gq97a6.firewall.listeners.DiscordListener
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Firewall : JavaPlugin(), Listener {

    private val discordSrvListener = DiscordListener()

    companion object {
        lateinit var databaseURL: String
        lateinit var plugin: Plugin
    }

    override fun onEnable() {
        plugin = this
        databaseURL = "jdbc:h2:${dataFolder.absolutePath}/database"

        DB.initialize()

        server.pluginManager.registerEvents(AuthListener(), this)
        DiscordSRV.api.subscribe(discordSrvListener)
    }

    override fun onDisable() {
        DiscordSRV.api.unsubscribe(discordSrvListener)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?) =
        CommandsListener.onCommand(sender, command, label, args)
}