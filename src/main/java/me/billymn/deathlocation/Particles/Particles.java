package me.billymn.deathlocation.Particles;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public interface Particles {
    boolean onCommand(CommandSender sender, Command cmd, String label, String[] args);
}
