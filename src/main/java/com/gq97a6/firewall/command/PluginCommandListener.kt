package com.gq97a6.firewall.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.memberProperties

open class PluginCommandListener {
    val commands = mutableMapOf<String, PluginCommand<*>>()
    val paramClasses = mutableMapOf<String, KClass<out PluginCommandParams>>()
    val paramPatterns = mutableMapOf<String, PluginParamPattern>()

    inline fun <reified T : PluginCommandParams> registerCommand(command: PluginCommand<T>) {
        registerCommand(command, T::class)
    }

    fun registerCommand(command: PluginCommand<*>, paramClass: KClass<out PluginCommandParams>) {
        commands[command.name] = command
        paramClasses[command.name] = paramClass

        paramClass.memberProperties.filterIsInstance<KMutableProperty<*>>().let {
            paramPatterns[command.name] = PluginParamPattern(it)
        }
    }

    @Suppress("UNCHECKED_CAST")
    fun onCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        arguments: List<String>
    ): Boolean {
        val commandName = arguments.getOrNull(0) ?: "help"

        val commandExecuted = commands[commandName] ?: return false
        val paramClass = paramClasses[commandName] ?: return false
        val paramPattern = paramPatterns[commandName] ?: return false

        return try {
            val params = arguments.drop(1).parseArguments(paramPattern, paramClass)
            (commandExecuted as PluginCommand<PluginCommandParams>).onCommand(sender, command, label, params)
            true
        } catch (e: Exception) {
            println("Failed to execute command '$commandName': ${e.message}")
            false
        }
    }

    fun onTabComplete(
        sender: CommandSender,
        command: Command,
        alias: String,
        arguments: Array<out String>
    ): List<String> {
        if (arguments.size == 1) return commands.keys.toList()

        val commandName = arguments[0]
        val commandExecuted = commands[commandName] ?: return listOf()
        return commandExecuted.onTabComplete(sender, command, alias, arguments)
    }
}