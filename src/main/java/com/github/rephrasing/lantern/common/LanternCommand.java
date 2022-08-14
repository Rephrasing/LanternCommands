package com.github.rephrasing.lantern.common;

import lombok.Getter;
import lombok.Setter;
import org.bukkit.ChatColor;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Predicate;

@Getter
@Setter
public abstract class LanternCommand {

    private final JavaPlugin plugin;
    private final String name;

    private final List<String> aliases;

    private String description;
    private String permissionMessage;
    private String usageMessage;
    private String cooldownMessage;

    private int cooldown;

    private boolean playerOnly;

    private Predicate<LanternExecutionContext> permissionCheck;

    public LanternCommand(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
        this.aliases = new ArrayList<>();
        this.description = "";
        this.permissionMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + plugin.getName() + ChatColor.GRAY + "]" + ChatColor.RED + " You do not have permissions to use this command";
        this.usageMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + plugin.getName() + ChatColor.GRAY + "]" + ChatColor.RED + " /" + name;
        this.cooldown = -1;
        this.playerOnly = false;
        this.cooldownMessage = ChatColor.GRAY + "[" + ChatColor.WHITE + plugin.getName() + ChatColor.GRAY + "]" + ChatColor.RED + " You must wait " + ChatColor.YELLOW + "%seconds%s" + ChatColor.RED + " before using this command again.";
    }

    public abstract boolean execute(LanternExecutionContext context);

    public abstract List<String> tabComplete(LanternExecutionContext context, String argUsed, int argSize);

    public boolean hasCooldown() {
        return cooldown > 0;
    }

    public void addAlias(String alias) {
        if (aliases.contains(alias)) throw new IllegalArgumentException("Alias already exists");
        aliases.add(alias);
    }

    public boolean hasAliases() {
        return !aliases.isEmpty();
    }

    public boolean hasPermissionCheck() {
        return permissionMessage != null;
    }

}
