package me.billymn.deathlocation.Particles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Particle implements Particles {

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        boolean result = false;
        if (cmd.getName().equalsIgnoreCase("back")) {
            if (sender instanceof Player) {
                Player player = (Player) sender;
                player.getWorld().spawnParticle(org.bukkit.Particle.DRIP_LAVA, player.getLocation(), 10, 0.1, 0.1, 0.1, 0.1);
                result = true;
            } else {
                sender.sendMessage("This command can only be used by players.");
            }
        }
        return result;
    }
}
