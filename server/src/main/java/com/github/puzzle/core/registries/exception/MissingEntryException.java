package com.github.puzzle.core.registries.exception;

import com.github.puzzle.core.registries.IRegistry;
import finalforeach.cosmicreach.util.Identifier;
import org.jetbrains.annotations.NotNull;

public class MissingEntryException extends RuntimeException {
    public MissingEntryException(@NotNull IRegistry<?> registry, Identifier name) {
        super("Registry \"" + registry.identifier() + "\" does not contain \"" + name + "\"");
    }
}
