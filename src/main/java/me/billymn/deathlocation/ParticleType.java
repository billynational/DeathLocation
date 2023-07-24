package me.billymn.deathlocation;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public enum ParticleType {
    FLAME("Flame"),
    HEART("Heart"),
    ENCHANTMENT("Enchantment");
    private static Map<UUID, ParticleType> playerParticleTypes = new HashMap<>();

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

    // Method to set the particle type for the specified player
    public static void setParticleTypeForPlayer(UUID playerUUID, ParticleType particleType) {
        playerParticleTypes.put(playerUUID, particleType);
    }

    // Method to get the particle type for the specified player
    public static ParticleType getParticleTypeForPlayer(UUID playerUUID) {
        return playerParticleTypes.getOrDefault(playerUUID, ParticleType.FLAME);
    }
}