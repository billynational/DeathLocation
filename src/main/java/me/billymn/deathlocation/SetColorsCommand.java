package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.Color;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.UUID;

public class SetColorsCommand implements CommandExecutor {
    private final JavaPlugin plugin;
    private final ParticleUtils particleUtils;
    private final FileConfiguration config;

    public SetColorsCommand(JavaPlugin plugin, ParticleUtils particleUtils, FileConfiguration config) {
        this.plugin = plugin;
        this.particleUtils = particleUtils;
        this.config = config;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("setcolors") && sender instanceof Player) {
            Player player = (Player) sender;
            UUID playerUUID = player.getUniqueId();

            if (args.length != 2) {
                player.sendMessage(ChatColor.RED + "Invalid command format. Usage: /setcolors <particle_color> <chat_color>");
                return true;
            }

            String particleColorString = args[0];
            String chatColorString = args[1];

            // Parse particleColorString and chatColorString using ColorUtils methods
            Color particleColor = ColorUtils.parseColorString(particleColorString);
            ChatColor chatColor = ColorUtils.parseChatColorString(chatColorString);

            if (particleColor == null) {
                player.sendMessage(ChatColor.RED + "Invalid particle color specified.");
                return true;
            }

            ParticleUtils.getInstance().setParticleColorForPlayer(playerUUID, particleColor);
            ParticleUtils.getInstance().setChatColorForPlayer(playerUUID, chatColor);
            ParticleUtils.getInstance().saveConfig(plugin.getConfig());

            player.sendMessage(ChatColor.GREEN + "Particle color and chat color updated successfully.");
            return true;
        }

        return false;
    }
}
