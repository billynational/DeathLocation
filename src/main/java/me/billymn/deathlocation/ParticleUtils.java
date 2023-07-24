package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static me.billymn.deathlocation.ColorUtils.getColorString;
import static me.billymn.deathlocation.ColorUtils.parseColorString;

public class ParticleUtils {
    private static ParticleUtils instance = null;

    // Add the ParticleType enum
    public enum ParticleType {
        FLAME("Flame"),
        HEART("Heart"),
        ENCHANTMENT("Enchantment");

        private final String displayName;

        ParticleType(String displayName) {
            this.displayName = displayName;
        }

        public String getDisplayName() {
            return displayName;
        }

        public static ParticleType fromDisplayName(String displayName) {
            for (ParticleType type : values()) {
                if (type.displayName.equalsIgnoreCase(displayName)) {
                    return type;
                }
            }
            return null;
        }

        public static String getAvailableDisplayNames() {
            StringBuilder builder = new StringBuilder();
            for (ParticleType type : values()) {
                builder.append(type.getDisplayName()).append(", ");
            }
            return builder.substring(0, builder.length() - 2);
        }
    }

    private Map<UUID, ParticleType> playerParticleTypes = new HashMap<>();

    public void setParticleTypeForPlayer(UUID playerUUID, ParticleType particleType) {
        playerParticleTypes.put(playerUUID, particleType);
    }

    public ParticleType getParticleTypeForPlayer(UUID playerUUID) {
        return playerParticleTypes.getOrDefault(playerUUID, ParticleType.FLAME);
    }


    private Map<UUID, Color> particleColors = new HashMap<>();
    private Map<UUID, ChatColor> chatColors = new HashMap<>();

    private Color particleColor = Color.BLUE;
    private ChatColor chatColor = ChatColor.LIGHT_PURPLE;


    private ParticleUtils() {

    }

    public void setParticleColorForPlayer(UUID playerUUID, Color color) {
        particleColors.put(playerUUID, color);
    }

    public void setChatColorForPlayer(UUID playerUUID, ChatColor color) {
        chatColors.put(playerUUID, color);
    }

    public Color getParticleColorForPlayer(UUID playerUUID) {
        Color playerColor = particleColors.get(playerUUID);
        return playerColor != null ? playerColor : particleColor;
    }

    public ChatColor getChatColorForPlayer(UUID playerUUID) {
        ChatColor playerColor = chatColors.get(playerUUID);
        return playerColor != null ? playerColor : chatColor;
    }

    public ChatColor getDefaultChatColor() {
        return chatColor;
    }

    public static ParticleUtils getInstance() {
        if (instance == null) {
            instance = new ParticleUtils();
        }
        return instance;
    }
    // Other existing methods...


    public void startTeleportationCountdown(final Player player, final Location deathLocation, final HashMap<UUID, Boolean> teleporting, ParticleUtils particleUtils) {
        (new BukkitRunnable() {
            int countdown = 5;

            public void run() {
                if (this.countdown > 1) {
                    ParticleUtils.spawnTeleportParticles(deathLocation, player);
                    ParticleUtils.spawnFlameParticles(player, teleporting, deathLocation, particleUtils); // Pass 'player' to the method
                    player.sendMessage(particleUtils.getChatColorForPlayer(player.getUniqueId()) + "Teleporting back in " + this.countdown + " seconds...");
                } else {
                    player.teleport(deathLocation);
                    player.sendMessage(particleUtils.getChatColorForPlayer(player.getUniqueId()) + "Teleported back to death location.");
                    teleporting.put(player.getUniqueId(), false);
                    ParticleUtils.spawnTeleportParticles(deathLocation, player); // Pass 'player' to the method
                    this.cancel();
                }

                --this.countdown;
            }
        }).runTaskTimer(JavaPlugin.getPlugin(Deathlocation.class), 0L, 20L);
        teleporting.put(player.getUniqueId(), true);
    }

