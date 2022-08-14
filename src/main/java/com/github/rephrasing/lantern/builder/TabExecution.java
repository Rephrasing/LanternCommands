package com.github.rephrasing.lantern.builder;

import java.util.List;

@FunctionalInterface
public interface TabExecution<C, A, S> {

    List<String> apply(C context, A argUsed, S argSize);

}
