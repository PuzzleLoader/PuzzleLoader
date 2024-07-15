package dev.crmodders.puzzle.core.registries;

import dev.crmodders.flux.tags.Identifier;
import javassist.NotFoundException;

public class RegistryObject<T> {

    Identifier id;
    IRegistry<T> registry;

    public RegistryObject(IRegistry<T> registry, Identifier id) {
        this.registry = registry;
        this.id = id;
    }

    public T getObject() throws IStorable.NotStorableException, NotFoundException {
        if (registry instanceof IStorable<T> storable) {
            return storable.get(id);
        }
        if (registry instanceof IRegistry.IDynamic<T> dynamic) {
            return dynamic.get(id);
        }
        throw new IStorable.NotStorableException("The registry " + registry.getId() + " was not an Instance of " + IStorable.class.getName());
    }

    public IRegistry<T> getRegistry() {
        return registry;
    }

    public Identifier getId() {
        return id;
    }

}
