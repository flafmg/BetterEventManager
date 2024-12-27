package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RemoveAllBypassCommand(messagesConfig: ConfigManager) : BaseCommand("removeallbypass", messagesConfig) {

    init {
        baseCommand.withSubcommand(
            CommandAPICommand("event")
                .withArguments(eventTypeArgument("eventType"))
                .executes(CommandExecutor { sender, args ->
                    handleRemoveAllBypassEvent(sender, args)
                })
        )
        baseCommand.withSubcommand(
            CommandAPICommand("player")
                .withArguments(EntitySelectorArgument.ManyPlayers("targets"))
                .executes(CommandExecutor { sender, args ->
                    handleRemoveAllBypassPlayer(sender, args)
                })
        )
        baseCommand.executes(CommandExecutor { sender, args ->
            handleRemoveAllBypassAll(sender, args)
        })
    }

    private fun handleRemoveAllBypassEvent(sender: CommandSender, args: CommandArguments) {
        val eventType = args.get("eventType") as EventType
        BypassManager.getPlayers(eventType).forEach { playerId ->
            BypassManager.removePlayer(eventType, playerId)
        }
        sendMessage(sender, messagesConfig.getString("messages.removeallbypass.event.execution"), mutableMapOf("eventType" to eventType.name))
    }

    private fun handleRemoveAllBypassPlayer(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        if (targets.isEmpty()) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }

        targets.forEach { target ->
            BypassManager.removeAllPermissions(target.uniqueId)
        }

        val targetsS = targets.joinToString(",") { it.name }
        sendMessage(sender, messagesConfig.getString("messages.removeallbypass.player.execution"), mutableMapOf("targets" to targetsS))
    }

    private fun handleRemoveAllBypassAll(sender: CommandSender, args: CommandArguments) {
        BypassManager.clearAll()
        sendMessage(sender, messagesConfig.getString("messages.removeallbypass.all.execution"))
    }
}