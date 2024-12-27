package me.flafmg.bem.listener

import me.flafmg.bem.manager.*
import me.flafmg.bem.util.sendEventBlockedMessages
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.UUID

class ChatEventListener(private val messagesConfig: ConfigManager, private val mainConfig: ConfigManager) : Listener {

    @EventHandler(priority = EventPriority.HIGHEST)
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId
        val spectatorChatPrefix = mainConfig.getString("spectatorChatPrefix") ?: ""

        if (SpectatorManager.isSpectator(playerId)) {
            val allowSpectatorsChat = EventManager.isEventEnabled(EventType.SPECCHAT)
            val allowSpectatorsPrivateChat = EventManager.isEventEnabled(EventType.SPECCHATPUBLIC)
            val allowSpectatorsChatBypass = EventManager.isEventEnabled(EventType.SPECCHATBYPASS)

            if (!allowSpectatorsChat) {
                event.isCancelled = true
                sendEventBlockedMessages(player, EventType.CHAT, messagesConfig)
                return
            }

            if (spectatorChatPrefix.isNotEmpty()) {
                event.format = "${colorMessage(spectatorChatPrefix)}${event.format}"
            }

            if (allowSpectatorsPrivateChat) {
                event.recipients.clear()
                event.recipients.addAll(SpectatorManager.getSpectators().mapNotNull { Bukkit.getPlayer(it) })
                event.recipients.addAll(Bukkit.getOnlinePlayers().filter { SpectatorManager.canViewSpecChat(it.uniqueId) })
            } else {
                event.recipients.addAll(Bukkit.getOnlinePlayers())
            }

            if (!allowSpectatorsChatBypass && !EventManager.isEventEnabled(EventType.CHAT)) {
                event.isCancelled = true
                sendEventBlockedMessages(player, EventType.CHAT, messagesConfig)
                return
            }
        } else {
            if (!EventManager.isEventEnabled(EventType.CHAT) &&
                !player.hasPermission("bettereventmanager.bypass.chat") &&
                !BypassManager.hasBypass(EventType.CHAT, playerId)) {

                event.isCancelled = true
                sendEventBlockedMessages(player, EventType.CHAT, messagesConfig)
            }
        }

        event.recipients.addAll(Bukkit.getOnlinePlayers().filter { SpectatorManager.canViewSpecChat(it.uniqueId) && !SpectatorManager.isSpectator(it.uniqueId) })
    }
}