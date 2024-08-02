package dev.crmodders.puzzle.loader.entrypoint;


import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import dev.crmodders.puzzle.annotations.Internal;
import dev.crmodders.puzzle.loader.lang.LanguageAdapter;
import dev.crmodders.puzzle.loader.lang.LanguageAdapterException;
import dev.crmodders.puzzle.loader.mod.AdapterPathPair;
import dev.crmodders.puzzle.loader.mod.ModContainer;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

@Internal
public class EntrypointContainer {
    private final ImmutableMap<String, ImmutableCollection<AdapterPathPair>> entrypointClasses;
    private final ModContainer container;

    public <T> void invokeClasses(String key, Class<T> type, Consumer<? super T> invoker) throws Exception {
        if (!LanguageAdapter.ADAPTERS.containsKey("java")) LanguageAdapter.ADAPTERS.put("java", LanguageAdapter.JAVA_INSTANCE);
        if (entrypointClasses.get(key) != null) {
            for (AdapterPathPair pair : Objects.requireNonNull(entrypointClasses.get(key))){
                if (LanguageAdapter.ADAPTERS.get(pair.getAdapter()) == null) throw new LanguageAdapterException("Langauge Adapter \"" + pair.getAdapter() + "\" does not exist.");
                T inst = LanguageAdapter.ADAPTERS.get(pair.getAdapter()).create(container.INFO, pair.getValue(), type);
                invoker.accept(inst);
            }
        }
    }

    public EntrypointContainer(ModContainer container, @NotNull ImmutableMap<String, ImmutableCollection<AdapterPathPair>> entrypoints) {
        this.container = container;
//        ImmutableMap.Builder<String, ImmutableCollection<Class<?>>> entrypointClasses0 = ImmutableMap.builder();
//        entrypoints.keySet().forEach(key -> {
//            Collection<Class<?>> classes = new ArrayList<>();
//            for (AdapterPathPair pair : Objects.requireNonNull(entrypoints.get(key))) {
//                String clazzName = pair.getValue();
//                try {
//                    Class<?> clazz = Class.forName(clazzName, false, Piece.classLoader);
//                    classes.add(clazz);
//                } catch (ClassNotFoundException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            entrypointClasses0.put(key, ImmutableList.copyOf(classes));
//        });
//        entrypointClasses = entrypointClasses0.build();
        entrypointClasses = entrypoints;
    }

}
