package dev.crmodders.puzzle.core.registries;

public class NotWritableException extends RuntimeException {

    public NotWritableException(IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is not writable");
    }

}
