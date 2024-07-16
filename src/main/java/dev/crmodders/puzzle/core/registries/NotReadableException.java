package dev.crmodders.puzzle.core.registries;

public class NotReadableException extends RuntimeException {

    public NotReadableException(IRegistry<?> registry) {
        super("Registry \"" + registry.identifier() + "\" is not readable");
    }

}
