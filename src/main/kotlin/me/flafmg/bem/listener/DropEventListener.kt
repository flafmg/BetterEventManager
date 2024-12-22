package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerDropItemEvent
import java.util.UUID

class DropEventListener(private val messagesConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerDropItem(event: PlayerDropItemEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId
        if (!EventManager.isEventEnabled(EventType.DROP) &&
            !player.hasPermission("bettereventmanager.bypass.drop") &&
            !BypassManager.hasBypass(EventType.DROP, playerId)) {

            event.isCancelled = true
            sendEventBlockedMessages(player, EventType.DROP, messagesConfig)
        }
    }
}