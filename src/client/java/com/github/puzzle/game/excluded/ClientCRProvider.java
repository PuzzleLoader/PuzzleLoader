package com.github.puzzle.game.excluded;

import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.core.loader.meta.Version;
import com.github.puzzle.core.loader.provider.IGameProvider;
import com.github.puzzle.core.loader.util.ModLocator;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.server.ServerLauncher;

import java.io.IOException;
import java.util.Collection;
import java.util.List;

public class ClientCRProvider implements IGameProvider {


    @Override
    public String getId() {
        return "cosmic-reach";
    }

    @Override
    public String getName() {
        return "Cosmic Reach";
    }

    @Override
    public Version getGameVersion() {
        return Version.parseVersion(getRawVersion());
    }

    @Override
    public String getRawVersion() {
        try {
            return new String(GameAssetLoader.class.getResourceAsStream("/build_assets/version.txt").readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getEntrypoint() {
        return ServerGlobals.isRunningOnParadox ? ModLocator.PARADOX_SERVER_ENTRYPOINT : ServerLauncher.class.getName();;
    }

    @Override
    public Collection<String> getArgs() {
        return List.of();
    }

    @Override
    public void registerTransformers(PuzzleClassLoader classLoader) {

    }

    @Override
    public void initArgs(String[] args) {

    }

    @Override
    public void inject(PuzzleClassLoader classLoader) {

    }

    @Override
    public void addBuiltinMods() {

    }
}
