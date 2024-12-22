package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class SmiteCommand(private val messagesConfig: ConfigManager) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.smite")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        val target = Bukkit.getPlayer(args[0])
        if (target == null) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return true
        }

        target.world.strikeLightningEffect(target.location)

        sendMessage(sender, messagesConfig.getString("messages.smite.execution"), mutableMapOf("target" to target.name))
        sendMessage(target, messagesConfig.getString("messages.smite.targetMessage"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.smite.announce"), getOnlinePlayers(), mutableMapOf("target" to target.name))
        }

        genericLog(sender, "/smite ${args.joinToString(" ")}", messagesConfig)
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/smite <player> [-s]"))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            return Bukkit.getOnlinePlayers().map { it.name }
        }
        return emptyList()
    }
}