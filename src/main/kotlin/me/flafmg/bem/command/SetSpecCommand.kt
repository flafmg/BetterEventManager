package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.SpectatorManager
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class SetSpecCommand(private val messagesConfig: ConfigManager) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.setspec")) {
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
        if(SpectatorManager.isSpectator(target.uniqueId)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyAdded"), mutableMapOf("target" to target.name))
            return true;
        }
        SpectatorManager.addPlayer(target.uniqueId)
        sendMessage(sender, messagesConfig.getString("messages.setspec.execution"), mutableMapOf("target" to target.name))
        sendMessage(target, messagesConfig.getString("messages.setspec.targetMessage"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.setspec.announce"), getOnlinePlayers(), mutableMapOf("target" to target.name))
        }

        genericLog(sender, "/setspec ${args.joinToString(" ")}", messagesConfig)
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/setspec <player> [-s]"))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            return Bukkit.getOnlinePlayers().filter { !SpectatorManager.isSpectator(it.uniqueId) }.map { it.name }
        }
        return emptyList()
    }
}