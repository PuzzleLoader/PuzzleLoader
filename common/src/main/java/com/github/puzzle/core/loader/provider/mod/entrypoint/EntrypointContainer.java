package com.github.puzzle.core.loader.provider.mod.entrypoint;


import com.github.puzzle.core.loader.provider.ProviderException;
import com.github.puzzle.core.loader.provider.lang.ILangProvider;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.google.common.collect.ImmutableCollection;
import com.google.common.collect.ImmutableMap;
import org.jetbrains.annotations.NotNull;

import java.util.Objects;
import java.util.function.Consumer;

public class EntrypointContainer {
    private final ImmutableMap<String, ImmutableCollection<AdapterPathPair>> entrypointClasses;
    private final ModContainer container;

    public <T> void invokeClasses(String key, Class<T> type, Consumer<? super T> invoker) throws Exception {
        if (!ILangProvider.PROVDERS.containsKey("java")) ILangProvider.PROVDERS.put("java", ILangProvider.JAVA_INSTANCE);
        if (entrypointClasses.get(key) != null) {
            for (AdapterPathPair pair : Objects.requireNonNull(entrypointClasses.get(key))){
                if (ILangProvider.PROVDERS.get(pair.getAdapter()) == null) throw new ProviderException("LangProvider \"" + pair.getAdapter() + "\" does not exist.");
                T inst = ILangProvider.PROVDERS.get(pair.getAdapter()).create(container.INFO, pair.getValue(), type);
                invoker.accept(inst);
            }
        }
    }

    public EntrypointContainer(ModContainer container, @NotNull ImmutableMap<String, ImmutableCollection<AdapterPathPair>> entrypoints) {
        this.container = container;
        entrypointClasses = entrypoints;
    }

}
