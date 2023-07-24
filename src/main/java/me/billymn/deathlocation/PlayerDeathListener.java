package me.billymn.deathlocation;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;

import java.util.HashMap;
import java.util.UUID;

public class PlayerDeathListener implements Listener {
    private HashMap<UUID, Location> deathLocations;
    private HashMap<UUID, Boolean> teleporting;

    public PlayerDeathListener(HashMap<UUID, Location> deathLocations, HashMap<UUID, Boolean> teleporting) {
        this.deathLocations = deathLocations;
        this.teleporting = teleporting;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        deathLocations.put(player.getUniqueId(), player.getLocation());
    }
}
