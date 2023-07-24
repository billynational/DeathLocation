package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public enum ParticleColorOption {
    GREEN(ChatColor.GREEN, Color.GREEN),
    RED(ChatColor.RED, Color.RED),
    BLUE(ChatColor.BLUE, Color.BLUE);

    private final ChatColor chatColor;
    private final Color color;

    ParticleColorOption(ChatColor chatColor, Color color) {
        this.chatColor = chatColor;
        this.color = color;
    }

    public ChatColor getChatColor() {
        return chatColor;
    }

    public Color getColor() {
        return color;
    }
}
