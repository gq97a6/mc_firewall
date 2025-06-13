package com.gq97a6.firewall

import com.gq97a6.firewall.listeners.CommandsListener
import com.gq97a6.firewall.listeners.ConnectionsListener
import com.gq97a6.firewall.listeners.DiscordListener
import com.gq97a6.firewall.managers.DatabaseManager
import com.gq97a6.firewall.managers.StaticText
import github.scarsz.discordsrv.DiscordSRV
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.event.Listener
import org.bukkit.plugin.Plugin
import org.bukkit.plugin.java.JavaPlugin

class Firewall : JavaPlugin(), Listener {

    private val discordSrvListener = DiscordListener()

    companion object {
        var isFirewallOpen = false

        var dbURl = ""
        var dbUser = ""
        var dbPassword = ""

        var discordRequiredRoleID = ""
        var discordServerId = ""
        var botName = "Firewall Bot"

        var maxAccountCountPerIP = 1
        var maxAccountCountPerDiscord = 1

        lateinit var plugin: Plugin
    }

    override fun onEnable() {
        saveDefaultConfig()

        plugin = this

        dbURl = config.getString("database.url") ifNone "jdbc:h2:${dataFolder.absolutePath}/database"
        dbUser = config.getString("database.user") ifNone ""
        dbPassword = config.getString("database.password") ifNone ""

        discordRequiredRoleID = config.getString("discord.requiredRoleID") ifNone ""
        discordServerId = config.getString("discord.serverID") ifNone ""
        botName = config.getString("discord.botName") ifNone "Firewall Bot"

        maxAccountCountPerIP = config.getString("limits.maxAccountCountPerIP")?.toIntOrNull() ?: 1
        maxAccountCountPerDiscord = config.getString("limits.maxAccountCountPerDiscord")?.toIntOrNull() ?: 1

        StaticText.initialize(config)
        CommandsListener.initialize()
        DatabaseManager.initialize()

        server.pluginManager.registerEvents(ConnectionsListener(), this)
        DiscordSRV.api.subscribe(discordSrvListener)
    }

    override fun onDisable() {
        DiscordSRV.api.unsubscribe(discordSrvListener)
    }

    override fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean = CommandsListener.onCommand(sender, command, label, args.toList())

    override fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        args: Array<out String>
    ): List<String> = CommandsListener.onTabComplete(sender, command, alias, args)
}