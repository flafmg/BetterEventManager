package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerPickupItemEvent
import java.util.UUID

class PickupEventListener(private val messagesConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerPickupItem(event: PlayerPickupItemEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId
        if (!EventManager.isEventEnabled(EventType.PICKUP) &&
            !player.hasPermission("bettereventmanager.bypass.pickup") &&
            !BypassManager.hasBypass(EventType.PICKUP, playerId)) {

            event.isCancelled = true
            sendEventBlockedMessages(player, EventType.PICKUP, messagesConfig)
        }
    }
}