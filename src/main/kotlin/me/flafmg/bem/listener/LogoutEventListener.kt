package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.GlowManager
import me.flafmg.bem.manager.SpectatorManager
import org.bukkit.Bukkit
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerQuitEvent

class LogoutEventListener(private val config: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerQuit(event: PlayerQuitEvent) {
        val player = event.player
        val playerId = player.uniqueId

        if (!config.getBoolean("autoSaveBypassChanges")!!) {
            BypassManager.removeAllPermissions(playerId)
        }

        GlowManager.removeGlow(player)
        if (SpectatorManager.isSpectator(playerId)) {
            SpectatorManager.removePlayer(playerId)
            val spawnLocation = Bukkit.getWorlds().firstOrNull()?.spawnLocation
            if (spawnLocation != null) {
                player.teleport(spawnLocation)
            }
        }
    }
}