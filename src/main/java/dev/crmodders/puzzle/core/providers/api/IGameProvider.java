package dev.crmodders.puzzle.core.providers.api;

import dev.crmodders.puzzle.core.mod.Version;
import dev.crmodders.puzzle.core.launch.PuzzleClassLoader;

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

}
