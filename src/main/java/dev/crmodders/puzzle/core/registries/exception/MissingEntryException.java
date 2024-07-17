package dev.crmodders.puzzle.core.registries.exception;

import dev.crmodders.puzzle.core.Identifier;
import dev.crmodders.puzzle.core.registries.IRegistry;
import org.jetbrains.annotations.NotNull;

public class MissingEntryException extends RuntimeException {
    public MissingEntryException(@NotNull IRegistry<?> registry, Identifier name) {
        super("Registry \"" + registry.identifier() + "\" does not contain \"" + name + "\"");
    }
}
