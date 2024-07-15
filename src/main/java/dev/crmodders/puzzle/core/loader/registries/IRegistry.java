package dev.crmodders.puzzle.core.loader.registries;

import dev.crmodders.flux.tags.Identifier;

import java.util.Iterator;
import java.util.function.Supplier;

public interface IRegistry<T> {

    static <T> RegistryObject<T> register(IRegistry<T> registry, Identifier id, Supplier<T> objectSupplier) {
        assert registry instanceof IStorable<T>;
        return ((IStorable<T>) registry).register(id, objectSupplier);
    }

    static <T> T register(IRegistry.IDynamic<T> registry, Identifier id, T object) {
        return registry.store(id, object);
    }

    RegistryObject<T>[] getObjects();
    Iterator<T> getIterator();
    Identifier getId();

    interface IFreezing<T> extends IRegistry<T> {
        boolean isFrozen();
        void freeze();
    }

    interface IDynamic<T> extends IRegistry<T> {
        T store(Identifier id, T object);
        T get(Identifier id);
    }

}
