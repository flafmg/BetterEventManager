package me.flafmg.bem.command

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendMessage
import org.bukkit.command.Command
import org.bukkit.command.CommandExecutor
import org.bukkit.command.CommandSender

class StatusCommand(
    private val messagesConfig: ConfigManager
) : CommandExecutor {

    override fun onCommand(sender: CommandSender, command: Command, label: String, args: Array<String>): Boolean {
        if (args.isEmpty()) {
            showAllEventsStatus(sender)
        } else {
            val eventType = EventType.valueOf(args[0].uppercase())
            showEventBypassStatus(sender, eventType)
        }
        return true
    }

    private fun showAllEventsStatus(sender: CommandSender) {
        val header = messagesConfig.getString("messages.status.header")!!
        sendMessage(sender, header)

        for (eventType in EventType.entries) {
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