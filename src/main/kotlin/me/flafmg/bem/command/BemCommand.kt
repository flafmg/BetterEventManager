package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.BetterEventManager
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender

class BemCommand(private val plugin: BetterEventManager) : BaseCommand("bem", plugin.messagesConfig, aliases = listOf("BetterEventManager"), hasSilent = false) {

    init {
        baseCommand.executes(CommandExecutor { sender, args ->
            handleBem(sender, args)
        })
    }

    private fun handleBem(sender: CommandSender, args: CommandArguments) {
        val version = plugin.description.version
        sendMessage(sender, "&bBetterEventManager made by &6@flaffymg&b, version: &6$version. &9&nhttps://github.com/flafmg/BetterEventManager")
    }
}