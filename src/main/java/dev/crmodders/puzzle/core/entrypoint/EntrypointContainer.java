package dev.crmodders.puzzle.core.entrypoint;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import dev.crmodders.puzzle.annotations.Internal;
import dev.crmodders.puzzle.core.launch.Piece;

import java.util.*;
import java.util.function.Consumer;

@Internal
public class EntrypointContainer {

    private final ImmutableMap<String, ImmutableCollection<Class<?>>> entrypointClasses;

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

    public EntrypointContainer(ImmutableMap<String, ImmutableCollection<String>> entrypoints) {
        ImmutableMap.Builder<String, ImmutableCollection<Class<?>>> entrypointClasses0 = ImmutableMap.builder();
        entrypoints.keySet().forEach(key -> {
            Collection<Class<?>> classes = new ArrayList<>();
            for (String clazzName : Objects.requireNonNull(entrypoints.get(key))) {
                try {
                    Class<?> clazz = Class.forName(clazzName, false, Piece.classLoader);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            entrypointClasses0.put(key, ImmutableList.copyOf(classes));
        });
        entrypointClasses = entrypointClasses0.build();
    }

    public EntrypointContainer(Map<String, Collection<String>> entrypoints) {
        ImmutableMap.Builder<String, ImmutableCollection<Class<?>>> entrypointClasses0 = ImmutableMap.builder();
        entrypoints.keySet().forEach(key -> {
            Collection<Class<?>> classes = new ArrayList<>();
            for (String clazzName : Objects.requireNonNull(entrypoints.get(key))) {
                try {
                    Class<?> clazz = Class.forName(clazzName, false, Piece.classLoader);
                    classes.add(clazz);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
            }
            entrypointClasses0.put(key, ImmutableList.copyOf(classes));
        });
        entrypointClasses = entrypointClasses0.build();
    }

}
