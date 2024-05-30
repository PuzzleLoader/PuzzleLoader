package dev.crmodders.puzzle.core.entrypoint;


import dev.crmodders.puzzle.annotations.Internal;
import dev.crmodders.puzzle.core.launch.Piece;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Internal
public class EntrypointContainer {

    private final Map<String, Collection<Class<?>>> entrypointClasses;

    public <T> Collection<Class<T>> getClasses(String key, Class<T> type) {
        Collection<Class<T>> classes = new ArrayList<>();
        if (entrypointClasses.get(key) != null) {
            for (Class<?> clazz : entrypointClasses.get(key)){
                Class<T> foundClass = null;
                for (Class<?> invokees : clazz.getInterfaces()) {
                    if (invokees.getName().equals(type.getName())) {
                        foundClass = (Class<T>) clazz;
                    }
                }
                classes.add(foundClass);
            }
        }
        return classes;
    }

    public <T> void invokeClasses(String key, Class<T> type, Consumer<? super T> invoker) {
        for (Class<T> clazz : getClasses(key, type)) {
            try {
                invoker.accept(clazz.newInstance());
            } catch (InstantiationException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public EntrypointContainer(Map<String, Collection<String>> entrypoints) {
        entrypointClasses = new HashMap<>();
        entrypoints.keySet().forEach(key -> {
            Collection<Class<?>> classes = new ArrayList<>();
            for (String clazz : entrypoints.get(key)) {
                try {
                    classes.add(Class.forName(clazz, false, Piece.classLoader));
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            entrypointClasses.put(key, classes);
        });
    }

}
