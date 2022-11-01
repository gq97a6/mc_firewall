package com.gq97a6.firewall.listeners

import com.gq97a6.firewall.AuthManager
import com.gq97a6.firewall.classes.Code
import com.gq97a6.firewall.classes.Link
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

object CommandsListener {
    fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<out String>?): Boolean {
        if (args.isNullOrEmpty()) return true
        return when (args[0]) {
            //"accept" -> onAcceptCommand(sender, command, label, args)
            "unlink?" -> onUnlinkTryCommand(sender, command, label, args)
            "unlink" -> onUnlinkCommand(sender, command, label, args)
            "link" -> onLinkCommand(sender, command, label, args)
            "mcban" -> onMcbanCommand(sender, command, label, args)
            "dcban" -> onDcbanCommand(sender, command, label, args)
            "purge" -> onPurgeCommand(sender, command, label, args)
            else -> false
        }
    }

    private fun onMcbanCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        sender.sendMessage("Minecraft UUID banned")
        return true
    }

    private fun onPurgeCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        AuthManager.purge()
        sender.sendMessage("Database purged")
        return true
    }

    private fun onDcbanCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        sender.sendMessage("Discord UUID banned")
        return true
    }

    private fun onLinkCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        return if (args.size == 5 && AuthManager.link(Code(args[2], args[1], args[4], ""), args[3])) {
            sender.sendMessage("Link created")
            true
        } else {
            sender.sendMessage("Unlink failed")
            false
        }
    }

    private fun onUnlinkCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        return if (args.size == 5 && AuthManager.unlink(Link(args[2], args[1], args[4], args[3]))) {
            sender.sendMessage("Link created")
            true
        } else {
            sender.sendMessage("Unlink failed")
            false
        }
    }

    private fun onUnlinkTryCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        return if (args.size == 3 && AuthManager.unlinkTry(args[1], args[2])) {
            sender.sendMessage("Unlink successful")
            true
        } else {
            sender.sendMessage("Unlink failed")
            false
        }
    }

    /*
    private fun onAcceptCommand(
        sender: CommandSender,
        command: Command,
        label: String,
        args: Array<out String>
    ): Boolean {
        return if (args.size == 3 && AuthManager.unlinkTry(args[1], args[2])) {
            val code = DB.runAction {
                //Get code from database
                return@runAction executeQuery("SELECT * FROM CODES WHERE code = '${args[1]}'")?.let {
                    if (it.next()) {
                        Code(
                            it.getString("ip"),
                            it.getString("username"),
                            it.getString("mc_uuid"),
                            args[1],
                        )
                    } else null
                }
            }
            if (code != null) {

            }
            sender.sendMessage("Code accepted")
            true
        } else {
            sender.sendMessage("Accept failed")
            false
        }
    }
    */
}