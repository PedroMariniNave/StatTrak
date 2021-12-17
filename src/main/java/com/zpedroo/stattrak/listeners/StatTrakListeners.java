package com.zpedroo.stattrak.listeners;

import com.zpedroo.stattrak.utils.config.Settings;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

public class StatTrakListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onPlayerDeath(PlayerDeathEvent event) {
        if (event.getEntity().getKiller() == null) return;

        Player killer = event.getEntity().getKiller();
        if (killer.getItemInHand() == null || killer.getItemInHand().getType().equals(Material.AIR)) return;

        ItemStack item = killer.getItemInHand().clone();
        NBTItem nbt = new NBTItem(item);
        if (!nbt.hasKey("StatTrakKills")) return;

        int kills = nbt.getInteger("StatTrakKills");
        int finalKills = kills + 1;
        nbt.setInteger("StatTrakKills", finalKills);

        ItemMeta meta = nbt.getItem().getItemMeta();
        String displayName = meta.hasDisplayName() ? StringUtils.replace(meta.getDisplayName(), StringUtils.replace(Settings.DISPLAY,
                "{kills}", String.valueOf(kills)), "") : item.getType().toString();
        meta.setDisplayName(StringUtils.replaceEach(displayName + Settings.DISPLAY, new String[]{
                "{kills}"
        }, new String[]{
                String.valueOf(finalKills)
        }));

        nbt.getItem().setItemMeta(meta);

        killer.setItemInHand(nbt.getItem());
    }
}