package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.manager.MaxPlayersManager
import me.flafmg.bem.util.sendMessage
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.genericLog
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter

class MaxPlayersCommand(private val messagesConfig: ConfigManager) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.maxplayers")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        val silent = args.contains("-s")
        when (args[0].lowercase()) {
            "on" -> {
                if(EventManager.isEventEnabled(EventType.MAXPLAYERS)) {
                    sendMessage(sender, messagesConfig.getString("messages.system.alreadyEnabled"))
                    return true
                }
                EventManager.enableEvent(EventType.MAXPLAYERS)
                sendMessage(sender, messagesConfig.getString("messages.maxplayers.enabled.execution"))
                if (!silent) {
                    broadcastToPlayers(messagesConfig.getString("messages.maxplayers.enabled.announce"), getOnlinePlayers())
                }
                genericLog(sender, "maxplayers on", messagesConfig)
            }
            "off" -> {
                if(!EventManager.isEventEnabled(EventType.MAXPLAYERS)) {
                    sendMessage(sender, messagesConfig.getString("messages.system.alreadyDisabled"))
                    return true
                }
                EventManager.disableEvent(EventType.MAXPLAYERS)
                sendMessage(sender, messagesConfig.getString("messages.maxplayers.disabled.execution"))
                if (!silent) {
                    broadcastToPlayers(messagesConfig.getString("messages.maxplayers.disabled.announce"), getOnlinePlayers())
                }
                genericLog(sender, "maxplayers off", messagesConfig)
            }
            "get" -> {
                sendMessage(sender, messagesConfig.getString("messages.maxplayers.get.execution"), mutableMapOf("count" to MaxPlayersManager.getMaxPlayers().toString()))
            }
            "set" -> {
                if (args.size < 2) {
                    sendUsage(sender)
                    return true
                }
                val count = args[1].toIntOrNull()
                if (count == null || count < 0) {
                    sendMessage(sender, messagesConfig.getString("messages.system.invalidArgument"))
                    return true
                }
                MaxPlayersManager.setMaxPlayers(count)
                sendMessage(sender, messagesConfig.getString("messages.maxplayers.set.execution"), mutableMapOf("count" to count.toString()))
                if (!silent) {
                    broadcastToPlayers(messagesConfig.getString("messages.maxplayers.set.announce"), getOnlinePlayers(), mutableMapOf("count" to count.toString()))
                }
                genericLog(sender, "maxplayers set $count", messagesConfig)
            }
            else -> sendUsage(sender)
        }
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/maxplayers <on|off|get|set> [count] [-s]"))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String>? {
        if (!sender.hasPermission("bettereventmanager.command.maxplayers")) {
            return emptyList()
        }

        return when (args.size) {
            1 -> listOf("on", "off", "get", "set").filter { it.startsWith(args[0], ignoreCase = true) }
            2 -> if (args[0].equals("set", ignoreCase = true)) listOf("<count>") else emptyList()
            else -> emptyList()
        }
    }
}