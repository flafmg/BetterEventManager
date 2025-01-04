package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.entity.Player
import org.bukkit.event.Cancellable
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.block.BlockBreakEvent
import org.bukkit.event.block.BlockPlaceEvent
import org.bukkit.event.player.PlayerBucketEmptyEvent
import java.util.UUID

class BuildEventListener(private val messagesConfig: ConfigManager) : Listener {

    private fun handleBlockEvent(player: Player, eventType: EventType, event: Cancellable) {
        val playerId: UUID = player.uniqueId
        if (!EventManager.isEventEnabled(eventType) &&
            !player.hasPermission("bettereventmanager.bypass.build") &&
            !BypassManager.hasBypass(eventType, playerId)) {

            event.isCancelled = true
            sendEventBlockedMessages(player, eventType, messagesConfig)
        }
    }

    @EventHandler
    fun onBlockPlace(event: BlockPlaceEvent) {
        handleBlockEvent(event.player, EventType.BUILD, event)
    }

    @EventHandler
    fun onBlockBreak(event: BlockBreakEvent) {
        handleBlockEvent(event.player, EventType.BUILD, event)
    }

    @EventHandler
    fun onPlayerBucketEmpty(event: PlayerBucketEmptyEvent) {
        handleBlockEvent(event.player, EventType.BUILD, event)
    }
}