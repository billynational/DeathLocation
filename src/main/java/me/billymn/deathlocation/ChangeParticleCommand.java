package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.UUID;

public class ChangeParticleCommand implements CommandExecutor {

    private final ParticleUtils particleUtils;

    public ChangeParticleCommand(ParticleUtils particleUtils) {
        this.particleUtils = particleUtils;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage(ChatColor.RED + "This command can only be used by players.");
            return true;
        }

        Player player = (Player) sender;
        if (args.length != 1) {
            player.sendMessage(ChatColor.RED + "Usage: /changeparticle <particle>");
            return true;
        }

        ParticleUtils.ParticleType particleType = ParticleUtils.ParticleType.fromDisplayName(args[0]);
        if (particleType == null) {
            player.sendMessage(ChatColor.RED + "Invalid particle type. Available particle types: " + ParticleUtils.ParticleType.getAvailableDisplayNames());
            return true;
        }

        UUID playerUUID = player.getUniqueId();
        particleUtils.setParticleTypeForPlayer(playerUUID, particleType);
        player.sendMessage(ChatColor.GREEN + "Particle type updated to: " + particleType.getDisplayName());
        return true;
    }
}
