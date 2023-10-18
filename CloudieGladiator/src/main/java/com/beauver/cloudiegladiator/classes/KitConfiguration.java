package com.beauver.cloudiegladiator.classes;

import com.beauver.cloudiegladiator.CloudieGladiator;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.Plugin;

import java.io.File;
import java.io.IOException;

public class KitConfiguration {
    private final Plugin plugin;
    private FileConfiguration config;
    private File configFile;

    public KitConfiguration(Plugin plugin) {
        this.plugin = plugin;
    }

    public void reloadConfig() {
        if (configFile == null) {
            configFile = new File(plugin.getDataFolder(), "kits.yml");
        }
        config = YamlConfiguration.loadConfiguration(configFile);
    }

    public FileConfiguration getConfig() {
        if (config == null) {
            reloadConfig();
        }
        return config;
    }

    public void saveConfig() {
        if (config == null || configFile == null) {
            return;
        }
        try {
            getConfig().save(configFile);
        } catch (IOException ex) {
            ex.printStackTrace();
        }
    }
}

