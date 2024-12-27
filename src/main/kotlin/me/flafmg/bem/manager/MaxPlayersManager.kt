package me.flafmg.bem.manager

object MaxPlayersManager {
    private lateinit var configManager: ConfigManager

    fun initialize(config: ConfigManager) {
        configManager = config
        maxPlayers = configManager.getInt("max_players") ?: 0
    }

    private var maxPlayers: Int = 0

    fun setMaxPlayers(count: Int) {
        maxPlayers = count
        if (configManager.getBoolean("autoSaveEventChanges") == true) {
            configManager.set("max_players", count)
            configManager.save()
        }
    }

    fun getMaxPlayers(): Int {
        return maxPlayers
    }

    fun isMaxPlayersEnabled(): Boolean {
        return EventManager.isEventEnabled(EventType.MAXPLAYERS)
    }
}