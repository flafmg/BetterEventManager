package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.manager.SpectatorManager
import me.flafmg.bem.util.broadcastToPlayers
import me.flafmg.bem.util.getOnlinePlayers
import org.bukkit.Location
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
                val playerLocation = player.getLocation();

                if (playerLocation.y < 0) {
                    player.teleport(playerLocation.world!!.spawnLocation)
                }

                val keepInventory = player.world.getGameRuleValue("keepInventory")?.toBoolean() ?: false

                if (!keepInventory) {
                    player.inventory.contents.forEach {
                        if (it != null) {
                            player.world.dropItemNaturally(player.location, it)
                        }
                    }
                    player.inventory.armorContents.forEach {
                        if (it != null) {
                            player.world.dropItemNaturally(player.location, it)
                        }
                    }
                }
                player.inventory.clear()

                if (!SpectatorManager.isSpectator(player.uniqueId)) {
                    SpectatorManager.addPlayer(player.uniqueId)
                    broadcastToPlayers(
                        messagesConfig.getString("messages.system.eliminationMessage"),
                        getOnlinePlayers(),
                        mutableMapOf("player" to player.name)
                    )
                } else{
                    player.teleport(player.world.spawnLocation)
                    player.health = 20.0
                }
            }
        }
    }
}