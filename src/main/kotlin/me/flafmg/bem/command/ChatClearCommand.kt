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
import org.bukkit.command.CommandSender

class ChatClearCommand(messagesConfig: ConfigManager) : BaseCommand("chatclear", messagesConfig) {

    init {
        baseCommand.executes(CommandExecutor { sender, args ->
            handleChatClear(sender, args)
        })
    }

    private fun handleChatClear(sender: CommandSender, args: CommandArguments) {
        if (!sender.hasPermission("bettereventmanager.command.chatclear")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return
        }

        for (player in getOnlinePlayers()) {
            for (i in 0..100) {
                player.sendMessage("")
            }
        }

        sendMessage(sender, messagesConfig.getString("messages.chatclear.execution"))
        if (!super.hasSilent) {
            broadcastToPlayers(messagesConfig.getString("messages.chatclear.announce"), getOnlinePlayers())
        }

        genericLog(sender, "/chatclear ${args.rawArgs().joinToString(" ")}", messagesConfig)
    }
}