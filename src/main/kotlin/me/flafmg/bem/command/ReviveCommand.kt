package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.SpectatorManager
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class ReviveCommand(messagesConfig: ConfigManager) : BaseCommand("revive", messagesConfig, aliases = listOf("reviver")) {

    init {
        baseCommand.withArguments(EntitySelectorArgument.ManyPlayers("targets"))
            .executes(CommandExecutor { sender, args ->
                handleRevive(sender, args)
            })
    }

    private fun handleRevive(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        if (targets.isEmpty()) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }

        val allNotSpectators = targets.all { !SpectatorManager.isSpectator(it.uniqueId) }
        if (allNotSpectators) {
            val targetNames = targets.joinToString(", ") { it.name }
            sendMessage(
                sender,
                messagesConfig.getString("messages.system.alreadyRemoved"),
                mutableMapOf("targets" to targetNames)
            )
            return
        }

        val revivedPlayers = mutableListOf<String>()
        targets.forEach { target ->
            if (SpectatorManager.isSpectator(target.uniqueId)) {
                SpectatorManager.removePlayer(target.uniqueId)
                val location =
                    if (sender is Player) sender.location else Bukkit.getWorlds().firstOrNull()?.spawnLocation
                if (location != null) {
                    target.teleport(location)
                }
                revivedPlayers.add(target.name)
                sendMessage(target, messagesConfig.getString("messages.revive.targetMessage"))
            }
        }

        if (revivedPlayers.isNotEmpty()) {
            val targetNames = revivedPlayers.joinToString(", ")
            sendMessage(
                sender,
                messagesConfig.getString("messages.revive.execution"),
                mutableMapOf("targets" to targetNames)
            )
            if (!super.hasSilent) {
                broadcastToPlayers(
                    messagesConfig.getString("messages.revive.announce"),
                    getOnlinePlayers(),
                    mutableMapOf("targets" to targetNames)
                )
            }
            revivedPlayers.forEach { targetName ->
                genericLog(sender, "revive $targetName", messagesConfig)
            }
        }
    }
}