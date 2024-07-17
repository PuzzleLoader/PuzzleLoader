package dev.crmodders.puzzle.core.registries.exception;

import dev.crmodders.puzzle.core.registries.IRegistry;
import org.jetbrains.annotations.NotNull;

public class AlreadyFrozenException extends RuntimeException {
    public AlreadyFrozenException(@NotNull IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is already frozen");
    }
}
