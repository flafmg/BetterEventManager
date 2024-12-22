package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.colorMessage
import me.flafmg.bem.util.getOnlinePlayers
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.entity.PlayerDeathEvent

class HardcoreKickListener(private val messagesConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerDeath(event: PlayerDeathEvent) {
        val player = event.entity
        print("hoi" + EventManager.isEventEnabled(EventType.HCK))
        if (EventManager.isEventEnabled(EventType.HCK) &&
            !BypassManager.hasBypass(EventType.HCK, player.uniqueId)
            && !player.hasPermission("betterevent.bypass.hck")) {
            event.deathMessage = null
            broadcastToPlayers(messagesConfig.getString("messages.system.eliminationMessage"), getOnlinePlayers(), mutableMapOf("player" to player.name))
            player.kickPlayer(colorMessage(messagesConfig.getString("messages.hck.kickMessage")!!))
        }
    }
}