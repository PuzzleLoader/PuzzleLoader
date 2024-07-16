package dev.crmodders.puzzle.core.registries;

public class AlreadyFrozenException extends RuntimeException {

    public AlreadyFrozenException(IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is already frozen");
    }

}
