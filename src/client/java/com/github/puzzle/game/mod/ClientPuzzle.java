package com.github.puzzle.game.mod;

import com.github.puzzle.core.annotation.Internal;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientPostModInitializer;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.game.commands.ClientCommandManager;

@Internal
@Env(EnvType.CLIENT)
public class ClientPuzzle implements ClientModInitializer, ClientPostModInitializer {

    @Override
    public void onInit() {
        ClientCommandManager.registerCommands();
    }

    @Override
    public void onPostInit() {

    }
}
