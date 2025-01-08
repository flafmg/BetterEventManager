package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender

class BemHelpCommand(messagesConfig: ConfigManager) : BaseCommand("bemhelp", messagesConfig, hasSilent = false) {

    init {
        baseCommand.executes(CommandExecutor { sender, args ->
            handleHelp(sender, args)
        })
    }

    private fun handleHelp(sender: CommandSender, args: CommandArguments) {
        val helpMessage = messagesConfig.getString("messages.help.execution")!!
        sendMessage(sender, helpMessage)
    }
}