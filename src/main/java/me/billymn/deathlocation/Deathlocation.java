package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.Location;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.text.MessageFormat;
import java.util.HashMap;
import java.util.Map;
import java.util.ResourceBundle;
import java.util.UUID;

public class Deathlocation extends JavaPlugin implements Listener {

    private Map<UUID, ParticleUtils> particleUtilsMap = new HashMap<>();
    private ResourceBundle defaultMessages;

    private HashMap<UUID, Location> deathLocations = new HashMap<>();
    private HashMap<UUID, Boolean> teleporting = new HashMap<>();

    public void onEnable() {
        // Initialize the ParticleUtils and ColorChangeGUI instances
        ParticleUtils particleUtils = ParticleUtils.getInstance();
        ColorChangeGUI colorChangeGUI = new ColorChangeGUI(this, particleUtils);

        // Register event listeners and commands
        getServer().getPluginManager().registerEvents(new TeleportParticleListener(particleUtils), this);
        getServer().getPluginManager().registerEvents(this, this);
        getServer().getPluginManager().registerEvents(colorChangeGUI, this);
        getCommand("changeparticle").setExecutor(new ChangeParticleCommand(particleUtils));
        getCommand("opencolorgui").setExecutor(new OpenColorGUICommand(colorChangeGUI));
        getCommand("setcolors").setExecutor(new SetColorsCommand(this, particleUtils, getConfig()));
        getCommand("reloaddeathlocation").setExecutor(new ReloadDeathLocationCommand(this));

        // Load configurations
        defaultMessages = ResourceBundle.getBundle("messages/messages_en");

        // Load particle and chat colors for online players
        for (Player player : getServer().getOnlinePlayers()) {
            UUID playerUUID = player.getUniqueId();
            ParticleUtils playerParticleUtils = ParticleUtils.getInstance();
            particleUtilsMap.put(playerUUID, playerParticleUtils);
            Color particleColor = playerParticleUtils.getParticleColorForPlayer(playerUUID);
            ChatColor chatColor = playerParticleUtils.getChatColorForPlayer(playerUUID);
            playerParticleUtils.setParticleColorForPlayer(playerUUID, particleColor);
            playerParticleUtils.setChatColorForPlayer(playerUUID, chatColor);
        }
    }

    public void onDisable() {
        // Save configurations
        ParticleUtils particleUtils = ParticleUtils.getInstance();
        particleUtils.saveConfig(getConfig());
        ParticleSelector.savePlayerColors(getConfig());
    }

    public String getLocalized(String key, Object... args) {
        String message = defaultMessages.getString(key);
        return MessageFormat.format(message, args);
    }

    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("back") && sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();
            if (deathLocations.containsKey(playerUUID)) {
                Location deathLocation = deathLocations.get(playerUUID);
                HashMap<UUID, Boolean> teleporting = this.teleporting;

                if (teleporting.containsKey(playerUUID) && teleporting.get(playerUUID)) {
                    player.sendMessage(getChatColor(playerUUID) + "You are already teleporting back.");
                    return true;
                }

                player.sendMessage(getChatColor(playerUUID) + "Teleporting back to death location in 5 seconds...");
                ParticleUtils.spawnFlameParticles(player, teleporting, deathLocation, ParticleUtils.getInstance());
                startTeleportationCountdown(player, deathLocation, teleporting);
                return true;
            } else {
                player.sendMessage(getChatColor(playerUUID) + "No death location found.");
                return true;
            }
        } else if (cmd.getName().equalsIgnoreCase("setcolors") && sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if (args.length == 2) {
                ChatColor chatColor = null;
                Color particleColor = null;

                try {
                    chatColor = ChatColor.valueOf(args[0].toUpperCase());
                    particleColor = ParticleColorOption.valueOf(args[1].toUpperCase()).getColor();
                } catch (IllegalArgumentException e) {
                    // Invalid arguments
                }

                if (chatColor != null && particleColor != null) {
                    ParticleUtils.getInstance().setChatColorForPlayer(playerUUID, chatColor);
                    ParticleUtils.getInstance().setParticleColorForPlayer(playerUUID, particleColor);
                    player.sendMessage(getChatColor(playerUUID) + "Colors updated successfully.");
                    return true;
                }
            }

            // Invalid arguments, show usage
            player.sendMessage(ChatColor.RED + "Usage: /setcolors <chatColor> <particleColor>");
            player.sendMessage(ChatColor.RED + "Available chatColors: " + ChatColor.WHITE + "GREEN, RED, BLUE");
            player.sendMessage(ChatColor.RED + "Available particleColors: " + ChatColor.WHITE + "GREEN, RED, BLUE");
            return true;
        }

        return false;
    }

    public void startTeleportationCountdown(Player player, Location deathLocation, HashMap<UUID, Boolean> teleporting) {
        ParticleUtils.getInstance().spawnFlameParticles(player, teleporting, deathLocation, ParticleUtils.getInstance());
        ParticleUtils.getInstance().startTeleportationCountdown(player, deathLocation, teleporting, ParticleUtils.getInstance());
    }

    public ChatColor getChatColor(UUID playerUUID) {
        ParticleUtils particleUtils = particleUtilsMap.get(playerUUID);
        if (particleUtils == null) {
            particleUtils = ParticleUtils.getInstance();
            particleUtilsMap.put(playerUUID, particleUtils);
        }
        return particleUtils.getChatColorForPlayer(playerUUID);
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player player = event.getEntity();
        deathLocations.put(player.getUniqueId(), player.getLocation());
    }
}
