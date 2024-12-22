package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageEvent
import java.util.UUID

class DamageEventListener(private val messagesConfig: ConfigManager) : Listener {

    @EventHandler
    fun onEntityDamage(event: EntityDamageEvent) {
        val entity = event.entity
        if (entity is Player) {
            val playerId: UUID = entity.uniqueId
            if (!EventManager.isEventEnabled(EventType.DAMAGE) &&
                !entity.hasPermission("bettereventmanager.bypass.damage") &&
                !BypassManager.hasBypass(EventType.DAMAGE, playerId)) {

                event.isCancelled = true
            }
        }
    }
}