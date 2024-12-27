package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.SpectatorManager
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.genericLog
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.sendMessage
import org.bukkit.GameMode
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SetSpecCommand(messagesConfig: ConfigManager) : BaseCommand("setspec", messagesConfig, aliases = listOf("matar")) {

    init {
        baseCommand.withArguments(EntitySelectorArgument.ManyPlayers("targets"))
            .executes(CommandExecutor { sender, args ->
                handleSetSpec(sender, args)
            })
    }

    private fun handleSetSpec(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        if (targets.isEmpty()) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }

        targets.forEach { target ->
            if (SpectatorManager.isSpectator(target.uniqueId)) {
                sendMessage(sender, messagesConfig.getString("messages.system.alreadyAdded"), mutableMapOf("targets" to target.name))
                return
            }

            SpectatorManager.addPlayer(target.uniqueId)
            sendMessage(sender, messagesConfig.getString("messages.setspec.execution"), mutableMapOf("targets" to target.name))
            sendMessage(target, messagesConfig.getString("messages.setspec.targetMessage"))
            if (!super.hasSilent) {
                broadcastToPlayers(messagesConfig.getString("messages.setspec.announce"),
                    getOnlinePlayers(), mutableMapOf("targets" to target.name))
            }

            genericLog(sender, "setspec ${target.name}", messagesConfig)
        }
    }
}