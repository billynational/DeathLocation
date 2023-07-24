package me.billymn.deathlocation;

import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerTeleportEvent;

public class TeleportParticleListener implements Listener {
    private final ParticleUtils particleUtils;

    public TeleportParticleListener(ParticleUtils particleUtils) {
        this.particleUtils = particleUtils;
    }

    @EventHandler
    public void onPlayerTeleport(PlayerTeleportEvent event) {
        Player player = event.getPlayer();
        Location teleportLocation = event.getTo();
        ParticleUtils.spawnTeleportParticles(teleportLocation, player);
    }
}
