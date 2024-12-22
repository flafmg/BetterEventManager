package me.flafmg.bem.listener

import me.flafmg.bem.manager.*
import me.flafmg.bem.util.sendEventBlockedMessages
import me.flafmg.bem.util.*
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.AsyncPlayerChatEvent
import java.util.UUID

class ChatEventListener(private val messagesConfig: ConfigManager, private val mainConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerChat(event: AsyncPlayerChatEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId
        val spectatorChatPrefix = mainConfig.getString("spectatorChatPrefix") ?: ""

        if (SpectatorManager.isSpectator(playerId)) {
            val allowSpectatorsChat = mainConfig.getBoolean("allowSpectatorsChat") ?: true
            val allowSpectatorsPrivateChat = mainConfig.getBoolean("allowSpectatorsPrivateChat") ?: false
            val allowSpectatorsChatBypass = mainConfig.getBoolean("allowSpectatorsChatBypass") ?: false

            if (!allowSpectatorsChat) {
                event.isCancelled = true
                sendEventBlockedMessages(player, EventType.CHAT, messagesConfig)
                return
            }

            if (spectatorChatPrefix.isNotEmpty()) {
                event.format = "${ colorMessage(spectatorChatPrefix)}${event.format}"
            }

            if (allowSpectatorsPrivateChat) {
                event.recipients.clear()
                event.recipients.addAll(SpectatorManager.getSpectators().mapNotNull { Bukkit.getPlayer(it) })
            } else {
                event.recipients.addAll(Bukkit.getOnlinePlayers().filter { it.hasPermission("bettereventmanager.bypass.specchat") })
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
    }
}