package com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls;

import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;

@Env(EnvType.CLIENT)
public interface ClientModInitializer {
    String ENTRYPOINT_KEY = "client_init";

    void onInit();

    static void invokeEntrypoint() {
        ModInitializer.invokeEntrypoint();
        PuzzleEntrypointUtil.invoke(ENTRYPOINT_KEY, ClientModInitializer.class, ClientModInitializer::onInit);
    }
}
