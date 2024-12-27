package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.BooleanArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender

class ChatCommand(
    messagesConfig: ConfigManager,
    private val mainConfig: ConfigManager
) : BaseEventCommand(EventType.CHAT, messagesConfig, true, true) {

    init {
        baseCommand.withSubcommand(
            CommandAPICommand("specs")
                .withSubcommand(
                    CommandAPICommand("on")
                        .withArguments(BooleanArgument("silent").setOptional(true))
                        .executes(CommandExecutor { sender, args ->
                            handleSpecsOn(sender, args)
                        })
                )
                .withSubcommand(
                    CommandAPICommand("off")
                        .withArguments(BooleanArgument("silent").setOptional(true))
                        .executes(CommandExecutor { sender, args ->
                            handleSpecsOff(sender, args)
                        })
                )
                .withSubcommand(
                    CommandAPICommand("private")
                        .withArguments(BooleanArgument("silent").setOptional(true))
                        .executes(CommandExecutor { sender, args ->
                            handleSpecsPrivate(sender, args)
                        })
                )
                .withSubcommand(
                    CommandAPICommand("public")
                        .withArguments(BooleanArgument("silent").setOptional(true))
                        .executes(CommandExecutor { sender, args ->
                            handleSpecsPublic(sender, args)
                        })
                )
        )

        baseCommand.withSubcommand(
            CommandAPICommand("bypass")
                .withSubcommand(
                    CommandAPICommand("on")
                        .withArguments(BooleanArgument("silent").setOptional(true))
                        .executes(CommandExecutor { sender, args ->
                            handleBypassOn(sender, args)
                        })
                )
                .withSubcommand(
                    CommandAPICommand("off")
                        .withArguments(BooleanArgument("silent").setOptional(true))
                        .executes(CommandExecutor { sender, args ->
                            handleBypassOff(sender, args)
                        })
                )
        )
    }

    private fun handleSpecsOn(sender: CommandSender, args: CommandArguments) {
        if (EventManager.isEventEnabled(EventType.SPECCHAT)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyEnabled"))
            return
        }
        EventManager.enableEvent(EventType.SPECCHAT)
        sendMessage(sender, messagesConfig.getString("messages.chat.specs.enabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.chat.specs.enabled.announce"), getOnlinePlayers())
        }
    }

    private fun handleSpecsOff(sender: CommandSender, args: CommandArguments) {
        if (!EventManager.isEventEnabled(EventType.SPECCHAT)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyDisabled"))
            return
        }
        EventManager.disableEvent(EventType.SPECCHAT)
        sendMessage(sender, messagesConfig.getString("messages.chat.specs.disabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.chat.specs.disabled.announce"), getOnlinePlayers())
        }
    }

    private fun handleSpecsPrivate(sender: CommandSender, args: CommandArguments) {
        if (EventManager.isEventEnabled(EventType.SPECCHATPUBLIC)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyEnabled"))
            return
        }
        EventManager.enableEvent(EventType.SPECCHATPUBLIC)
        sendMessage(sender, messagesConfig.getString("messages.chat.specs.privateEnabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.chat.specs.privateEnabled.announce"), getOnlinePlayers())
        }
    }

    private fun handleSpecsPublic(sender: CommandSender, args: CommandArguments) {
        if (!EventManager.isEventEnabled(EventType.SPECCHATPUBLIC)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyDisabled"))
            return
        }
        EventManager.disableEvent(EventType.SPECCHATPUBLIC)
        sendMessage(sender, messagesConfig.getString("messages.chat.specs.privateDisabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.chat.specs.privateDisabled.announce"), getOnlinePlayers())
        }
    }

    private fun handleBypassOn(sender: CommandSender, args: CommandArguments) {
        if (EventManager.isEventEnabled(EventType.SPECCHATBYPASS)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyEnabled"))
            return
        }
        EventManager.enableEvent(EventType.SPECCHATBYPASS)
        sendMessage(sender, messagesConfig.getString("messages.chat.specs.bypassEnabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.chat.specs.bypassEnabled.announce"), getOnlinePlayers())
        }
    }

    private fun handleBypassOff(sender: CommandSender, args: CommandArguments) {
        if (!EventManager.isEventEnabled(EventType.SPECCHATBYPASS)) {
            sendMessage(sender, messagesConfig.getString("messages.system.alreadyDisabled"))
            return
        }
        EventManager.disableEvent(EventType.SPECCHATBYPASS)
        sendMessage(sender, messagesConfig.getString("messages.chat.specs.bypassDisabled.execution"))
        if (!isSilent(args)) {
            broadcastToPlayers(messagesConfig.getString("messages.chat.specs.bypassDisabled.announce"), getOnlinePlayers())
        }
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/chat specs <on|off|private|public> | /chat bypass <on|off>"))
    }
}