package com.zpedroo.stattrak.commands;

import com.zpedroo.stattrak.utils.config.Item;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

public class StatTrakCmd implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length > 0) {
            switch (args[0].toUpperCase()) {
                case "GIVE":
                    if (args.length < 3) break;

                    Player target = Bukkit.getPlayer(args[1]);
                    if (target == null) break;

                    int amount = 0;
                    try {
                        amount = Integer.parseInt(args[2]);
                    } catch (Exception ex) {
                        // ignore
                    }

                    if (amount <= 0) break;

                    ItemStack item = Item.getStatTrakItem();
                    item.setAmount(amount);

                    if (target.getInventory().firstEmpty() != -1) {
                        target.getInventory().addItem(item);
                    } else {
                        target.getWorld().dropItemNaturally(target.getLocation(), item);
                    }
                    return true;
            }
        }

        return false;
    }
}