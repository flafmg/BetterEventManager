package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.SpectatorManager
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ToggleSpecChatCommand(messagesConfig: ConfigManager) : BaseCommand("togglespecchat", messagesConfig) {

    init {
        baseCommand.executes(CommandExecutor { sender, args ->
            handleToggleSpecChat(sender, args)
        })
    }

    private fun handleToggleSpecChat(sender: CommandSender, args: CommandArguments) {
        if (sender !is Player) {
            sendMessage(sender, "&cThis command can only be executed by players")
            return
        }

        val playerId = sender.uniqueId
        val enabled = SpectatorManager.toggleSpecChatViewer(playerId)
        val messageKey = if (enabled) {
            "messages.togglespecchat.enabled"
        } else {
            "messages.togglespecchat.disabled"
        }
        sendMessage(sender, messagesConfig.getString(messageKey))
    }
}