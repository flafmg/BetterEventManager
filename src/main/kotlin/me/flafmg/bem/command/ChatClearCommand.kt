package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.*
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ChatClearCommand(private val messagesConfig: ConfigManager) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.chatclear")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        for (player in getOnlinePlayers()) {
            for (i in 0..100) {
                player.sendMessage("")
            }
        }

        sendMessage(sender, messagesConfig.getString("messages.chatclear.execution"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.chatclear.announce"), getOnlinePlayers())
        }

        genericLog(sender, "/chatclear ${args.joinToString(" ")}", messagesConfig)
        return true
    }
}