package me.flafmg.bem.listener

import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.MaxPlayersManager
import me.flafmg.bem.manager.SpectatorManager
import me.flafmg.bem.util.colorMessage
import me.flafmg.bem.util.getOnlinePlayers
import me.flafmg.bem.util.sendMessage
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.event.player.PlayerLoginEvent

class JoinEventListener(private val messagesConfig: ConfigManager, private val mainConfig: ConfigManager) : Listener {

    @EventHandler
    fun onPlayerLogin(event: PlayerLoginEvent) {
        val player = event.player
        val kickMessage = messagesConfig.getString("messages.maxplayers.kickMessage") ?: "Servidor cheio."
        val specJoinWhenFull = mainConfig.getBoolean("specJoinWhenFull") ?: false

        if (MaxPlayersManager.isMaxPlayersEnabled() &&
            getOnlinePlayers().size >= MaxPlayersManager.getMaxPlayers() &&
            !player.hasPermission("bettereventmanager.bypass.maxplayers") &&
            !specJoinWhenFull) {

            event.disallow(PlayerLoginEvent.Result.KICK_FULL, colorMessage(kickMessage))
        }
    }

    @EventHandler
    fun onPlayerJoin(event: PlayerJoinEvent) {
        val player = event.player
        val specJoinWhenFull = mainConfig.getBoolean("specJoinWhenFull") ?: false

        if (MaxPlayersManager.isMaxPlayersEnabled() &&
            getOnlinePlayers().size >= MaxPlayersManager.getMaxPlayers() &&
            !player.hasPermission("bettereventmanager.bypass.maxplayers") &&
            specJoinWhenFull) {

            SpectatorManager.addPlayer(player.uniqueId)
            sendMessage(player, messagesConfig.getString("messages.maxplayers.spectatorJoinMessage"))
        }
    }
}