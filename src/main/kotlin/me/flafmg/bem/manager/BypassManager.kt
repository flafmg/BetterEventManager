package me.flafmg.bem.manager

import org.bukkit.Bukkit
import java.util.UUID

object BypassManager {
    private val bypassLists: MutableMap<EventType, MutableSet<UUID>> = mutableMapOf()
    private lateinit var config: ConfigManager

    fun initialize(configManager: ConfigManager) {
        config = configManager
        loadBypassLists()
    }

    private fun loadBypassLists() {
        for (eventType in EventType.entries) {
            val playerNames = config.getStringList("bypass.${eventType.name.lowercase()}")
            val playerUUIDs = playerNames!!.mapNotNull { Bukkit.getPlayer(it)?.uniqueId }
            bypassLists[eventType] = playerUUIDs.toMutableSet()!!
        }
    }

    fun saveBypassLists() {
        for (eventType in EventType.entries) {
            val playerNames = bypassLists[eventType]?.mapNotNull { Bukkit.getPlayer(it)?.name } ?: emptyList()
            config.set("bypass.${eventType.name.lowercase()}", playerNames)
        }
        config.save()
    }

    fun addPlayer(category: EventType, playerId: UUID) {
        bypassLists.computeIfAbsent(category) { mutableSetOf() }
        bypassLists[category]?.add(playerId)
        if (config.getBoolean("autoSaveBypassChanges")!!) {
            saveBypassLists()
        }
    }

    fun removePlayer(category: EventType, playerId: UUID) {
        bypassLists[category]?.remove(playerId)
        if (bypassLists[category]?.isEmpty() == true) {
            bypassLists.remove(category)
        }
        if (config.getBoolean("autoSaveBypassChanges")!!) {
            saveBypassLists()
        }
    }

    fun hasBypass(category: EventType, playerId: UUID): Boolean {
        return bypassLists[category]?.contains(playerId) == true
    }

    fun clearAll() {
        bypassLists.clear()
    }

    fun getPlayers(category: EventType): Set<UUID> {
        return bypassLists[category] ?: emptySet()
    }

    fun removeAllPermissions(playerId: UUID) {
        for (category in EventType.entries) {
            bypassLists[category]?.remove(playerId)
        }
    }
}