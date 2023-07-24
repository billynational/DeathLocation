package me.billymn.deathlocation;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class ColorChangeGUI implements Listener {
    private final JavaPlugin plugin;
    private final ParticleUtils particleUtils;

    public ColorChangeGUI(JavaPlugin plugin, ParticleUtils particleUtils) {
        this.plugin = plugin;
        this.particleUtils = particleUtils;
    }

    public void openColorGUI(Player player) {
        Inventory colorGUI = Bukkit.createInventory(null, 9, "Color Options");

        colorGUI.setItem(0, createColorOptionItem(ChatColor.RED, Color.RED, "Red Particle"));
        colorGUI.setItem(1, createColorOptionItem(ChatColor.GREEN, Color.GREEN, "Green Particle"));
        colorGUI.setItem(2, createColorOptionItem(ChatColor.BLUE, Color.BLUE, "Blue Particle"));
        colorGUI.setItem(3, createColorOptionItem(ChatColor.WHITE, Color.WHITE, "White Particle"));
        colorGUI.setItem(4, createColorOptionItem(ChatColor.BLACK, Color.BLACK, "Black Particle"));
        colorGUI.setItem(5, createColorOptionItem(ChatColor.LIGHT_PURPLE, null, "Light Purple Particle"));
        colorGUI.setItem(6, createColorOptionItem(ChatColor.YELLOW, null, "Yellow Particle"));
        colorGUI.setItem(7, createColorOptionItem(ChatColor.AQUA, null, "Aqua Particle"));
        colorGUI.setItem(8, createColorOptionItem(ChatColor.GRAY, null, "Gray Particle"));

        player.openInventory(colorGUI);
    }

    private ItemStack createColorOptionItem(ChatColor chatColor, Color particleColor, String displayName) {
        ItemStack itemStack = new ItemStack(Material.PAPER);
        ItemMeta itemMeta = itemStack.getItemMeta();

        itemMeta.setDisplayName(chatColor + displayName);

        List<String> lore = new ArrayList<>();
        lore.add(ChatColor.GRAY + "Left-click to set particle color.");
        lore.add(ChatColor.GRAY + "Right-click to set chat color.");
        itemMeta.setLore(lore);
        itemStack.setItemMeta(itemMeta);

        return itemStack;
    }

    @EventHandler
    public void onColorOptionClick(InventoryClickEvent event) {
        Player player = (Player) event.getWhoClicked();
        if (!event.getView().getTitle().equals("Color Options")) {
            return;
        }

        event.setCancelled(true);
        ItemStack clickedItem = event.getCurrentItem();
        if (clickedItem == null || clickedItem.getType() == Material.AIR) {
            return;
        }

        String displayName = ChatColor.stripColor(clickedItem.getItemMeta().getDisplayName());

        // Check if it's a left-click or right-click
        if (event.getClick().isLeftClick()) {
            changePlayerParticleColor(player, displayName);
        } else if (event.getClick().isRightClick()) {
            changePlayerChatColor(player, displayName);
        }

        // Close the GUI after the color change
        player.closeInventory();
    }

    private void changePlayerParticleColor(Player player, String displayName) {
        Color particleColor = getColorFromDisplayName(displayName);
        if (particleColor != null) {
            UUID playerUUID = player.getUniqueId();
            ParticleUtils.getInstance().setParticleColorForPlayer(playerUUID, particleColor);
            player.sendMessage(ChatColor.GREEN + "Particle color updated successfully.");
        }
    }

    private void changePlayerChatColor(Player player, String displayName) {
        ChatColor chatColor = getChatColorFromDisplayName(displayName);
        if (chatColor != null) {
            UUID playerUUID = player.getUniqueId();
            ParticleUtils.getInstance().setChatColorForPlayer(playerUUID, chatColor);
            player.sendMessage(ChatColor.GREEN + "Chat color updated successfully.");
        }
    }

    private Color getColorFromDisplayName(String displayName) {
        switch (displayName) {
            case "Red Particle":
                return Color.RED;
            case "Green Particle":
                return Color.GREEN;
            case "Blue Particle":
                return Color.BLUE;
            case "White Particle":
                return Color.WHITE;
            case "Black Particle":
                return Color.BLACK;
            case "Light Purple Particle":
                return Color.PURPLE;
            case "Yellow Particle":
                return Color.YELLOW; // Yellow color
            case "Aqua Particle":
                return Color.AQUA; // Aqua color
            case "Gray Particle":
                return Color.GRAY;
            // Add more cases for other color options as needed
            default:
                return null;
        }
    }

    private ChatColor getChatColorFromDisplayName(String displayName) {
        switch (displayName) {
            case "Red Particle":
                return ChatColor.RED;
            case "Green Particle":
                return ChatColor.GREEN;
            case "Blue Particle":
                return ChatColor.BLUE;
            case "White Particle":
                return ChatColor.WHITE;
            case "Black Particle":
                return ChatColor.BLACK;
            case "Light Purple Particle":
                return ChatColor.LIGHT_PURPLE;
            case "Yellow Particle":
                return ChatColor.YELLOW; // Yellow color
            case "Aqua Particle":
                return ChatColor.AQUA; // Aqua color
            case "Gray Particle":
                return ChatColor.GRAY;
            // Add more cases for other color options as needed
            default:
                return null;
        }
    }
}
