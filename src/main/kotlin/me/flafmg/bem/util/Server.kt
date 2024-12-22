package me.flafmg.bem.util

import org.bukkit.Bukkit
import org.bukkit.entity.Player

fun getPlayer(nick: String): Player? {
    return Bukkit.getOnlinePlayers().firstOrNull { it.name == nick }
}
fun getOnlinePlayers(): List<Player> {
    return Bukkit.getOnlinePlayers().toList()
}
