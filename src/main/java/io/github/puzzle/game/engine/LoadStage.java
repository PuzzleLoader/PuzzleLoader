package io.github.puzzle.game.engine;

import io.github.puzzle.core.PuzzleRegistries;
import io.github.puzzle.core.localization.TranslationKey;

import java.util.ArrayList;
import java.util.List;

public class LoadStage {

    public GameLoader loader;
    public TranslationKey title;

    public void initialize(GameLoader loader) {
        this.loader = loader;
        try {
            PuzzleRegistries.EVENT_BUS.register(this);
        } catch (Exception ignored) {}
    }

    public void doStage() {
        loader.setupProgressBar(loader.progressBar2, 0);
        loader.setupProgressBar(loader.progressBar3, 0);
    }

    public List<Runnable> getGlTasks() {
        return new ArrayList<>();
    }

}