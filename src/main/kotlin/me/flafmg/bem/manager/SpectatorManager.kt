package me.flafmg.bem.manager

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import java.util.UUID

object SpectatorManager {
    private val spectators: MutableSet<UUID> = mutableSetOf()

    fun addPlayer(playerId: UUID) {
        val player = Bukkit.getPlayer(playerId) ?: return
        player.health = player.maxHealth
        player.gameMode = GameMode.SPECTATOR
        spectators.add(playerId)
    }

    fun removePlayer(playerId: UUID) {
        val player = Bukkit.getPlayer(playerId) ?: return
        player.gameMode = GameMode.SURVIVAL
        spectators.remove(playerId)
    }

    fun isSpectator(playerId: UUID): Boolean {
        return spectators.contains(playerId)
    }

    fun getSpectators(): Set<UUID> {
        return spectators
    }
}