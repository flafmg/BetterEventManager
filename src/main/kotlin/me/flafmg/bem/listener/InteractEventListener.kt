package me.flafmg.bem.listener

import me.flafmg.bem.manager.BypassManager
import me.flafmg.bem.manager.ConfigManager
import me.flafmg.bem.manager.EventManager
import me.flafmg.bem.manager.EventType
import me.flafmg.bem.util.sendEventBlockedMessages
import org.bukkit.Material
import org.bukkit.entity.EntityType
import org.bukkit.event.EventHandler
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerInteractEntityEvent
import org.bukkit.event.player.PlayerInteractEvent
import java.util.UUID

class InteractEventListener(private val messagesConfig: ConfigManager) : Listener {

    private val prohibitedMaterials = setOf(
        Material.CHEST, Material.ENDER_CHEST, Material.FURNACE, Material.CRAFTING_TABLE, Material.ANVIL, Material.ENCHANTING_TABLE,
        Material.BREWING_STAND, Material.ITEM_FRAME, Material.LEVER, Material.TRIPWIRE_HOOK, Material.JUKEBOX
    )

    private val prohibitedEntityTypes = setOf(
        EntityType.ITEM_FRAME, EntityType.ARMOR_STAND, EntityType.VILLAGER, EntityType.MINECART, EntityType.BOAT,
        EntityType.WANDERING_TRADER, EntityType.LEASH_HITCH, EntityType.MINECART_CHEST
    )

    @EventHandler
    fun onPlayerInteract(event: PlayerInteractEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId

        val block = event.clickedBlock
        if (block != null) {
            val type = block.type
            if (type.name.contains("DOOR") || type.name.contains("TRAPDOOR") || type.name.contains("BUTTON") || type.name.contains("PRESSURE_PLATE") ||
                type.name.contains("SIGN") || type.name.contains("WALL_SIGN") || type.name.contains("FURNACE") || type.name.contains("CRAFTING_TABLE") ||
                type in prohibitedMaterials) {
                if (!EventManager.isEventEnabled(EventType.INTERACT) &&
                    !player.hasPermission("bettereventmanager.bypass.interact") &&
                    !BypassManager.hasBypass(EventType.INTERACT, playerId)) {

                    event.isCancelled = true
                    sendEventBlockedMessages(player, EventType.INTERACT, messagesConfig)
                }
            }
        }
    }

    @EventHandler
    fun onPlayerInteractEntity(event: PlayerInteractEntityEvent) {
        val player = event.player
        val playerId: UUID = player.uniqueId

        val entity = event.rightClicked
        if (entity != null && entity.type in prohibitedEntityTypes) {
            if (!EventManager.isEventEnabled(EventType.INTERACT) &&
                !player.hasPermission("bettereventmanager.bypass.interact") &&
                !BypassManager.hasBypass(EventType.INTERACT, playerId)) {

                event.isCancelled = true
                sendEventBlockedMessages(player, EventType.INTERACT, messagesConfig)
            }
        }
    }
}