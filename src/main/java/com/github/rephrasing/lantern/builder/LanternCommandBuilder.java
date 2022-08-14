package com.github.rephrasing.lantern.builder;

import com.github.rephrasing.lantern.common.LanternCommand;
import com.github.rephrasing.lantern.common.LanternExecutionContext;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

public class LanternCommandBuilder {

    private final JavaPlugin plugin;
    private final String name;
    private List<String> aliases;

    private Predicate<LanternExecutionContext> permissionCheck;
    private Predicate<LanternExecutionContext> execution;
    private TabExecution<LanternExecutionContext, String, Integer> tabComplete;

    private String description, permissionMessage, usageMessage, cooldownMessage;
    private int cooldown;
    private boolean playerOnly;

    private LanternCommandBuilder(JavaPlugin plugin, String name) {
        this.plugin = plugin;
        this.name = name;
    }

    public static LanternCommandBuilder builder(JavaPlugin plugin, String name) {
        return new LanternCommandBuilder(plugin, name);
    }

    public LanternCommand build() {
        if (execution == null) throw new IllegalArgumentException("Cannot build a command without execution context");
        LanternCommand command = new LanternCommand(plugin, name) {
            @Override
            public boolean execute(LanternExecutionContext context) {
                return execution.test(context);
            }

            @Override
            public List<String> tabComplete(LanternExecutionContext context, String argUsed, int argSize) {
                return tabComplete != null ? tabComplete.apply(context, argUsed, argSize) : null;
            }
        };

        if (aliases != null) command.getAliases().addAll(aliases);
        if (permissionCheck != null) command.setPermissionCheck(permissionCheck);
        if (description != null) command.setDescription(description);
        if (permissionMessage != null) command.setPermissionMessage(permissionMessage);
        if (usageMessage != null) command.setUsageMessage(usageMessage);
        if (cooldownMessage != null) command.setCooldownMessage(cooldownMessage);
        if (cooldown > 0) command.setCooldown(cooldown);
        command.setPlayerOnly(playerOnly);

        return command;
    }

    public LanternCommandBuilder execution(Predicate<LanternExecutionContext> execute) {
        this.execution = execute;
        return this;
    }

    public LanternCommandBuilder tabComplete(TabExecution<LanternExecutionContext, String, Integer> tabComplete) {
        this.tabComplete = tabComplete;
        return this;
    }

    public LanternCommandBuilder permissionCheck(Predicate<LanternExecutionContext> permissionCheck) {
        this.permissionCheck = permissionCheck;
        return this;
    }

    public LanternCommandBuilder aliases(String... aliases) {
        if (this.aliases != null) {
            List<String> found = Arrays.stream(aliases).filter(alias -> this.aliases.contains(alias)).collect(Collectors.toList());
            if (!found.isEmpty()) throw new IllegalArgumentException("Attempted to add aliases " + found + " but they already exist");
        }
        this.aliases = new ArrayList<>();
        this.aliases.addAll(Arrays.asList(aliases));
        return this;
    }

    public LanternCommandBuilder description(String description) {
        this.description = description;
        return this;
    }

    public LanternCommandBuilder permissionMessage(String permissionMessage) {
        this.permissionMessage = permissionMessage;
        return this;
    }

    public LanternCommandBuilder usageMessage(String usageMessage) {
        this.usageMessage = usageMessage;
        return this;
    }

    public LanternCommandBuilder cooldownMessage(String message) {
        this.cooldownMessage = message;
        return this;
    }

    public LanternCommandBuilder cooldown(int cooldown) {
        this.cooldown = cooldown;
        return this;
    }

    public LanternCommandBuilder playerOnly(boolean playerOnly) {
        this.playerOnly = playerOnly;
        return this;
    }
}
