package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AllCommand(private val messagesConfig: ConfigManager) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.all")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        when (args[0].lowercase()) {
            "on" -> handleAllOn(sender)
            "off" -> handleAllOff(sender)
            else -> sendUsage(sender)
        }
        genericLog(sender, "/all ${args.joinToString(" ")}", messagesConfig)
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/all <on|off>"))
    }

    private fun handleAllOn(sender: CommandSender) {
        EventType.entries.filter { it != EventType.HCD && it != EventType.HCK }.forEach {
            EventManager.enableEvent(it)
        }

        sendMessage(sender, messagesConfig.getString("messages.all.enabled.execution"))
        broadcastToPlayers(messagesConfig.getString("messages.all.enabled.announce"), getOnlinePlayers())
    }

    private fun handleAllOff(sender: CommandSender) {
        EventType.entries.filter { it != EventType.HCD && it != EventType.HCK && it != EventType.MAXPLAYERS }.forEach {
            EventManager.disableEvent(it)
        }

        sendMessage(sender, messagesConfig.getString("messages.all.disabled.execution"))
        broadcastToPlayers(messagesConfig.getString("messages.all.disabled.announce"), getOnlinePlayers())
    }

}