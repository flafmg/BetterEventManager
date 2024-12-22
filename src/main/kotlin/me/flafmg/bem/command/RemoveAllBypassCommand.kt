package me.flafmg.bem.command

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.sendMessage
import org.bukkit.Bukkit
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class RemoveAllBypassCommand(
    private val messagesConfig: ConfigManager
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (!sender.hasPermission("bettereventmanager.command.removeallbypass")) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return true
        }

        if (args.isEmpty()) {
            sendUsage(sender)
            return true
        }

        if (args[0] == "*") {
            BypassManager.clearAll()
            sendMessage(sender, messagesConfig.getString("messages.removeallbypass.all"))
        } else {
            val player = Bukkit.getPlayer(args[0])
            if (player == null) {
                sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
                return true
            }
            BypassManager.removeAllPermissions(player.uniqueId)
            sendMessage(sender, messagesConfig.getString("messages.removeallbypass.player")!!.replace("{player}", player.name))
        }

        return true
    }

    private fun sendUsage(sender: CommandSender) {
        sendMessage(sender, messagesConfig.getString("messages.system.invalidUsage"), mutableMapOf("usage" to "/RemoveAllBypass <player/*>"))
    }
}