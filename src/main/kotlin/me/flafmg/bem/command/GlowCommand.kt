package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.EntitySelectorArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.GlowManager
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class GlowCommand(messagesConfig: ConfigManager) : BaseCommand("glow", messagesConfig) {

    init {
        baseCommand.withSubcommand(
            CommandAPICommand("set")
                .withArguments(EntitySelectorArgument.ManyPlayers("targets"), StringArgument("color"), StringArgument("silent").setOptional(true))
                .executes(CommandExecutor { sender, args ->
                    handleSetGlow(sender, args)
                })
        )
        baseCommand.withSubcommand(
            CommandAPICommand("remove")
                .withArguments(EntitySelectorArgument.ManyPlayers("targets"), StringArgument("silent").setOptional(true))
                .executes(CommandExecutor { sender, args ->
                    handleRemoveGlow(sender, args)
                })
        )
    }

    private fun handleSetGlow(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        val colorCode = args.get("color") as String
        val color = ChatColor.getByChar(colorCode.replace("&", ""))

        if (color != null) {
            val targetNames = mutableListOf<String>()
            targets.forEach { target ->
                if (!GlowManager.hasGlow(target)) {
                    GlowManager.setGlow(target, color)
                    targetNames.add(target.name)
                    genericLog(sender, "/glow set ${target.name} $colorCode", messagesConfig)
                } else {
                    sendMessage(sender, messagesConfig.getString("messages.system.alreadyAdded"), mutableMapOf("targets" to target.name))
                }
            }
            if (targetNames.isNotEmpty()) {
                val targetsS = targetNames.joinToString(",")
                sendMessage(sender, messagesConfig.getString("messages.glow.execution"), mutableMapOf("targets" to targetsS, "color" to color.name))
            }
        } else {
            sendMessage(sender, messagesConfig.getString("messages.system.invalidArgument"))
        }
    }

    private fun handleRemoveGlow(sender: CommandSender, args: CommandArguments) {
        val targets = args.get("targets") as Collection<Player>
        val targetNames = mutableListOf<String>()

        targets.forEach { target ->
            if (GlowManager.hasGlow(target)) {
                GlowManager.removeGlow(target)
                targetNames.add(target.name)
                genericLog(sender, "/glow remove ${target.name}", messagesConfig)
            } else {
                sendMessage(sender, messagesConfig.getString("messages.system.alreadyRemoved"), mutableMapOf("targets" to target.name))
            }
        }
        if (targetNames.isNotEmpty()) {
            val targetsS = targetNames.joinToString(",")
            sendMessage(sender, messagesConfig.getString("messages.glow.removed"), mutableMapOf("targets" to targetsS))
        }
    }
}