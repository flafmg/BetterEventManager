package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender

class AllCommand(messagesConfig: ConfigManager) : BaseCommand("all", messagesConfig) {

    init {
        baseCommand.withSubcommand(
            CommandAPICommand("on")
                .executes(CommandExecutor { sender, args ->
                    handleAllOn(sender, args)
                })
        )
        baseCommand.withSubcommand(
            CommandAPICommand("off")
                .executes(CommandExecutor { sender, args ->
                    handleAllOff(sender, args)
                })
        )
    }

    private fun handleAllOn(sender: CommandSender, args: CommandArguments) {
        EventType.entries.filter { it != EventType.HCD && it != EventType.HCK && it != EventType.MAXPLAYERS && it != EventType.SPECCHATBYPASS && it != EventType.SPECCHATPUBLIC && it != EventType.SPECCHAT }.forEach {
            EventManager.enableEvent(it)
        }

        sendMessage(sender, messagesConfig.getString("messages.all.enabled.execution"))

        if(!super.hasSilent)
            broadcastToPlayers(messagesConfig.getString("messages.all.enabled.announce"), getOnlinePlayers())
    }

    private fun handleAllOff(sender: CommandSender, args: CommandArguments) {
        EventType.entries.filter { it != EventType.HCD && it != EventType.HCK && it != EventType.MAXPLAYERS && it != EventType.SPECCHATBYPASS && it != EventType.SPECCHATPUBLIC && it != EventType.SPECCHAT}.forEach {
            EventManager.disableEvent(it)
        }

        sendMessage(sender, messagesConfig.getString("messages.all.disabled.execution"))
        if(!super.hasSilent)
            broadcastToPlayers(messagesConfig.getString("messages.all.disabled.announce"), getOnlinePlayers())
    }
}