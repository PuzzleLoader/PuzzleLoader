package com.github.puzzle.game.engine;

import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.PuzzleRegistries;

import java.util.ArrayList;
import java.util.List;

public class LoadStage {

    public ClientGameLoader loader;
    public TranslationKey title;

    public void initialize(ClientGameLoader loader) {
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