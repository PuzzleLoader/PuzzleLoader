package com.github.puzzle.core.registries.exception;

import com.github.puzzle.core.registries.IRegistry;
import org.jetbrains.annotations.NotNull;

public class AlreadyFrozenException extends RuntimeException {
    public AlreadyFrozenException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is already frozen");
    }
}
