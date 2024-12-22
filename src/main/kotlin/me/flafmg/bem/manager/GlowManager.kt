package me.flafmg.bem.manager

import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.entity.Player
import org.bukkit.scoreboard.Scoreboard
import org.bukkit.scoreboard.Team
import java.util.UUID

object GlowManager {
    private val playerTeams: MutableMap<UUID, Team> = mutableMapOf()
    private val scoreboard: Scoreboard = Bukkit.getScoreboardManager()?.mainScoreboard ?: throw IllegalStateException("something went wrong on line 12 in GlowManager.kt oops :3")
    fun setGlow(player: Player, color: ChatColor?) {
        removeGlow(player)

        val teamName = "glow_${player.uniqueId}"
        val team = scoreboard.getTeam(teamName) ?: scoreboard.registerNewTeam(teamName)

        team.addEntry(player.name)
        team.setCanSeeFriendlyInvisibles(true)
        team.setAllowFriendlyFire(true)

        if (color != null) {
            team.color = color
        }

        playerTeams[player.uniqueId] = team
        player.isGlowing = true
    }

    fun removeGlow(player: Player) {
        val team = playerTeams.remove(player.uniqueId)
        team?.removeEntry(player.name)
        player.isGlowing = false
    }

    fun hasGlow(player: Player): Boolean {
        return playerTeams.containsKey(player.uniqueId)
    }
}