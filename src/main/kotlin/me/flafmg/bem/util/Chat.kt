package me.flafmg.bem.util

import net.md_5.bungee.api.ChatMessageType
import net.md_5.bungee.api.chat.TextComponent
import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

fun formatMessage(message: String, placeholders: MutableMap<String, String>): String {
    return placeholders.entries.fold(colorMessage(message)) { acc, entry ->
        acc.replace("{${entry.key}}", entry.value)
    }
}

fun colorMessage(message: String): String {
    return org.bukkit.ChatColor.translateAlternateColorCodes('&', message)
}

fun sendMessage(player: CommandSender, message: String?) {
    if (!message.isNullOrEmpty()) {
        player.sendMessage(formatMessage(message!!, mutableMapOf()))
    }
}

fun sendMessage(player: CommandSender, message: String?, placeholders: MutableMap<String, String>) {
    if (!message.isNullOrEmpty()) {
        player.sendMessage(formatMessage(message!!, placeholders))
    }
}

fun broadcastToPlayers(message: String?, players: List<Player>) {
    if (!message.isNullOrEmpty()) {
        for (player in players) {
            sendMessage(player, message!!)
        }
    }
}

fun broadcastToPlayers(message: String?, players: List<Player>, placeholders: MutableMap<String, String>) {
    if (!message.isNullOrEmpty()) {
        for (player in players) {
            sendMessage(player, message!!, placeholders)
        }
    }
}

fun sendTitle(player: Player, title: String?, subtitle: String?) {
    if (!title.isNullOrEmpty() && !subtitle.isNullOrEmpty()) {
        player.sendTitle(colorMessage(title!!), colorMessage(subtitle!!))
    }
}

fun sendTitle(player: Player, title: String?, subtitle: String?, placeholders: MutableMap<String, String>) {
    if (!title.isNullOrEmpty() && !subtitle.isNullOrEmpty()) {
        player.sendTitle(formatMessage(title!!, placeholders), formatMessage(subtitle!!, placeholders))
    }
}

fun broadcastTitle(title: String?, subtitle: String?, players: List<Player>) {
    if (!title.isNullOrEmpty() && !subtitle.isNullOrEmpty()) {
        for (player in players) {
            sendTitle(player, title!!, subtitle!!)
        }
    }
}

fun broadcastTitle(title: String?, subtitle: String?, players: List<Player>, placeholders: MutableMap<String, String>) {
    if (!title.isNullOrEmpty() && !subtitle.isNullOrEmpty()) {
        for (player in players) {
            sendTitle(player, title!!, subtitle!!, placeholders)
        }
    }
}

fun sendActionBar(player: Player, message: String?) {
    if (!message.isNullOrEmpty()) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(colorMessage(message!!)))
    }
}

fun sendActionBar(player: Player, message: String?, placeholders: MutableMap<String, String>) {
    if (!message.isNullOrEmpty()) {
        player.spigot().sendMessage(ChatMessageType.ACTION_BAR, TextComponent(formatMessage(message!!, placeholders)))
    }
}