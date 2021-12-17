package com.zpedroo.stattrak;

import com.zpedroo.stattrak.commands.StatTrakCmd;
import com.zpedroo.stattrak.listeners.StatTrakListeners;
import com.zpedroo.stattrak.listeners.PlayerGeneralListeners;
import com.zpedroo.stattrak.utils.FileUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandMap;
import org.bukkit.command.PluginCommand;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.util.List;

import static com.zpedroo.stattrak.utils.config.Settings.*;

public class VoltzStatTrak extends JavaPlugin {

    private static VoltzStatTrak instance;
    public static VoltzStatTrak get() { return instance; }

    public void onEnable() {
        instance = this;
        new FileUtils(this);

        registerListeners();
        registerCommand(COMMAND, ALIASES, PERMISSION, PERMISSION_MESSAGE, new StatTrakCmd());
    }

    private void registerCommand(String command, List<String> aliases, String permission, String permissionMessage, CommandExecutor executor) {
        try {
            Constructor<PluginCommand> constructor = PluginCommand.class.getDeclaredConstructor(String.class, Plugin.class);
            constructor.setAccessible(true);

            PluginCommand pluginCmd = constructor.newInstance(command, this);
            pluginCmd.setAliases(aliases);
            pluginCmd.setExecutor(executor);
            pluginCmd.setPermission(permission);
            pluginCmd.setPermissionMessage(permissionMessage);

            Field field = Bukkit.getPluginManager().getClass().getDeclaredField("commandMap");
            field.setAccessible(true);
            CommandMap commandMap = (CommandMap) field.get(Bukkit.getPluginManager());
            commandMap.register(getName().toLowerCase(), pluginCmd);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    private void registerListeners() {
        getServer().getPluginManager().registerEvents(new StatTrakListeners(), this);
        getServer().getPluginManager().registerEvents(new PlayerGeneralListeners(), this);
    }
}