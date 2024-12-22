package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.manager.SpectatorManager
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.getOnlinePlayers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import org.bukkit.entity.Player

class HardcoreDeathListener(private val messagesConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerDamage(event: EntityDamageEvent) {
        val player = event.entity as? Player ?: return

        if (EventManager.isEventEnabled(EventType.HCD) &&
            !BypassManager.hasBypass(EventType.HCD, player.uniqueId) &&
            !player.hasPermission("betterevent.bypass.hcd")) {

            val finalHealth = player.health - event.finalDamage
            if (finalHealth <= 0) {
                event.isCancelled = true
                SpectatorManager.addPlayer(player.uniqueId)
                broadcastToPlayers(messagesConfig.getString("messages.system.eliminationMessage"), getOnlinePlayers(), mutableMapOf("player" to player.name))
            }
        }
    }
}