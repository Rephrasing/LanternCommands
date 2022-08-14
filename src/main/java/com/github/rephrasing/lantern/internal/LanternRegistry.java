package com.github.rephrasing.lantern.internal;

import com.github.rephrasing.lantern.common.LanternCommand;
import com.github.rephrasing.lantern.common.LanternCommandSender;
import com.github.rephrasing.lantern.common.LanternExecutionContext;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.PluginIdentifiableCommand;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.plugin.Plugin;

import java.util.HashMap;
import java.util.List;
import java.util.UUID;

public final class LanternRegistry extends Command implements PluginIdentifiableCommand {

    private final LanternCommand command;
    private final HashMap<UUID, Long> cooldown = new HashMap<>();

    protected LanternRegistry(LanternCommand command) {
        super(command.getName());
        this.command = command;

        setDescription(command.getDescription());
        setAliases(command.getAliases());
        setPermissionMessage(command.getPermissionMessage());
        setUsage(command.getUsageMessage());
    }

    @Override
    public boolean execute(CommandSender sender, String label, String[] args) {
        if (!command.getPlugin().isEnabled()) return false;

        LanternExecutionContext context = new LanternExecutionContext(command, new LanternCommandSender(sender), args);

        if (!hasPermission(context)) {
            sender.sendMessage(command.getPermissionMessage());
            return true;
        }

        if (command.isPlayerOnly() && !(sender instanceof Player)) {
            sender.sendMessage("This command is only executable by players");
            return true;
        }

        if (sender instanceof Player player) {
            if (command.hasCooldown()) {
                if (cooldown.containsKey(player.getUniqueId())) {
                    long secondsLeft = ((cooldown.get(player.getUniqueId()) / 1000) + command.getCooldown()) - (System.currentTimeMillis() / 1000);
                    if (secondsLeft > 0) {
                        String cooldownMessage = command.getCooldownMessage();
                        if (!cooldownMessage.contains("%seconds%")) throw new IllegalArgumentException("Did not find the placeholder [%seconds%] in cooldownMessage of command [/" + command.getName() + "]");
                        player.sendMessage(ChatColor.GRAY + "[" + ChatColor.WHITE + command.getPlugin().getName() + ChatColor.GRAY + "] " + cooldownMessage.replace("%seconds%", String.valueOf(secondsLeft)));
                        return true;
                    }
                }
                cooldown.put(player.getUniqueId(), System.currentTimeMillis());
            }
        }

        boolean success = command.execute(context);

        if (!success) {
            sender.sendMessage(command.getUsageMessage());
        }

        return success;
    }

    @Override
    public List<String> tabComplete(CommandSender sender, String alias, String[] args) throws IllegalArgumentException {
        LanternExecutionContext context = new LanternExecutionContext(command, new LanternCommandSender(sender), args);

        List<String> provided = command.tabComplete(context, alias, args.length - 1);
        if (provided != null) return provided;

        if (sender instanceof Player playerSender) {
            return Bukkit.getOnlinePlayers().stream().filter(playerSender::canSee).map(HumanEntity::getName).toList();
        }
        return Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).toList();
    }

    private boolean hasPermission(LanternExecutionContext context) {
        if (command.getPermissionCheck() == null) return true;
        return command.getPermissionCheck().test(context);
    }

    @Override
    public Plugin getPlugin() {
        return command.getPlugin();
    }
}
