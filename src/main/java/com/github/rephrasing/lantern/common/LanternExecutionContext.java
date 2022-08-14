package com.github.rephrasing.lantern.common;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public final class LanternExecutionContext {

    private final LanternCommand command;
    private final LanternCommandSender sender;
    private final List<String> args;

    public LanternExecutionContext(LanternCommand command, LanternCommandSender sender, String[] args) {
        this.command = command;
        this.sender = sender;
        this.args = Arrays.asList(args);
    }

}
