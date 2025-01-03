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
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class AllCommand(messagesConfig: ConfigManager) : BaseCommand("all", messagesConfig) {

    init {
        baseCommand.withSubcommand(
            CommandAPICommand("on")
                .executes(CommandExecutor { sender, args ->
                    handleAllOn(sender, args)
                })
        )
        baseCommand.withSubcommand(
            CommandAPICommand("off")
                .executes(CommandExecutor { sender, args ->
                    handleAllOff(sender, args)
                })
        )
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

    private fun handleAllOn(sender: CommandSender, args: CommandArguments) {
        EventType.entries.filter { it != EventType.HCD && it != EventType.HCK && it != EventType.MAXPLAYERS && it != EventType.SPECCHATBYPASS && it != EventType.SPECCHATPUBLIC && it != EventType.SPECCHAT }.forEach {
            EventManager.enableEvent(it)
        }

        sendMessage(sender, messagesConfig.getString("messages.all.enabled.execution"))

        if (!isSilent(args))
            broadcastToPlayers(messagesConfig.getString("messages.all.enabled.announce"), getOnlinePlayers())
    }

    private fun handleAllOff(sender: CommandSender, args: CommandArguments) {
        EventType.entries.filter { it != EventType.HCD && it != EventType.HCK && it != EventType.MAXPLAYERS && it != EventType.SPECCHATBYPASS && it != EventType.SPECCHATPUBLIC && it != EventType.SPECCHAT}.forEach {
            EventManager.disableEvent(it)
        }

        sendMessage(sender, messagesConfig.getString("messages.all.disabled.execution"))
        if (!isSilent(args))
            broadcastToPlayers(messagesConfig.getString("messages.all.disabled.announce"), getOnlinePlayers())
    }

    private fun handleAdd(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        val targetNames = targets.joinToString(", ") { it.name }

        targets.forEach { target ->
            EventType.entries.filter { it != EventType.HCD && it != EventType.HCK && it != EventType.MAXPLAYERS && it != EventType.SPECCHATBYPASS && it != EventType.SPECCHATPUBLIC && it != EventType.SPECCHAT }.forEach {
                if (!BypassManager.hasBypass(it, target.uniqueId)) {
                    BypassManager.addPlayer(it, target.uniqueId)
                }
            }
            sendMessage(target, messagesConfig.getString("messages.all.added.targetMessage"))
        }

        sendMessage(sender, messagesConfig.getString("messages.all.added.execution"), mutableMapOf("targets" to targetNames))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.all.added.announce"), getOnlinePlayers(), mutableMapOf("targets" to targetNames))
        }
    }

    private fun handleRemove(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        val targetNames = targets.joinToString(", ") { it.name }

        targets.forEach { target ->
            EventType.entries.filter { it != EventType.HCD && it != EventType.HCK && it != EventType.MAXPLAYERS && it != EventType.SPECCHATBYPASS && it != EventType.SPECCHATPUBLIC && it != EventType.SPECCHAT }.forEach {
                if (BypassManager.hasBypass(it, target.uniqueId)) {
                    BypassManager.removePlayer(it, target.uniqueId)
                }
            }
            sendMessage(target, messagesConfig.getString("messages.all.removed.targetMessage"))
        }

        sendMessage(sender, messagesConfig.getString("messages.all.removed.execution"), mutableMapOf("targets" to targetNames))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.all.removed.announce"), getOnlinePlayers(), mutableMapOf("targets" to targetNames))
        }
    }
}