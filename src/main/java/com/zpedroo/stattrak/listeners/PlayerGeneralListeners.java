package com.zpedroo.stattrak.listeners;

import com.zpedroo.stattrak.utils.config.Messages;
import com.zpedroo.stattrak.utils.config.Settings;
import de.tr7zw.nbtapi.NBTItem;
import org.apache.commons.lang.StringUtils;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.List;
import java.util.concurrent.ThreadLocalRandom;

public class PlayerGeneralListeners implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onClick(InventoryClickEvent event) {
        if (event.getClickedInventory() == null) return;
        if (event.getCurrentItem() == null || event.getCurrentItem().getType().equals(Material.AIR)) return;
        if (event.getCursor() == null || event.getCursor().getType().equals(Material.AIR)) return;

        Player player = (Player) event.getWhoClicked();
        if (player.getGameMode().equals(GameMode.CREATIVE)) return;
        if (event.getClickedInventory().getType() != InventoryType.PLAYER) return;

        ItemStack currentItem = event.getCurrentItem();
        ItemStack cursor = event.getCursor();

        NBTItem cursorNBT = new NBTItem(cursor);
        if (!cursorNBT.hasKey("StatTrak")) return;

        event.setCancelled(true);

        ItemStack item = currentItem.clone();
        NBTItem currentNBT = new NBTItem(item);
        if (currentNBT.hasKey("StatTrakKills")) {
            player.sendMessage(Messages.ALREADY_HAS_STATTRAK);
            return;
        }

        List<String> compatibleItems = Settings.COMPATIBLE_ITEMS;
        if (!compatibleItems.contains(item.getType().toString())) {
            player.sendMessage(Messages.INCOMPATIBLE_ITEM);
            return;
        }

        cursor.setAmount(cursor.getAmount() - 1);
        event.setCursor(cursor);

        if (ThreadLocalRandom.current().nextDouble(0, 100) > Settings.APPLY_CHANCE) {
            player.playSound(player.getLocation(), Sound.EXPLODE, 10f, 10f);
            return;
        }

        currentNBT.setInteger("StatTrakKills", 0);

        String statTrakDisplay = Settings.DISPLAY;
        ItemMeta meta = currentNBT.getItem().getItemMeta();

        String displayName = meta.hasDisplayName() ? meta.getDisplayName() : item.getType().toString();
        meta.setDisplayName(StringUtils.replaceEach(displayName + " " + statTrakDisplay, new String[]{
                "{kills}"
        }, new String[]{
                String.valueOf(0)
        }));

        currentNBT.getItem().setItemMeta(meta);

        player.getInventory().setItem(event.getSlot(), currentNBT.getItem());
        player.playSound(player.getLocation(), Sound.ORB_PICKUP, 0.2f, 10f);
    }

    @EventHandler(priority = EventPriority.LOWEST, ignoreCancelled = true)
    public void onPlace(BlockPlaceEvent event) {
        if (event.getItemInHand() == null || event.getItemInHand().getType().equals(Material.AIR)) return;

        NBTItem nbt = new NBTItem(event.getItemInHand());
        if (!nbt.hasKey("StatTrak")) return;

        event.setCancelled(true);
    }
}