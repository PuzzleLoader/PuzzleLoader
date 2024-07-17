package dev.crmodders.puzzle.core.registries.exception;

import dev.crmodders.puzzle.core.registries.IRegistry;
import org.jetbrains.annotations.NotNull;

public class NotWritableException extends RuntimeException {
    public NotWritableException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is not writable");
    }
}
