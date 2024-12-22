package me.flafmg.bem.command

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

abstract class BaseCommand(
    protected val eventType: EventType,
    protected val messagesConfig: ConfigManager,
    protected val hasTarget: Boolean = false,
    protected val isToggleable: Boolean = true
) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.${eventType.name.lowercase()}")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        when (args[0].lowercase()) {
            "on" -> if (isToggleable) handleOn(sender, args)
            "off" -> if (isToggleable) handleOff(sender, args)
            "add" -> if (hasTarget) handleAdd(sender, args)
            "remove" -> if (hasTarget) handleRemove(sender, args)
            else -> sendUsage(sender)
        }

        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/${eventType.name.lowercase()} <on|off|add|remove> [player] [-s]"))
    }

    private fun handleOn(sender: CommandSender, args: Array<String>) {
        if (EventManager.isEventEnabled(eventType)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyEnabled"))
            return
        }
        EventManager.enableEvent(eventType)
        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.enabled.execution"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.enabled.announce"), getOnlinePlayers())
        }
        toggleEventLog(sender, eventType, "enabled", messagesConfig)
    }

    private fun handleOff(sender: CommandSender, args: Array<String>) {
        if (!EventManager.isEventEnabled(eventType)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyDisabled"))
            return
        }
        EventManager.disableEvent(eventType)
        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.disabled.execution"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.disabled.announce"), getOnlinePlayers())
        }
        toggleEventLog(sender, eventType, "disabled", messagesConfig)
    }

    private fun handleAdd(sender: CommandSender, args: Array<String>) {
        if (args.size < 2) {
            sendMessage(sender, messagesConfig.getString("messages.system.targetNotSpecified"))
            return
        }

        val target = Bukkit.getPlayer(args[1])
        if (target == null) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }

        if (BypassManager.hasBypass(eventType, target.uniqueId)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyAdded"))
            return
        }

        BypassManager.addPlayer(eventType, target.uniqueId)
        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.added.execution"), mutableMapOf("target" to target.name))
        sendMessage(target, messagesConfig.getString("messages.${eventType.name.lowercase()}.added.targetMessage"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.added.announce"), getOnlinePlayers(), mutableMapOf("target" to target.name))
        }
        targetEventLog(sender, target, eventType, "add", messagesConfig)

    }

    private fun handleRemove(sender: CommandSender, args: Array<String>) {
        if (args.size < 2) {
            sendMessage(sender, messagesConfig.getString("messages.system.targetNotSpecified"))
            return
        }

        val target = Bukkit.getPlayer(args[1])
        if (target == null) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }

        if (!BypassManager.hasBypass(eventType, target.uniqueId)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyRemoved"))
            return
        }

        BypassManager.removePlayer(eventType, target.uniqueId)
        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.removed.execution"), mutableMapOf("target" to target.name))
        sendMessage(target, messagesConfig.getString("messages.${eventType.name.lowercase()}.removed.targetMessage"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.removed.announce"), getOnlinePlayers(), mutableMapOf("target" to target.name))
        }
        targetEventLog(sender, target, eventType, "remove", messagesConfig)

    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            return listOf("on", "off", "add", "remove").filter { it.startsWith(args[0], true) }
        } else if (args.size == 2 && hasTarget) {
            when (args[0].lowercase()) {
                "add" -> return Bukkit.getOnlinePlayers().filter { !BypassManager.hasBypass(eventType, it.uniqueId) }.map { it.name }
                "remove" -> return BypassManager.getPlayers(eventType).mapNotNull { Bukkit.getPlayer(it)?.name }
            }
        }
        return emptyList()
    }
}

fun CommandBuilder(eventType: EventType, messagesConfig: ConfigManager, hasTarget: Boolean = false, isToggleable: Boolean = true): BaseCommand {
    return object : BaseCommand(eventType, messagesConfig, hasTarget, isToggleable) {}
}