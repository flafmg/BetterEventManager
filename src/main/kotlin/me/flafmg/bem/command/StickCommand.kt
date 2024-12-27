package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.arguments.ArgumentSuggestions
import dev.jorel.commandapi.arguments.IntegerArgument
import dev.jorel.commandapi.arguments.StringArgument
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.util.sendMessage
import org.bukkit.Material
import org.bukkit.command.CommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.Player
import org.bukkit.inventory.ItemStack

class StickCommand(messagesConfig: ConfigManager) : BaseCommand("stick", messagesConfig, hasSilent = false) {

    init {
        baseCommand.withArguments(
            StringArgument("enchantment").replaceSuggestions(ArgumentSuggestions.strings("sharp", "kb")),
            IntegerArgument("level").setOptional(true)
        ).executes(CommandExecutor { sender, args ->
            handleStick(sender, args)
        })
    }

    private fun handleStick(sender: CommandSender, args: CommandArguments) {
        if (sender !is Player) {
            sendMessage(sender, messagesConfig.getString("messages.system.noPermission"))
            return
        }

        val player = sender
        val stick = ItemStack(Material.STICK)
        val enchantment = when (args.get("enchantment") as String) {
            "sharp" -> Enchantment.DAMAGE_ALL
            "kb" -> Enchantment.KNOCKBACK
            else -> {
                sendMessage(player, messagesConfig.getString("messages.system.invalidArgument"))
                return
            }
        }

        val level = if (args.get("level") != null) {
            args.get("level") as Int
        } else {
            when (args.get("enchantment") as String) {
                "sharp" -> 255
                "kb" -> 10
                else -> 1
            }
        }

        if (level <= 0) {
            sendMessage(player, messagesConfig.getString("messages.system.invalidArgument"))
            return
        }

        stick.addUnsafeEnchantment(enchantment, level.coerceAtMost(255))
        player.inventory.addItem(stick)
        sendMessage(player, messagesConfig.getString("messages.stick.execution")!!
            .replace("{enchantment}", args.get("enchantment") as String)
            .replace("{level}", level.toString()))
    }
}