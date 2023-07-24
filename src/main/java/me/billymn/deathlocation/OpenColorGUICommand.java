package me.billymn.deathlocation;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class OpenColorGUICommand implements CommandExecutor {
    private final ColorChangeGUI colorChangeGUI;

    public OpenColorGUICommand(ColorChangeGUI colorChangeGUI) {
        this.colorChangeGUI = colorChangeGUI;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("Only players can use this command.");
            return true;
        }

        Player player = (Player) sender;
        colorChangeGUI.openColorGUI(player);
        return true;
    }
}
