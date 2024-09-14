package com.github.puzzle.core.registries.exception;

import org.jetbrains.annotations.NotNull;
import com.github.puzzle.core.registries.IRegistry;

public class NotReadableException extends RuntimeException {
    public NotReadableException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is not readable");
    }
}
