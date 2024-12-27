package me.flafmg.bem.command

import dev.jorel.commandapi.CommandAPICommand
import dev.jorel.commandapi.executors.CommandArguments
import dev.jorel.commandapi.executors.CommandExecutor
import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.CommandSender

class StatusCommand(messagesConfig: ConfigManager) : BaseCommand("status", messagesConfig, hasSilent = false) {

    init {
        baseCommand.withArguments(eventTypeArgument("eventType").setOptional(true))
            .executes(CommandExecutor { sender, args ->
                handleStatus(sender, args)
            })
    }

    private fun handleStatus(sender: CommandSender, args: CommandArguments) {
        val eventTypeArg = args.get("eventType") as? EventType
        print(eventTypeArg)
        if (eventTypeArg == null) {
            showAllEventsStatus(sender)
        } else {
            showEventBypassStatus(sender, eventTypeArg)
        }
    }

    private fun showAllEventsStatus(sender: CommandSender) {
        val header = messagesConfig.getString("messages.status.header")!!
        sendMessage(sender, header)

        for (eventType in EventType.values()) {
            val state = if (EventManager.isEventEnabled(eventType)) {
                messagesConfig.getString("messages.status.stateOn")!!
            } else {
                messagesConfig.getString("messages.status.stateOff")!!
            }
            val bypassCount = BypassManager.getPlayers(eventType).size
            val eventItem = messagesConfig.getString("messages.status.eventItem")!!
                .replace("{event}", eventType.name.lowercase())
                .replace("{state}", state)
                .replace("{nbypass}", bypassCount.toString())
            sendMessage(sender, eventItem)
        }
    }

    private fun showEventBypassStatus(sender: CommandSender, eventType: EventType) {
        val state = if (EventManager.isEventEnabled(eventType)) {
            messagesConfig.getString("messages.status.stateOn")!!
        } else {
            messagesConfig.getString("messages.status.stateOff")!!
        }
        val players = BypassManager.getPlayers(eventType).mapNotNull { sender.server.getPlayer(it)?.name }
        val eventHeader = messagesConfig.getString("messages.status.eventHeader")!!
            .replace("{event}", eventType.name.lowercase())
            .replace("{state}", state)
        sendMessage(sender, eventHeader)

        if (players.isEmpty()) {
            sendMessage(sender, messagesConfig.getString("messages.status.noPlayers")!!)
        } else {
            sendMessage(sender, messagesConfig.getString("messages.status.playersHeader")!!)
            players.forEach { player ->
                val playerItem = messagesConfig.getString("messages.status.playerItem")!!
                    .replace("{player}", player)
                sendMessage(sender, playerItem)
            }
        }
    }
}

