package com.github.puzzle.core.registries.exception;

import org.jetbrains.annotations.NotNull;
import com.github.puzzle.core.registries.IRegistry;

public class NotWritableException extends RuntimeException {
    public NotWritableException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is not writable");
    }
}
