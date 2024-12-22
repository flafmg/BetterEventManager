package me.flafmg.bem.manager

object MaxPlayersManager {
    private var maxPlayers: Int = 0

    fun setMaxPlayers(count: Int) {
        maxPlayers = count
    }

    fun getMaxPlayers(): Int {
        return maxPlayers
    }

    fun isMaxPlayersEnabled(): Boolean {
        return EventManager.isEventEnabled(EventType.MAXPLAYERS)
    }
}