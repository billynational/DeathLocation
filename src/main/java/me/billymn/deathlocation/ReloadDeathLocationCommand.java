package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadDeathLocationCommand implements CommandExecutor {
    private final JavaPlugin plugin;

    public ReloadDeathLocationCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (cmd.getName().equalsIgnoreCase("reloaddeathlocation")) {
            if (!sender.hasPermission("deathlocation.reload")) {
                sender.sendMessage(ChatColor.RED + "You do not have permission to use this command.");
                return true;
            }

            plugin.reloadConfig(); // Reload the plugin configuration
            ParticleSelector.loadPlayerColors(plugin.getConfig()); // Reload any other resources as needed
            sender.sendMessage(ChatColor.GREEN + "Deathlocation plugin configuration reloaded.");
            return true;
        }
        return false;
    }
}
