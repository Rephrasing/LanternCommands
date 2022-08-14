package com.github.rephrasing.lantern.common;

import lombok.Getter;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

@Getter
public final class LanternCommandSender {

    private final CommandSender instance;

    public LanternCommandSender(CommandSender instance) {
        this.instance = instance;
    }

    public boolean isPlayer() {
        return instance instanceof Player;
    }

    public Player asPlayer() {
        if (!isPlayer()) return null;
        return (Player) instance;
    }
}
