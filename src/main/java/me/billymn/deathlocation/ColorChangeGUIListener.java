package me.billymn.deathlocation;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;

public class ColorChangeGUIListener implements Listener {

    private final ParticleUtils particleUtils;

    public ColorChangeGUIListener(ParticleUtils particleUtils) {
        this.particleUtils = particleUtils;
    }

    @EventHandler
    public void onColorGUIClick(InventoryClickEvent event) {
        // Event handling code...
    }

    // Add other event handlers as needed...
}
