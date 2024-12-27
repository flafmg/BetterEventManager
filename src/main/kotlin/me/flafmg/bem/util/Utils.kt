package me.flafmg.bem.util

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventType
import org.bukkit.command.Command
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun sendEventBlockedMessages(player: Player, eventType: EventType, messagesConfig: ConfigManager) {
    val eventTypeName = eventType.name.lowercase()
    val message = messagesConfig.getString("messages.$eventTypeName.actionBlockedMessage")
    val actionBarMessage = messagesConfig.getString("messages.$eventTypeName.actionBlockedActionBar")

    if (!message.isNullOrEmpty()) {
        sendMessage(player, message)
    }
    if (!actionBarMessage.isNullOrEmpty()) {
        sendActionBar(player, actionBarMessage)
    }
}

fun logCommandExecution(sender: CommandSender, command: String, args: Array<String>, messagesConfig: ConfigManager) {
    val logMessage = messagesConfig.getString("messages.system.genericLog.generic")
    val placeholders = mutableMapOf("player" to sender.name, "command" to "/$command ${args.joinToString(" ")}")
    broadcastToPlayers(logMessage, getOnlinePlayers().filter { it.hasPermission("bem.command.log") }, placeholders)
}

fun toggleEventLog(sender: CommandSender, eventType: EventType, action: String, messagesConfig: ConfigManager) {
    val logMessage = when (action) {
        "enabled" -> messagesConfig.getString("messages.system.genericLog.enabled")
        "disabled" -> messagesConfig.getString("messages.system.genericLog.disabled")
        else -> return
    }
    val placeholders = mutableMapOf("player" to sender.name, "event" to eventType.name.lowercase())
    broadcastToPlayers(logMessage, getOnlinePlayers().filter { it.hasPermission("bem.command.log") }, placeholders)
}

fun targetEventLog(sender: CommandSender, target: Player, eventType: EventType, action: String, messagesConfig: ConfigManager) {
    val logMessage = when (action) {
        "add" -> messagesConfig.getString("messages.system.genericLog.added")
        "remove" -> messagesConfig.getString("messages.system.genericLog.removed")
        else -> return
    }
    val placeholders = mutableMapOf("player" to sender.name, "targets" to target.name, "event" to eventType.name.lowercase())
    broadcastToPlayers(logMessage, getOnlinePlayers().filter { it.hasPermission("bem.command.log") }, placeholders)
}

fun effectLog(sender: CommandSender, target: Player, effect: String, action: String, messagesConfig: ConfigManager) {
    val logMessage = when (action) {
        "add" -> messagesConfig.getString("messages.system.genericLog.effectAdded")
        "remove" -> messagesConfig.getString("messages.system.genericLog.effectRemoved")
        else -> return
    }
    val placeholders = mutableMapOf("player" to sender.name, "targets" to target.name, "effect" to effect)
    broadcastToPlayers(logMessage, getOnlinePlayers().filter { it.hasPermission("bem.command.log") }, placeholders)
}

fun genericLog(sender: CommandSender, command: String, messagesConfig: ConfigManager) {
    val logMessage = messagesConfig.getString("messages.system.genericLog.generic")
    val placeholders = mutableMapOf("player" to sender.name, "command" to command)
    broadcastToPlayers(logMessage, getOnlinePlayers().filter { it.hasPermission("bem.command.log") }, placeholders)
}