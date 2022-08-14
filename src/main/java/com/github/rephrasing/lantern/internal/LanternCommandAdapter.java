package com.github.rephrasing.lantern.internal;

import com.github.rephrasing.lantern.common.LanternCommand;
import lombok.SneakyThrows;
import org.bukkit.command.CommandMap;
import org.bukkit.plugin.java.JavaPlugin;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;

public final class LanternCommandAdapter {

    private static final List<LanternCommand> registered = new ArrayList<>();

    public static void registerCommand(LanternCommand command) {
        if (registered.contains(command)) throw new IllegalArgumentException("Attempted to register a command that was already registered");
        CommandMap map = getCommandMap(command.getPlugin());

        if (map.getCommand(command.getName()) != null) throw new IllegalArgumentException("Attempted to register a command with the name [/" + command.getName() +"] because it already exists on the server (Likely registered by another plugin)");

        registered.add(command);
        map.register(command.getName(), new LanternRegistry(command));
    }

    public static void unregisterCommand(LanternCommand command) {
        if (!registered.contains(command)) throw new IllegalArgumentException("Attempted to unregister a command that was not registered");
        CommandMap map = getCommandMap(command.getPlugin());
        map.getCommand(command.getName()).unregister(map);
        registered.remove(command);
    }

    @SneakyThrows
    private static CommandMap getCommandMap(JavaPlugin plugin) {
        Field field = plugin.getServer().getClass().getDeclaredField("commandMap");
        field.setAccessible(true);
        return (CommandMap) field.get(plugin.getServer());
    }
}
