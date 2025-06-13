package com.gq97a6.firewall.managers

import org.bukkit.configuration.file.FileConfiguration

object StaticText {
    var onNotOnServer = "You are not on the Discord server."
    var onRequiredRoleMissing = "You do not have the required role on the Discord server."
    var onRelinkRequired = "Your account requires re-verification through the Discord server.\nSend a message to <botName> with <code> to link accounts."
    var onLinkRequired = "Your account requires verification through the Discord server.\nSend a message to <botName> with <code> to link accounts."
    var onError = "Server temporarily unavailable"

    var onBotNotOnServer = "❌ You are not a member of the server."
    var onBotRequiredRoleMissing = "❌ You do not have the required role."
    var onBotLinkSuccessful = "✅ Your Discord account has been linked with "
    var onBotRelinkSuccessful = "✅ Connection renewed with "
    var onBotInvalidCodeSupplied = "❌ No such code found."
    var onBotPlayerBanned = "❌ Your access to the server is restricted."
    var onBotCodeNotFound = "❌ No such code found."
    var onBotMinecraftAlreadyLinked = "❌ This Minecraft account is already linked to another Discord account."
    var onBotAccountCountPerDiscord = "❌ Your Discord account is already linked to the maximum number of Minecraft accounts."
    var onBotAccountCountPerIP = "❌ Your IP address is associated with the maximum number of Minecraft accounts."
    var onBotError = "❌ Failed to link accounts."

    fun initialize(config: FileConfiguration) {
        config.getString("text.onConnect.whenNotOnServer")?.let {
            onNotOnServer = it
        }

        config.getString("text.onConnect.whenRequiredRoleMissing")?.let {
            onRequiredRoleMissing = it
        }

        config.getString("text.onConnect.whenRelinkRequired")?.let {
            onRelinkRequired = it
        }

        config.getString("text.onConnect.whenLinkRequired")?.let {
            onLinkRequired = it
        }

        config.getString("text.onConnect.onError")?.let {
            onError = it
        }

        config.getString("text.onBotMessage.whenNotOnServer")?.let {
            onBotNotOnServer = it
        }

        config.getString("text.onBotMessage.whenRequiredRoleMissing")?.let {
            onBotRequiredRoleMissing = it
        }

        config.getString("text.onBotMessage.whenLinkSuccessful")?.let {
            onBotLinkSuccessful = it
        }

        config.getString("text.onBotMessage.whenRelinkSuccessful")?.let {
            onBotRelinkSuccessful = it
        }

        config.getString("text.onBotMessage.whenInvalidCodeSupplied")?.let {
            onBotInvalidCodeSupplied = it
        }

        config.getString("text.onBotMessage.whenPlayerBanned")?.let {
            onBotPlayerBanned = it
        }

        config.getString("text.onBotMessage.whenCodeNotFound")?.let {
            onBotCodeNotFound = it
        }

        config.getString("text.onBotMessage.whenMinecraftAlreadyLinked")?.let {
            onBotMinecraftAlreadyLinked = it
        }

        config.getString("text.onBotMessage.whenAccountCountPerDiscord")?.let {
            onBotAccountCountPerDiscord = it
        }

        config.getString("text.onBotMessage.wheAccountCountPerIP")?.let {
            onBotAccountCountPerIP = it
        }

        config.getString("text.onBotMessage.onError")?.let {
            onBotError = it
        }
    }
}