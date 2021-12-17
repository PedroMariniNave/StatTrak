package com.zpedroo.stattrak.utils.config;

import com.zpedroo.stattrak.utils.FileUtils;
import org.bukkit.ChatColor;

public class Messages {

    public static final String ALREADY_HAS_STATTRAK = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.already-has-stattrak"));

    public static final String INCOMPATIBLE_ITEM = getColored(FileUtils.get().getString(FileUtils.Files.CONFIG, "Messages.incompatible-item"));

    private static String getColored(String str) {
        return ChatColor.translateAlternateColorCodes('&', str);
    }
}