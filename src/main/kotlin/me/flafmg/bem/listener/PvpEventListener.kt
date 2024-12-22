package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.UUID

class PvpEventListener(private val messagesConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerDamage(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        if (damager is Player) {
            val playerId: UUID = damager.uniqueId
            if (!EventManager.isEventEnabled(EventType.PVP) &&
                !damager.hasPermission("bettereventmanager.bypass.pvp") &&
                !BypassManager.hasBypass(EventType.PVP, playerId)) {

                event.isCancelled = true
                sendEventBlockedMessages(damager, EventType.PVP, messagesConfig)
            }
        }
    }

}