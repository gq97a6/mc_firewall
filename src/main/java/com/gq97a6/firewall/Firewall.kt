package com.gq97a6.firewall

import com.gq97a6.firewall.listeners.ConnectionsListener
import com.gq97a6.firewall.listeners.CommandsListener
import com.gq97a6.firewall.listeners.DiscordListener
import github.scarsz.discordsrv.DiscordSRV
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Firewall : JavaPlugin(), Listener {

    private val discordSrvListener = DiscordListener()

    companion object {
        var isOpen = false

        lateinit var dbURl: String
        lateinit var dbUser: String
        lateinit var dbPassword: String
        lateinit var botName: String

        lateinit var plugin: Plugin
    }

    override fun onEnable() {
        saveDefaultConfig()

        plugin = this

        dbURl = config.getString("database.url") ifNone "jdbc:h2:${dataFolder.absolutePath}/database"
        dbUser = config.getString("database.user") ifNone ""
        dbPassword = config.getString("database.password") ifNone ""
        botName = config.getString("botname") ifNone "Firewall Bot"

        DB.initialize()

        server.pluginManager.registerEvents(ConnectionsListener(), this)
        DiscordSRV.api.subscribe(discordSrvListener)
    }

    override fun onDisable() {
        DiscordSRV.api.unsubscribe(discordSrvListener)
    }

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?) =
        CommandsListener.onCommand(sender, command, label, args)

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>?
    ) = CommandsListener.onTabComplete(sender, command, alias, args)
}