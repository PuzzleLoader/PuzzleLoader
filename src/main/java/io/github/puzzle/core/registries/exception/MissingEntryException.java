package io.github.puzzle.core.registries.exception;

import io.github.puzzle.core.Identifier;
import io.github.puzzle.core.registries.IRegistry;
import org.jetbrains.annotations.NotNull;

public class MissingEntryException extends RuntimeException {
    public MissingEntryException(@NotNull IRegistry<?> registry, Identifier name) {
        super("Registry \"" + registry.identifier() + "\" does not contain \"" + name + "\"");
    }
}
