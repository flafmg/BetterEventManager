package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.manager.MaxPlayersManager
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.genericLog
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender

class MaxPlayersCommand(messagesConfig: ConfigManager) : BaseCommand("maxplayers", messagesConfig) {

    init {
        baseCommand.withSubcommand(
            CommandAPICommand("on")
                .withArguments(StringArgument("silent").setOptional(true))
                .executes(CommandExecutor { sender, args ->
                    handleOn(sender, args)
                })
        )
        baseCommand.withSubcommand(
            CommandAPICommand("off")
                .withArguments(StringArgument("silent").setOptional(true))
                .executes(CommandExecutor { sender, args ->
                    handleOff(sender, args)
                })
        )
        baseCommand.withSubcommand(
            CommandAPICommand("get")
                .executes(CommandExecutor { sender, args ->
                    handleGet(sender, args)
                })
        )
        baseCommand.withSubcommand(
            CommandAPICommand("set")
                .withArguments(IntegerArgument("count"), StringArgument("silent").setOptional(true))
                .executes(CommandExecutor { sender, args ->
                    handleSet(sender, args)
                })
        )
    }

    private fun handleOn(sender: CommandSender, args: CommandArguments) {
        if (EventManager.isEventEnabled(EventType.MAXPLAYERS)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyEnabled"))
            return
        }
        EventManager.enableEvent(EventType.MAXPLAYERS)
        sendMessage(sender, messagesConfig.getString("messages.maxplayers.enabled.execution"))
        if (!super.hasSilent) {
            broadcastToPlayers(messagesConfig.getString("messages.maxplayers.enabled.announce"), getOnlinePlayers())
        }
        genericLog(sender, "maxplayers on", messagesConfig)
    }

    private fun handleOff(sender: CommandSender, args: CommandArguments) {
        if (!EventManager.isEventEnabled(EventType.MAXPLAYERS)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyDisabled"))
            return
        }
        EventManager.disableEvent(EventType.MAXPLAYERS)
        sendMessage(sender, messagesConfig.getString("messages.maxplayers.disabled.execution"))
        if (!super.hasSilent) {
            broadcastToPlayers(messagesConfig.getString("messages.maxplayers.disabled.announce"), getOnlinePlayers())
        }
        genericLog(sender, "maxplayers off", messagesConfig)
    }

    private fun handleGet(sender: CommandSender, args: CommandArguments) {
        sendMessage(sender, messagesConfig.getString("messages.maxplayers.get.execution"), mutableMapOf("count" to MaxPlayersManager.getMaxPlayers().toString()))
    }

    private fun handleSet(sender: CommandSender, args: CommandArguments) {
        val count = args.get("count") as Int
        MaxPlayersManager.setMaxPlayers(count)
        sendMessage(sender, messagesConfig.getString("messages.maxplayers.set.execution"), mutableMapOf("count" to count.toString()))
        if (!super.hasSilent) {
            broadcastToPlayers(messagesConfig.getString("messages.maxplayers.set.announce"), getOnlinePlayers(), mutableMapOf("count" to count.toString()))
        }
        genericLog(sender, "maxplayers set $count", messagesConfig)
    }
}