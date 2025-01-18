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

        val allSpectators = targets.all { SpectatorManager.isSpectator(it.uniqueId) }
        if (allSpectators) {
            val targetNames = targets.joinToString(", ") { it.name }
            sendMessage(
                sender,
                messagesConfig.getString("messages.system.alreadyAdded"),
                mutableMapOf("targets" to targetNames)
            )
            return
        }

        val addedPlayers = mutableListOf<String>()
        targets.forEach { target ->
            if (!SpectatorManager.isSpectator(target.uniqueId)) {
                SpectatorManager.addPlayer(target.uniqueId)
                addedPlayers.add(target.name)
                sendMessage(target, messagesConfig.getString("messages.setspec.targetMessage"))
            }
        }

        if (addedPlayers.isNotEmpty()) {
            val targetNames = addedPlayers.joinToString(", ")
            sendMessage(
                sender,
                messagesConfig.getString("messages.setspec.execution"),
                mutableMapOf("targets" to targetNames)
            )
            if (!super.hasSilent) {
                broadcastToPlayers(
                    messagesConfig.getString("messages.setspec.announce"),
                    getOnlinePlayers(),
                    mutableMapOf("targets" to targetNames)
                )
            }
            addedPlayers.forEach { targetName ->
                genericLog(sender, "setspec $targetName", messagesConfig)
            }
        }
    }
}