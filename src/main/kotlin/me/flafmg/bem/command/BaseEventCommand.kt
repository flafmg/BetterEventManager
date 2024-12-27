package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

open class BaseEventCommand(
    protected val eventType: EventType,
    messagesConfig: ConfigManager,
    protected val hasTarget: Boolean = true,
    protected val isToggleable: Boolean = true,
    private val aliases: List<String> = emptyList()
) : BaseCommand(eventType.name.lowercase(), messagesConfig, aliases) {

    init {
        baseCommand.withPermission("bettereventmanager.command.${eventType.name.lowercase()}")

        if (isToggleable) {
            baseCommand.withSubcommand(
                CommandAPICommand("on")
                    .withArguments(BooleanArgument("silent").setOptional(true))
                    .executes(CommandExecutor { sender, args ->
                        handleOn(sender, args)
                    })
            )
            baseCommand.withSubcommand(
                CommandAPICommand("off")
                    .withArguments(BooleanArgument("silent").setOptional(true))
                    .executes(CommandExecutor { sender, args ->
                        handleOff(sender, args)
                    })
            )
        }

        if (hasTarget) {
            baseCommand.withSubcommand(
                CommandAPICommand("add")
                    .withArguments(EntitySelectorArgument.ManyPlayers("targets"), BooleanArgument("silent").setOptional(true))
                    .executes(CommandExecutor { sender, args ->
                        handleAdd(sender, args)
                    })
            )

            baseCommand.withSubcommand(
                CommandAPICommand("remove")
                    .withArguments(EntitySelectorArgument.ManyPlayers("targets"), BooleanArgument("silent").setOptional(true))
                    .executes(CommandExecutor { sender, args ->
                        handleRemove(sender, args)
                    })
            )
        }
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/${eventType.name.lowercase()} <on|off|add|remove> [player] [-s]"))
    }

    private fun handleOn(sender: CommandSender, args: CommandArguments) {
        if (EventManager.isEventEnabled(eventType)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyEnabled"))
            return
        }
        EventManager.enableEvent(eventType)
        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.enabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.enabled.announce"), getOnlinePlayers())
        }
        toggleEventLog(sender, eventType, "enabled", messagesConfig)
    }

    private fun handleOff(sender: CommandSender, args: CommandArguments) {
        if (!EventManager.isEventEnabled(eventType)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyDisabled"))
            return
        }
        EventManager.disableEvent(eventType)
        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.disabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.disabled.announce"), getOnlinePlayers())
        }
        toggleEventLog(sender, eventType, "disabled", messagesConfig)
    }

    private fun handleAdd(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        if (targets.isEmpty()) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }
        val targetNames = targets.joinToString(", ") { it.name }

        targets.forEach { target ->
            if (BypassManager.hasBypass(eventType, target.uniqueId)) {
                sendMessage(sender, messagesConfig.getString("messages.system.alreadyAdded"))
                return
            }

            BypassManager.addPlayer(eventType, target.uniqueId)
        }

        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.added.execution"), mutableMapOf("targets" to targetNames))
        targets.forEach { target ->
            sendMessage(target, messagesConfig.getString("messages.${eventType.name.lowercase()}.added.targetMessage"))
        }
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.added.announce"), getOnlinePlayers(), mutableMapOf("targets" to targetNames))
        }
        targets.forEach { target ->
            targetEventLog(sender, target, eventType, "add", messagesConfig)
        }
    }

    private fun handleRemove(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        if (targets.isEmpty()) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }
        val targetNames = targets.joinToString(", ") { it.name }

        targets.forEach { target ->
            if (!BypassManager.hasBypass(eventType, target.uniqueId)) {
                sendMessage(sender, messagesConfig.getString("messages.system.alreadyRemoved"))
                return
            }

            BypassManager.removePlayer(eventType, target.uniqueId)
        }

        sendMessage(sender, messagesConfig.getString("messages.${eventType.name.lowercase()}.removed.execution"), mutableMapOf("targets" to targetNames))
        targets.forEach { target ->
            sendMessage(target, messagesConfig.getString("messages.${eventType.name.lowercase()}.removed.targetMessage"))
        }
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.${eventType.name.lowercase()}.removed.announce"), getOnlinePlayers(), mutableMapOf("targets" to targetNames))
        }
        targets.forEach { target ->
            targetEventLog(sender, target, eventType, "remove", messagesConfig)
        }
    }

    companion object {
        @JvmStatic
        fun buildCommand(
            eventType: EventType,
            messagesConfig: ConfigManager,
            hasTarget: Boolean = true,
            isToggleable: Boolean = true,
            aliases: List<String> = emptyList()
        ): BaseEventCommand {
            return BaseEventCommand(eventType, messagesConfig, hasTarget, isToggleable, aliases)
        }
    }
}