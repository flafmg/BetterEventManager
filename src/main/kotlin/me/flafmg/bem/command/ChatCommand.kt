package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class ChatCommand(
    messagesConfig: ConfigManager,
    private val mainConfig: ConfigManager
) : BaseCommand(EventType.CHAT, messagesConfig, false, false) {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.chat")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        when (args[0].lowercase()) {
            "specs" -> handleSpecsCommand(sender, args)
            "on", "off", "add", "remove" -> super.onCommand(sender, command, label, args)
            else -> sendUsage(sender)
        }

        return true
    }

    private fun handleSpecsCommand(sender: CommandSender, args: Array<String>) {
        if (args.size < 2) {
            sendUsage(sender)
            return
        }

        when (args[1].lowercase()) {
            "on" -> {
                EventManager.enableEvent(EventType.SPECCHAT)
                sendMessage(sender, messagesConfig.getString("messages.chat.specs.on"))
            }
            "off" -> {
                EventManager.disableEvent(EventType.SPECCHAT)
                sendMessage(sender, messagesConfig.getString("messages.chat.specs.off"))
            }
            "bypass" -> {
                if (args.size < 3) {
                    sendUsage(sender)
                    return
                }
                when (args[2].lowercase()) {
                    "on" -> {
                        EventManager.enableEvent(EventType.SPECCHATBYPASS)
                        sendMessage(sender, messagesConfig.getString("messages.chat.specs.bypassOn"))
                    }
                    "off" -> {
                        EventManager.disableEvent(EventType.SPECCHATBYPASS)
                        sendMessage(sender, messagesConfig.getString("messages.chat.specs.bypassOff"))
                    }
                    else -> sendUsage(sender)
                }
            }
            "private" -> {
                EventManager.enableEvent(EventType.SPECCHATPUBLIC)
                sendMessage(sender, messagesConfig.getString("messages.chat.specs.private"))
            }
            "public" -> {
                EventManager.disableEvent(EventType.SPECCHATPUBLIC)
                sendMessage(sender, messagesConfig.getString("messages.chat.specs.public"))
            }
            else -> sendUsage(sender)
        }
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/chat specs <on|off|bypass on|bypass off|private|public>"))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            return listOf("specs", "on", "off", "add", "remove").filter { it.startsWith(args[0], true) }
        }
        if (args.size == 2 && args[0].equals("specs", true)) {
            return listOf("on", "off", "bypass", "private", "public").filter { it.startsWith(args[1], true) }
        }
        if (args.size == 3 && args[0].equals("specs", true) && args[1].equals("bypass", true)) {
            return listOf("on", "off").filter { it.startsWith(args[2], true) }
        }
        return emptyList()
    }
}