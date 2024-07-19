package dev.crmodders.puzzle.loader.providers.api;

import dev.crmodders.puzzle.loader.launch.PuzzleClassLoader;
import dev.crmodders.puzzle.loader.mod.Version;

import java.util.Collection;

public interface IGameProvider {
    // Game Names
    String getId();
    String getName();

    // Game Version
    Version getGameVersion();
    String getRawVersion();

    // Extra Data
    String getEntrypoint();
    Collection<String> getArgs();

    // Inits
    void registerTransformers(PuzzleClassLoader classLoader);
    void initArgs(String[] args);
    void inject(PuzzleClassLoader classLoader);

    void addBuiltinMods();
}
