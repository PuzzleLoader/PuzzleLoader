package com.github.puzzle.core.registries.exception;

import org.jetbrains.annotations.NotNull;
import com.github.puzzle.core.registries.IRegistry;
import com.github.puzzle.core.util.Identifier;

public class MissingEntryException extends RuntimeException {
    public MissingEntryException(@NotNull IRegistry<?> registry, Identifier name) {
        super("Registry \"" + registry.identifier() + "\" does not contain \"" + name + "\"");
    }
}
