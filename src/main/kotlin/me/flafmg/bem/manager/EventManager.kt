package me.flafmg.bem.manager

object EventManager {
    private val eventStatus: MutableMap<EventType, Boolean> = mutableMapOf()
    private lateinit var config: ConfigManager

    init {
        for (eventType in EventType.entries) {
            eventStatus[eventType] = false
        }
    }

    fun initialize(configManager: ConfigManager) {
        config = configManager
        loadEventStatus()
    }

    private fun loadEventStatus() {
        for (eventType in EventType.entries) {
            eventStatus[eventType] = config.getBoolean("events.${eventType.name.lowercase()}")!!
        }
    }

    fun saveEventStatus() {
        for (eventType in EventType.entries) {
            config.set("events.${eventType.name.lowercase()}", eventStatus[eventType] ?: false)
        }
        config.save()
    }

    fun enableEvent(eventType: EventType) {
        eventStatus[eventType] = true
        if (config.getBoolean("autoSaveEventChanges")!!) {
            saveEventStatus()
        }
    }

    fun disableEvent(eventType: EventType) {
        eventStatus[eventType] = false
        if (config.getBoolean("autoSaveEventChanges")!!) {
            saveEventStatus()
        }
    }

    fun isEventEnabled(eventType: EventType): Boolean {
        return eventStatus[eventType] ?: false
    }

    fun toggleEvent(eventType: EventType) {
        eventStatus[eventType] = !(eventStatus[eventType] ?: false)
        if (config.getBoolean("autoSaveEventChanges")!!) {
            saveEventStatus()
        }
    }
}