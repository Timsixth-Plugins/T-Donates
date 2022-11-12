package pl.timsixth.donates.util;

import lombok.experimental.UtilityClass;
import me.clip.placeholderapi.PlaceholderAPI;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

@UtilityClass
public class ChatUtil {

    public static String chatColor(String text) {
        return ChatColor.translateAlternateColorCodes('&', text);
    }

    public static List<String> chatColor(List<String> stringList) {
        List<String> strings = new ArrayList<>();
        for (String text : stringList) {
            String msg = ChatColor.translateAlternateColorCodes('&', text);
            strings.add(msg);
        }
        return strings;
    }

    public static void sendMessage(Player player, String message) {
        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") == null) {
            player.sendMessage(message);
            return;
        }
        player.sendMessage(PlaceholderAPI.setPlaceholders(player, message));
    }
}
