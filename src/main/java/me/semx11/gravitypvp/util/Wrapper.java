package me.semx11.gravitypvp.util;

import net.md_5.bungee.api.ChatColor;
import org.bukkit.Bukkit;

public class Wrapper {

    private Wrapper() {
    }

    public static String format(String msg, Object... args) {
        return ChatColor.translateAlternateColorCodes('&',
                args.length < 1 ? msg : String.format(msg, args));
    }

    public static int getOnlineCount() {
        return Bukkit.getOnlinePlayers().size();
    }

}
