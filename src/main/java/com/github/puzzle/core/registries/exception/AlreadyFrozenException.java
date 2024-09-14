package com.github.puzzle.core.registries.exception;

import org.jetbrains.annotations.NotNull;
import com.github.puzzle.core.registries.IRegistry;

public class AlreadyFrozenException extends RuntimeException {
    public AlreadyFrozenException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is already frozen");
    }
}
