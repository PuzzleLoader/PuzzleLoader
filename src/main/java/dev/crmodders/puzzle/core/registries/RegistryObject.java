package dev.crmodders.puzzle.core.registries;

import dev.crmodders.puzzle.core.Identifier;

import java.util.function.Supplier;

public class RegistryObject<T> {

    /*
        this is temporary until the load cycle is finished
     */
    public static <T> RegistryObject<T> register(IRegistry<T> registry, Identifier name, Supplier<T> supplier) {
        return registry.store(name, supplier.get());
    }

    private IRegistry<T> registry;
    private Identifier name;

    public RegistryObject(Identifier registry, Identifier name) {
        throw new UnsupportedOperationException("not implemented");
    }

    public RegistryObject(IRegistry<T> registry, Identifier name) {
        this.registry = registry;
        this.name = name;
    }

    public T get() {
        return registry.get(name);
    }

}
