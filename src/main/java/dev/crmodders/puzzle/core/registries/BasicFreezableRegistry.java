package dev.crmodders.puzzle.core.registries;

import dev.crmodders.flux.tags.Identifier;

import java.util.*;
import java.util.function.Supplier;

public class BasicFreezableRegistry<T> implements IRegistry.IFreezing<T>, IStorable<T> {

    Identifier registryId;
    Map<Identifier, Supplier<T>> pendingRegistyObjects;
    Map<Identifier, T> registeredObjects;
    List<RegistryObject<T>> registryObjects;

    boolean isFrozen;

    public BasicFreezableRegistry(Identifier id) {
        registryId = id;
        pendingRegistyObjects = new HashMap<>();
        registryObjects = new ArrayList<>();
        registeredObjects = new HashMap<>();
    }

    @Override
    public RegistryObject<T> register(Identifier id, Supplier<T> objectSupplier) {
        if (isFrozen) throw new RuntimeException("Registry \"" + registryId + "\" is frozen");
        if (pendingRegistyObjects.containsKey(id)) throw new RuntimeException("CANNOT HAVE 2+ OBJECTS WITH SAME ID \"" + id + "\" IN REGISTRY \"" + registryId + "\"");
        pendingRegistyObjects.put(id, objectSupplier);
        registryObjects.add(new RegistryObject<>(this, id));
        return registryObjects.get(registryObjects.size() - 1);
    }

    @Override
    public T get(Identifier id) {
        if (registeredObjects.containsKey(id)) return registeredObjects.get(id);
        throw new RuntimeException("\"" + id + "\" does not exist in registry \"" + registryId + "\"");
    }

    @Override
    public RegistryObject<T>[] getObjects() {
        return registryObjects.toArray(new RegistryObject[]{});
    }

    @Override
    public Iterator<T> getIterator() {
        return new RegistryIterator<>(this);
    }

    @Override
    public Identifier getId() {
        return registryId;
    }

    @Override
    public boolean isFrozen() {
        return isFrozen;
    }

    @Override
    public void freeze() {
        isFrozen = true;

        for (Identifier id : pendingRegistyObjects.keySet()) {
            registeredObjects.put(id, pendingRegistyObjects.get(id).get());
        }
        pendingRegistyObjects.clear();
    }
}