    public static void spawnFlameParticles(Player player, Map<UUID, Boolean> teleporting, Location deathLocation, ParticleUtils particleUtils) {
        ParticleType particleType = particleUtils.getParticleTypeForPlayer(player.getUniqueId());
        // Method implementation..
    // Rest of the code...
        final int radius = 2;
        final double offset = Math.PI; // Offset for the second helix

        new BukkitRunnable() {
            double locationY = 0.0;

            public void run() {
                ParticleUtils particleUtils = ParticleUtils.getInstance();
                if (!(teleporting.getOrDefault(player.getUniqueId(), false))) {
                    this.cancel();
                } else {
                    // Use the player-specific particle color here
                    Color particleColor = particleUtils.getParticleColorForPlayer(player.getUniqueId());

                    // First helix
                    double x1 = radius * Math.cos(locationY);
                    double z1 = radius * Math.sin(locationY);
                    player.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(x1, locationY, z1), 50, new Particle.DustOptions(particleColor, 1.0F));

                    // Second helix with offset
                    double x2 = radius * Math.cos(locationY + offset);
                    double z2 = radius * Math.sin(locationY + offset);
                    player.spawnParticle(Particle.REDSTONE, player.getEyeLocation().add(x2, locationY, z2), 50, new Particle.DustOptions(particleColor, 1.0F));

                    locationY += 0.1;
                    if (locationY >= 6.283185307179586) {
                        this.cancel();
                    }
                }
            }
        }.runTaskTimer(JavaPlugin.getPlugin(Deathlocation.class), 0L, 1L);
    }

    public static void spawnTeleportParticles(Location location, Player player) {
        int particleCount = 100;
        double particleSpread = 0.5;
        ParticleUtils particleUtils = ParticleUtils.getInstance(); // Get the ParticleUtils instance

        // Use the player-specific particle color here
        Color particleColor = particleUtils.getParticleColorForPlayer(player.getUniqueId());

        for (int i = 0; i < particleCount; ++i) {
            double angle = 6.283185307179586 * (double) i / (double) particleCount;
            double x = particleSpread * Math.cos(angle);
            double z = particleSpread * Math.sin(angle);
            location.getWorld().spawnParticle(Particle.REDSTONE, location.getX() + x, location.getY() + 0.5, location.getZ() + z, 0, 0.0, 0.0, 0.0, new Particle.DustOptions(particleColor, 1.0F));
        }
    }

    public void loadConfig(FileConfiguration config) {
        // Existing code for loading default colors...

        String chatColorString = config.getString("chat-color", "LIGHT_PURPLE");

        ChatColor parsedChatColor;
        try {
            parsedChatColor = ChatColor.valueOf(chatColorString.toUpperCase());
        } catch (IllegalArgumentException var6) {
            parsedChatColor = ChatColor.LIGHT_PURPLE;
        }

        chatColor = parsedChatColor;

        // Load player-specific colors from config
        ConfigurationSection playerColorsSection = config.getConfigurationSection("players-colors");
        if (playerColorsSection != null) {
            for (String playerUUIDString : playerColorsSection.getKeys(false)) {
                UUID playerUUID = UUID.fromString(playerUUIDString);
                String playerParticleColorString = playerColorsSection.getString(playerUUIDString + ".particle-color");
                String playerChatColorString = playerColorsSection.getString(playerUUIDString + ".chat-color");
                Color playerParticleColor = parseColorString(playerParticleColorString);
                ChatColor playerChatColor = ChatColor.valueOf(playerChatColorString.toUpperCase());
                particleColors.put(playerUUID, playerParticleColor);
                chatColors.put(playerUUID, playerChatColor);
            }
        }
    }

    public void setColors(Color particleColor, ChatColor chatColor) {
        this.particleColor = particleColor;
        this.chatColor = chatColor;
    }

    public Color getParticleColor() {
        return particleColor;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public void saveConfig(FileConfiguration config) {
        // Save the default particle and chat colors
        config.set("particle-color", getColorString(particleColor));
        config.set("chat-color", chatColor.name());

        // Save the particle and chat colors for individual players
        ConfigurationSection playerColorsSection = config.createSection("players-colors");
        for (Map.Entry<UUID, Color> entry : particleColors.entrySet()) {
            UUID playerUUID = entry.getKey();
            Color color = entry.getValue();
            String playerParticleColorString = getColorString(color);
            config.set("players-colors." + playerUUID + ".particle-color", playerParticleColorString);
        }

        for (Map.Entry<UUID, ChatColor> entry : chatColors.entrySet()) {
            UUID playerUUID = entry.getKey();
            ChatColor color = entry.getValue();
            config.set("players-colors." + playerUUID + ".chat-color", color.name());
        }

        try {
            File configFile = new File(((JavaPlugin) JavaPlugin.getPlugin(Deathlocation.class)).getDataFolder(), "config.yml");
            config.save(configFile);
            System.out.println("Config file saved at: " + configFile.getAbsolutePath());
        } catch (IOException var2) {
            var2.printStackTrace();
        }
    }



}
