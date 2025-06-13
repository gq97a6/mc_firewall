package com.gq97a6.firewall.command

import kotlin.reflect.KClass
import kotlin.reflect.KMutableProperty
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.findAnnotation

class PluginParamPattern(properties: List<KMutableProperty<*>>) {

    val requiredParams = properties.mapNotNull {
        Pair(it, it.findAnnotation<RequiredParam>() ?: return@mapNotNull null)
    }

    val shortOptions = properties.mapNotNull {
        Pair(it, it.findAnnotation<ShortOptionParam>() ?: return@mapNotNull null)
    }

    val longOptions = properties.mapNotNull {
        Pair(it, it.findAnnotation<LongOptionParam>() ?: return@mapNotNull null)
    }

    val shortFlags = properties.mapNotNull {
        Pair(it, it.findAnnotation<ShortFlagParam>() ?: return@mapNotNull null)
    }

    val longFlags = properties.mapNotNull {
        Pair(it, it.findAnnotation<LongFlagParam>() ?: return@mapNotNull null)
    }
}

//Run action per Parameter of a command
fun PluginParamPattern.perRequiredParam(action: (KMutableProperty<*>, RequiredParam) -> Unit) {
    this.requiredParams.forEach { action(it.first, it.second) }
}

//Run action per Long Option (eg. --username Notch) of a command
fun PluginParamPattern.perLongOption(action: (KMutableProperty<*>, LongOptionParam) -> Unit) {
    this.longOptions.forEach { action(it.first, it.second) }
}

//Run action per Short Option (eg. -u Notch) of a command
fun PluginParamPattern.perShortOption(action: (KMutableProperty<*>, ShortOptionParam) -> Unit) {
    this.shortOptions.forEach { action(it.first, it.second) }
}

//Run action per Long Flag (eg. --allow) of a command
fun PluginParamPattern.perLongFlag(action: (KMutableProperty<*>, LongFlagParam) -> Unit) {
    this.longFlags.forEach { action(it.first, it.second) }
}

//Run action per Short Flag (eg. -a) of a command
fun PluginParamPattern.perShortFlag(action: (KMutableProperty<*>, ShortFlagParam) -> Unit) {
    this.shortFlags.forEach { action(it.first, it.second) }
}

fun List<String>.parseArguments(
    paramPattern: PluginParamPattern,
    paramClass: KClass<out PluginCommandParams>
): PluginCommandParams {

    // Extract positional parameters first
    val args = this
    val positionalArgs = this.filter { !it.startsWith("-") }.toMutableList()
    val paramInstance = paramClass.createInstance()

    paramPattern.apply {
        perRequiredParam { prop, _ ->
            if (positionalArgs.isNotEmpty()) {
                prop.setter.call(paramInstance, positionalArgs.removeAt(0))
            }
        }

        perLongOption { prop, longOption ->
            val index = args.indexOf("--${longOption.tag}")
            if (index >= 0 && index + 1 < args.size) {
                prop.setter.call(paramInstance, args[index + 1])
            }
        }

        perShortOption { prop, shortOption ->
            for (i in args.indices) {
                val arg = args[i]
                if (arg.startsWith("-") && !arg.startsWith("--") && shortOption.tag in arg) {
                    // Check if value is attached (e.g., -uNotch)
                    val charIndex = arg.indexOf(shortOption.tag)
                    if (charIndex + 1 < arg.length) {
                        prop.setter.call(paramInstance, arg.substring(charIndex + 1))
                    }
                    // Check if value is next argument
                    else if (i + 1 < args.size && !args[i + 1].startsWith("-")) {
                        prop.setter.call(paramInstance, args[i + 1])
                    }
                    break
                }
            }
        }

        perLongFlag { prop, longFlag ->
            if (args.contains("--${longFlag.tag}")) {
                prop.setter.call(paramInstance, true)
            }
        }

        perShortFlag { prop, shortFlag ->
            if (args.any { it.startsWith("-") && !it.startsWith("--") && shortFlag.tag in it }) {
                prop.setter.call(paramInstance, true)
            }
        }
    }

    return paramInstance
}