package me.flafmg.bem.command

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.successLog
import me.flafmg.bem.util.errLog
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class ReloadConfigCommand(
    private val mainConfig: ConfigManager,
    private val messagesConfig: ConfigManager
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        val successMessage = messagesConfig.getString("messages.reloadconfig.execution")!!
        val errorMessage = messagesConfig.getString("messages.reloadconfig.error")!!

        return try {
            mainConfig.reload()
            messagesConfig.reload()
            successLog("Configuração recarregada com sucesso.")
            sendMessage(sender, successMessage)
            true
        } catch (e: Exception) {
            errLog("Erro ao recarregar a configuração: ${e.message}")
            sendMessage(sender, errorMessage)
            false
        }
    }
}