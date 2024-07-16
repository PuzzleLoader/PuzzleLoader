package dev.crmodders.puzzle.core.registries;

import javassist.NotFoundException;

import java.util.Arrays;
import java.util.Iterator;

public class RegistryIterator<T> implements Iterator<T> {

    private final IRegistry<T> registry;
    private final Iterator<RegistryObject<T>> it;

    public RegistryIterator(IRegistry<T> registry) {
        this.registry = registry;
        this.it = Arrays.stream(registry.getObjects()).iterator();
    }

    @Override
    public boolean hasNext() {
        return it.hasNext();
    }

    @Override
    public T next() {
        try {
            return it.next().getObject();
        } catch (NotStorableException | NotFoundException e) {
            throw new RuntimeException(e);
        }
    }
}
