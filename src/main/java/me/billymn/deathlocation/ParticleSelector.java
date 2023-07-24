package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class ParticleSelector {
    private static Map<UUID, Color> particleColors = new HashMap<>();
    private static Map<UUID, ChatColor> chatColors = new HashMap<>();

    public static void setParticleColorForPlayer(UUID playerUUID, Color color) {
        particleColors.put(playerUUID, color);
    }

    public static Color getParticleColorForPlayer(UUID playerUUID) {
        return particleColors.getOrDefault(playerUUID, Color.BLUE);
    }

    public static void setChatColorForPlayer(UUID playerUUID, ChatColor color) {
        chatColors.put(playerUUID, color);
    }

    public static ChatColor getChatColorForPlayer(UUID playerUUID) {
        return chatColors.getOrDefault(playerUUID, ChatColor.LIGHT_PURPLE);
    }

    // Other methods and functionalities can be added here.

    // Load player color preferences from the configuration file and store them in particleColors and chatColors maps.
    public static void loadPlayerColors(FileConfiguration config) {
        if (config.contains("players-colors")) {
            ConfigurationSection playersConfig = config.getConfigurationSection("players-colors");
            for (String playerId : playersConfig.getKeys(false)) {
                UUID playerUUID = UUID.fromString(playerId);
                String particleColorString = playersConfig.getString(playerId + ".particle-color");
                String chatColorString = playersConfig.getString(playerId + ".chat-color");
                Color particleColor = ColorUtils.parseColorString(particleColorString);
                ChatColor chatColor = ColorUtils.parseChatColorString(chatColorString);
                particleColors.put(playerUUID, particleColor);
                chatColors.put(playerUUID, chatColor);
            }
        }
    }

    // Save player color preferences from particleColors and chatColors maps to the configuration file.
    public static void savePlayerColors(FileConfiguration config) {
        ConfigurationSection playersConfig = config.createSection("players-colors");
        for (Map.Entry<UUID, Color> entry : particleColors.entrySet()) {
            UUID playerUUID = entry.getKey();
            Color particleColor = entry.getValue();
            ChatColor chatColor = chatColors.getOrDefault(playerUUID, ChatColor.LIGHT_PURPLE);
            String particleColorString = particleColor.getRed() + "," + particleColor.getGreen() + "," + particleColor.getBlue();
            playersConfig.set(playerUUID + ".particle-color", particleColorString);
            playersConfig.set(playerUUID + ".chat-color", chatColor.name());
        }
    }
}
