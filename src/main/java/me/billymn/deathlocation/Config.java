package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

public class Config {
    private final JavaPlugin plugin;
    private FileConfiguration config;

    public Config(JavaPlugin plugin) {
        this.plugin = plugin;
        plugin.saveDefaultConfig();
        reloadConfig();
    }

    public void reloadConfig() {
        plugin.reloadConfig();
        config = plugin.getConfig();
    }

    public String getMessage(String path) {
        String message = config.getString(path);
        if (message == null) {
            plugin.getLogger().warning("Missing message in config: " + path);
            return ChatColor.RED + "Error: Message not found!";
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }
}
