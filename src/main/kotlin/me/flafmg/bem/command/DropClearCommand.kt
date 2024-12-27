package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.genericLog
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.sendMessage
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.EntityType

class DropClearCommand(messagesConfig: ConfigManager) : BaseCommand("dropclear", messagesConfig) {

    init {
        baseCommand.executes(CommandExecutor { sender, args ->
            handleDropClear(sender, args)
        })
    }

    private fun handleDropClear(sender: CommandSender, args: CommandArguments) {
        if (!sender.hasPermission("bettereventmanager.command.dropclear")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return
        }

        for (world in Bukkit.getWorlds()) {
            for (entity in world.entities) {
                if (entity.type == EntityType.DROPPED_ITEM) {
                    entity.remove()
                }
            }
        }

        sendMessage(sender, messagesConfig.getString("messages.dropclear.execution"))
        if (!super.hasSilent) {
            broadcastToPlayers(messagesConfig.getString("messages.dropclear.announce"), getOnlinePlayers())
        }

        genericLog(sender, "/dropclear ${args.rawArgs().joinToString(" ")}", messagesConfig)
    }
}