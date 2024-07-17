package dev.crmodders.puzzle.core.registries;

import dev.crmodders.puzzle.core.Identifier;
import dev.crmodders.puzzle.core.registries.exception.AlreadyFrozenException;
import dev.crmodders.puzzle.core.registries.exception.MissingEntryException;
import dev.crmodders.puzzle.core.registries.exception.NotReadableException;
import dev.crmodders.puzzle.core.registries.exception.NotWritableException;

import java.util.Set;

public interface IRegistry<T> extends Iterable<T> {
    Identifier identifier();

    boolean contains(Identifier name) throws NotReadableException;
    T get(Identifier name) throws NotReadableException, MissingEntryException;
    RegistryObject<T> store(Identifier id, T value) throws NotWritableException;
    Set<Identifier> names() throws NotReadableException;

    void freeze() throws AlreadyFrozenException;

    boolean readable();
    boolean writable();
}
