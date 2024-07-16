package dev.crmodders.puzzle.core.registries;

import dev.crmodders.puzzle.core.Identifier;

public class MissingEntryException extends RuntimeException {

    public MissingEntryException(IRegistry<?> registry, Identifier name) {
        super("Registry \"" + registry.identifier() + "\" does not contain \"" + name + "\"");
    }

}
