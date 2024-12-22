package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.UUID

class InteractEventListener(private val messagesConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId
        if (!EventManager.isEventEnabled(EventType.INTERACT) &&
            !player.hasPermission("bettereventmanager.bypass.interact") &&
            !BypassManager.hasBypass(EventType.INTERACT, playerId)) {

            event.isCancelled = true
            sendEventBlockedMessages(player, EventType.INTERACT, messagesConfig)
        }
    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId
        if (!EventManager.isEventEnabled(EventType.INTERACT) &&
            !player.hasPermission("bettereventmanager.bypass.interact") &&
            !BypassManager.hasBypass(EventType.INTERACT, playerId)) {

            event.isCancelled = true
            sendEventBlockedMessages(player, EventType.INTERACT, messagesConfig)
        }
    }
}