package me.flafmg.bem.manager

import me.flafmg.bem.util.*
import org.bukkit.configuration.file.FileConfiguration
import org.bukkit.configuration.file.YamlConfiguration
import org.bukkit.plugin.java.JavaPlugin
import java.io.File
import java.io.InputStreamReader

class ConfigManager(private val plugin: JavaPlugin, private val resourceFileName: String) {
    private val pluginFolder: File = plugin.dataFolder
    private val configFile: File = File(pluginFolder, resourceFileName)
    private val config: FileConfiguration

    init {
        checkAndLoadResource()
        config = YamlConfiguration.loadConfiguration(configFile)
    }

    private fun checkAndLoadResource() {
        val resourceStream = javaClass.classLoader.getResourceAsStream(resourceFileName)
            ?: throw IllegalArgumentException("Resource file '$resourceFileName' not found in resources folder.")

        if (!configFile.exists()) {
            pluginFolder.mkdirs()
            configFile.createNewFile()
            val reader = InputStreamReader(resourceStream)
            val defaultConfig = YamlConfiguration.loadConfiguration(reader)
            defaultConfig.save(configFile)
            successLog("Config file '$resourceFileName' created and loaded successfully.")
        } else {
            infoLog("Config file '$resourceFileName' already exists. Loading...")
        }
    }

    fun <T> get(key: String, suppressWarns: Boolean = false): T? {
        return if (config.contains(key)) {
            config.get(key) as? T ?: run {
                errLog("Value for key '$key' is not of expected type.")
                null
            }
        } else {
            if (!suppressWarns) {
                warnLog("Key '$key' not found in configuration.")
            }
            null
        }
    }

    fun set(key: String, value: Any) {
        config.set(key, value)
    }

    fun getString(key: String, suppressWarns: Boolean = false): String? = get(key, suppressWarns)
    fun getInt(key: String, suppressWarns: Boolean = false): Int? = get(key, suppressWarns)
    fun getBoolean(key: String, suppressWarns: Boolean = false): Boolean? = get(key, suppressWarns)
    fun getDouble(key: String, suppressWarns: Boolean = false): Double? = get(key, suppressWarns)
    fun getList(key: String, suppressWarns: Boolean = false): List<Any>? = get(key, suppressWarns)
    fun getStringList(key: String, suppressWarns: Boolean = false): List<String>? = get(key, suppressWarns)
    fun getIntList(key: String, suppressWarns: Boolean = false): List<Int>? = get(key, suppressWarns)
    fun getDoubleList(key: String, suppressWarns: Boolean = false): List<Double>? = get(key, suppressWarns)

    fun save() {
        try {
            config.save(configFile)
            successLog("Configuration file '$resourceFileName' saved successfully.")
        } catch (e: Exception) {
            errLog("Failed to save configuration file '$resourceFileName': ${e.message}")
        }
    }

    fun reload() {
        try {
            config.load(configFile)
            successLog("Configuration file '$resourceFileName' reloaded successfully.")
        } catch (e: Exception) {
            errLog("Failed to reload configuration file '$resourceFileName': ${e.message}")
        }
    }
}