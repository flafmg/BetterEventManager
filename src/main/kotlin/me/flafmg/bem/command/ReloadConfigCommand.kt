package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.successLog
import me.flafmg.bem.util.errLog
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender

class ReloadConfigCommand(
    private val mainConfig: ConfigManager,
    messagesConfig: ConfigManager
) : BaseCommand("reloadconfig", messagesConfig, hasSilent = false) {

    init {
        baseCommand
            .executes(CommandExecutor { sender, args ->
                handleReloadConfig(sender, args)
            })
    }

    private fun handleReloadConfig(sender: CommandSender, args: CommandArguments) {
        val successMessage = messagesConfig.getString("messages.reloadconfig.execution")!!
        val errorMessage = messagesConfig.getString("messages.reloadconfig.error")!!

        try {
            mainConfig.reload()
            messagesConfig.reload()
            successLog("Configuration reloaded sucessfully")
            sendMessage(sender, successMessage)
        } catch (e: Exception) {
            errLog("Error reloading configuration ${e.message}")
            sendMessage(sender, errorMessage)
        }
    }
}