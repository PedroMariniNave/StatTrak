package com.zpedroo.stattrak.utils.config;

import com.zpedroo.stattrak.utils.FileUtils;
import com.zpedroo.stattrak.utils.builder.ItemBuilder;
import de.tr7zw.nbtapi.NBTItem;
import org.bukkit.inventory.ItemStack;

public class Item {

    private static final ItemStack statTrakItem = ItemBuilder.build(FileUtils.get().getFile(FileUtils.Files.CONFIG).get(), "Item").build();

    public static ItemStack getStatTrakItem() {
        NBTItem nbt = new NBTItem(statTrakItem.clone());
        nbt.addCompound("StatTrak");

        return nbt.getItem();
    }
}