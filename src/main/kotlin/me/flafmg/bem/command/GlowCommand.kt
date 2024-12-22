package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.GlowManager
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.command.TabCompleter
import org.bukkit.entity.Player

class GlowCommand(private val messagesConfig: ConfigManager) : CommandExecutor, TabCompleter {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.glow")) {
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

        if (args.size > 1) {
            if (args[1].equals("remove", ignoreCase = true) || args[1].equals("&r", ignoreCase = true)) {
                if(GlowManager.hasGlow(target)) {
                    GlowManager.removeGlow(target)
                    sendMessage(sender, messagesConfig.getString("messages.glow.removed"), mutableMapOf("target" to target.name))
                } else {
                    sendMessage(sender, messagesConfig.getString("messages.system.alreadyRemoved"), mutableMapOf("target" to target.name))
                    return true
                }
                sendMessage(sender, messagesConfig.getString("messages.glow.removed"), mutableMapOf("target" to target.name))
            } else {
                val colorCode = args[1]
                val color = ChatColor.getByChar(colorCode.replace("&", ""))
                if (color != null) {
                    if(!GlowManager.hasGlow(target)) {
                        GlowManager.setGlow(target, color)
                        sendMessage(sender, messagesConfig.getString("messages.glow.removed"), mutableMapOf("target" to target.name))
                    } else {
                        sendMessage(sender, messagesConfig.getString("messages.system.alreadyAdded"), mutableMapOf("target" to target.name))
                        return true
                    }
                    sendMessage(sender, messagesConfig.getString("messages.glow.execution"), mutableMapOf("target" to target.name, "color" to color.name))
                } else {
                    sendMessage(sender, messagesConfig.getString("messages.system.invalidArgument"))
                }
            }
        } else {
            GlowManager.setGlow(target, null)
            sendMessage(sender, messagesConfig.getString("messages.glow.execution"), mutableMapOf("target" to target.name, "color" to "default"))
        }

        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.glow.announce"), getOnlinePlayers(), mutableMapOf("target" to target.name))
        }

        genericLog(sender, "/glow ${args.joinToString(" ")}", messagesConfig)
        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/glow <player> [color|remove] [-s]"))
    }

    override fun onTabComplete(sender: CommandSender, command: Command, alias: String, args: Array<String>): List<String> {
        if (args.size == 1) {
            return Bukkit.getOnlinePlayers().map { it.name }
        } else if (args.size == 2) {
            return ChatColor.values().map { "&${it.char}" } + "remove"
        }
        return emptyList()
    }
}