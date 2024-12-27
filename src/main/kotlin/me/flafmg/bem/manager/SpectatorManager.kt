package me.flafmg.bem.manager

import org.bukkit.Bukkit
import org.bukkit.GameMode
import org.bukkit.entity.Player
import java.util.UUID

object SpectatorManager {
    private val spectators: MutableSet<UUID> = mutableSetOf()
    private val specChatViewers: MutableSet<UUID> = mutableSetOf()

    fun addPlayer(playerId: UUID) {
        val player = Bukkit.getPlayer(playerId) ?: return
        player.health = player.maxHealth
        player.gameMode = GameMode.SPECTATOR
        spectators.add(playerId)
        specChatViewers.add(playerId)
    }

    fun removePlayer(playerId: UUID) {
        val player = Bukkit.getPlayer(playerId) ?: return
        player.gameMode = GameMode.SURVIVAL
        spectators.remove(playerId)
        specChatViewers.remove(playerId)
    }

    fun isSpectator(playerId: UUID): Boolean {
        return spectators.contains(playerId)
    }

    fun getSpectators(): Set<UUID> {
        return spectators
    }

    fun toggleSpecChatViewer(playerId: UUID): Boolean {
        return if (specChatViewers.contains(playerId)) {
            specChatViewers.remove(playerId)
            false
        } else {
            specChatViewers.add(playerId)
            true
        }
    }

    fun canViewSpecChat(playerId: UUID): Boolean {
        return specChatViewers.contains(playerId)
    }

    fun getChatViewers(): Set<UUID> {
        return specChatViewers
    }

    fun isSpecChatEnabled(): Boolean {
        return EventManager.isEventEnabled(EventType.SPECCHAT)
    }

    fun isSpecChatBypassEnabled(): Boolean {
        return EventManager.isEventEnabled(EventType.SPECCHATBYPASS)
    }

    fun isSpecChatPublicEnabled(): Boolean {
        return EventManager.isEventEnabled(EventType.SPECCHATPUBLIC)
    }
}