package dev.crmodders.puzzle.core.registries;

import dev.crmodders.puzzle.core.resources.Identifier;
import javassist.NotFoundException;

import java.util.function.Supplier;

interface IStorable<T> extends IRegistry<T> {

    RegistryObject<T> register(Identifier id, Supplier<T> objectSupplier);
    T get(Identifier id) throws NotFoundException;

}
