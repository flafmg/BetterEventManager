package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.entity.EntityType
import org.bukkit.entity.Player
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.EntityDamageByEntityEvent
import java.util.UUID

class PvmEventListener(private val messagesConfig: ConfigManager, private val mainConfig: ConfigManager) : Listener {

    @EventHandler
    fun onEntityDamageByEntity(event: EntityDamageByEntityEvent) {
        val damager = event.damager
        val entity = event.entity
        if (damager is Player && entity !is Player) {
            val playerId: UUID = damager.uniqueId
            val whitelist = mainConfig.getStringList("pvmWhiteList", suppressWarns = true)?.map { EntityType.valueOf(it) }
            if (!EventManager.isEventEnabled(EventType.PVM) &&
                !damager.hasPermission("bettereventmanager.bypass.pvm") &&
                !BypassManager.hasBypass(EventType.PVM, playerId) &&
                whitelist?.contains(entity.type) != true
            ) {
                event.isCancelled = true
                sendEventBlockedMessages(damager, EventType.PVM, messagesConfig)
            }
        }
    }
}