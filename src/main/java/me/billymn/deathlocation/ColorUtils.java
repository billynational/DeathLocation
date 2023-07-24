package me.billymn.deathlocation;

import org.bukkit.ChatColor;
import org.bukkit.Color;

public class ColorUtils {

    public static Color getColorByName(String colorName) {
        colorName = colorName.toUpperCase();
        switch (colorName) {
            case "WHITE":
                return Color.WHITE;
            case "BLACK":
                return Color.BLACK;
            case "RED":
                return Color.RED;
            case "GREEN":
                return Color.GREEN;
            case "BLUE":
                return Color.BLUE;
            // Add more color mappings as needed
            default:
                return null;
        }
    }

    public static String getColorString(Color color) {
        int red = color.getRed();
        int green = color.getGreen();
        int blue = color.getBlue();
        return red + "," + green + "," + blue;
    }
    public static Color parseColorString(String colorString) {
        String[] rgbValues = colorString.split(",");
        if (rgbValues.length == 3) {
            try {
                int red = Integer.parseInt(rgbValues[0]);
                int green = Integer.parseInt(rgbValues[1]);
                int blue = Integer.parseInt(rgbValues[2]);
                return Color.fromRGB(red, green, blue);
            } catch (NumberFormatException ignored) {
                // Handle invalid color strings
            }
        }
        // Return a default color if parsing fails
        return Color.BLUE;
    }

    public static ChatColor parseChatColorString(String chatColorString) {
        try {
            return ChatColor.valueOf(chatColorString.toUpperCase());
        } catch (IllegalArgumentException ignored) {
            // Handle invalid chat color strings
        }
        // Return a default chat color if parsing fails
        return ChatColor.LIGHT_PURPLE;
    }
}
