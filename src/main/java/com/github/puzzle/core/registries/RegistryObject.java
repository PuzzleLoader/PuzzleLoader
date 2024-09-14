package com.github.puzzle.core.registries;

import org.jetbrains.annotations.NotNull;
import com.github.puzzle.core.util.Identifier;

import java.util.function.Supplier;

public class RegistryObject<T> {
    /*
        this is temporary until the load cycle is finished
     */
    public static <T> RegistryObject<T> register(@NotNull IRegistry<T> registry, Identifier name, @NotNull Supplier<T> supplier) {
        return registry.store(name, supplier.get());
    }

    private final IRegistry<T> registry;
    private final Identifier name;

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
