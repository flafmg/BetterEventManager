package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType

class DropClearCommand(private val messagesConfig: ConfigManager) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.dropclear")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        for (world in Bukkit.getWorlds()) {
            for (entity in world.entities) {
                if (entity.type == EntityType.DROPPED_ITEM) {
                    entity.remove()
                }
            }
        }

        sendMessage(sender, messagesConfig.getString("messages.dropclear.execution"))
        if (!args.contains("-s")) {
            broadcastToPlayers(messagesConfig.getString("messages.dropclear.announce"), getOnlinePlayers())
        }

        genericLog(sender, "/dropclear ${args.joinToString(" ")}", messagesConfig)
        return true
    }
}