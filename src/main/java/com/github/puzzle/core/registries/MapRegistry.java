package com.github.puzzle.core.registries;

import com.github.puzzle.core.registries.exception.AlreadyFrozenException;
import com.github.puzzle.core.registries.exception.MissingEntryException;
import com.github.puzzle.core.registries.exception.NotReadableException;
import com.github.puzzle.core.registries.exception.NotWritableException;
import finalforeach.cosmicreach.util.Identifier;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;
import java.util.Map;
import java.util.Set;

public class MapRegistry<T> implements IRegistry<T> {
    protected Identifier identifier;
    protected Map<Identifier, T> values;
    protected boolean readable, writable;

    public MapRegistry(Identifier identifier, Map<Identifier, T> values, boolean readable, boolean writable) {
        this.identifier = identifier;
        this.values = values;
        this.readable = readable;
        this.writable = writable;
    }

    @Override
    public Identifier identifier() {
        return identifier;
    }

    @Override
    public boolean contains(Identifier id) {
        if(readable)
            return values.containsKey(id);
        else throw new NotReadableException(this);
    }

    @Override
    public T get(Identifier name) {
        if(readable) {
            T value = values.get(name);
            if(value == null) throw new MissingEntryException(this, name);
            else return value;
        }
        else throw new NotReadableException(this);
    }

    @Override
    public RegistryObject<T> store(Identifier name, T value) {
        if(writable) {
            values.put(name, value);
            return new RegistryObject<>(this, name);
        } else throw new NotWritableException(this);
    }

    @Override
    public Set<Identifier> names() {
        if(readable)
            return values.keySet();
        else throw new NotReadableException(this);
    }

    @Override
    public void freeze() {
        if(writable)
            writable = false;
        else throw new AlreadyFrozenException(this);
    }

    @Override
    public boolean readable() {
        return readable;
    }

    @Override
    public boolean writable() {
        return writable;
    }

    @Override
    public @NotNull Iterator<T> iterator() {
        if(readable)
            return values.values().iterator();
        else throw new NotReadableException(this);
    }
}
