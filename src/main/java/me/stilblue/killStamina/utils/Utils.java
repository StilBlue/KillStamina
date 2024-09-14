package me.stilblue.killStamina.utils;

import me.clip.placeholderapi.PlaceholderAPI;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Utils {
    /**
     * Translate hexadecimal colors
     *
     * @param message
     * @return
     */
    public static String hex(String message) {
        Pattern pattern = Pattern.compile("#[a-fA-F0-9]{6}");
        Matcher matcher = pattern.matcher(message);
        while (matcher.find()) {
            String hexCode = message.substring(matcher.start(), matcher.end());
            String replaceSharp = hexCode.replace('#', 'x');

            char[] ch = replaceSharp.toCharArray();
            StringBuilder builder = new StringBuilder();
            for (char c : ch) {
                builder.append("&" + c);
            }

            message = message.replace(hexCode, builder.toString());
            matcher = pattern.matcher(message);
        }
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static void sendActionBar(Player p, String message) {
        TextComponent text_component = new TextComponent(message);
        p.spigot().sendMessage(ChatMessageType.ACTION_BAR, text_component);
    }

    /**
     * Get the sign symbol of the value
     * Return an empty string if it's negative to prevent duplicating issue
     * @param value
     * @return
     */
    public static String getSignSymbol(double value)
    {
        if(value < 0)
            return "";
        else
            return "+";
    }

    /**
     * Used to call any event
     * @param e
     */
    public static void callEvent(Event e) {
        Bukkit.getPluginManager().callEvent(e);
    }

    /**
     * Translate the string to hex color code
     * @param startTag
     * @param endTag
     * @param message
     * @return
     */
    public static String translateHexColorCodes(String startTag, String endTag, String message)
    {
        char COLOR_CHAR = ChatColor.COLOR_CHAR;
        final Pattern hexPattern = Pattern.compile(startTag + "([A-Fa-f0-9]{6})" + endTag);
        Matcher matcher = hexPattern.matcher(message);
        StringBuffer buffer = new StringBuffer(message.length() + 4 * 8);
        while (matcher.find())
        {
            String group = matcher.group(1);
            matcher.appendReplacement(buffer, COLOR_CHAR + "x"
                    + COLOR_CHAR + group.charAt(0) + COLOR_CHAR + group.charAt(1)
                    + COLOR_CHAR + group.charAt(2) + COLOR_CHAR + group.charAt(3)
                    + COLOR_CHAR + group.charAt(4) + COLOR_CHAR + group.charAt(5)
            );
        }
        return matcher.appendTail(buffer).toString();
    }

    public static String applyPlaceholders(UUID uuid, String msg)
    {
        if(!Bukkit.getPluginManager().isPluginEnabled("PlaceholderAPI")) {
            return msg;
        }
        if(uuid == null)
            uuid = UUID.randomUUID();
        Player p = Bukkit.getPlayer(uuid);
        if(p == null)
            return PlaceholderAPI.setPlaceholders(Bukkit.getOfflinePlayer(uuid), msg);
        return PlaceholderAPI.setPlaceholders(p, msg);
    }

    public static String roundToString(double num, int decimal) {
        return Double.toString(round(num, decimal));
    }

    public static double round(double num, int decimal) {
        double tens = Math.pow(10, decimal);
        return Math.round(num * tens) / tens;
    }
}