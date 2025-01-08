package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class RegenCommand(messagesConfig: ConfigManager) : BaseCommand("regen", messagesConfig) {

    init {
        baseCommand.withArguments(EntitySelectorArgument.ManyPlayers("targets"))
            .executes(CommandExecutor { sender, args ->
                handleRegen(sender, args)
            })
    }

    private fun handleRegen(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        if (targets.isEmpty()) {
            sendMessage(sender, messagesConfig.getString("messages.system.playerNotFound"))
            return
        }

        targets.forEach { target ->
            target.health = target.maxHealth
            target.foodLevel = 20
            target.saturation = 20f
            sendMessage(target, messagesConfig.getString("messages.regen.targetMessage"))
        }

        val targetNames = targets.joinToString(", ") { it.name }
        sendMessage(sender, messagesConfig.getString("messages.regen.execution"), mutableMapOf("targets" to targetNames))
    }
}