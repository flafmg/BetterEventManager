package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.*
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class SmiteCommand(messagesConfig: ConfigManager) : BaseCommand("smite", messagesConfig) {

    init {
        baseCommand.withArguments(EntitySelectorArgument.OnePlayer("targets"))
            .executes(CommandExecutor { sender, args ->
                handleSmite(sender, args)
            })
    }

    private fun handleSmite(sender: CommandSender, args: CommandArguments) {
        val target = args.get("targets") as Player
        target.world.strikeLightningEffect(target.location)

        sendMessage(sender, messagesConfig.getString("messages.smite.execution"), mutableMapOf("targets" to target.name))
        sendMessage(target, messagesConfig.getString("messages.smite.targetMessage"))
        if (!super.hasSilent) {
            broadcastToPlayers(messagesConfig.getString("messages.smite.announce"), getOnlinePlayers(), mutableMapOf("targets" to target.name))
        }

        genericLog(sender, "smite ${target.name}", messagesConfig)
    }
}