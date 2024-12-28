package com.github.puzzle.game.engine;

import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.PuzzleRegistries;
import finalforeach.cosmicreach.util.Identifier;

import java.util.ArrayList;
import java.util.List;

public class LoadStage {

    public IGameLoader loader;
    public TranslationKey title = new TranslationKey("puzzle-loader:none");

    public void initialize(IGameLoader loader) {
        this.loader = loader;
        try {
            PuzzleRegistries.EVENT_BUS.subscribe(this);
        } catch (Exception ignored) {}
    }

    public void doStage() {
        loader.setupProgressBar(loader.getProgressBar2(), 0);
        loader.setupProgressBar(loader.getProgressBar3(), 0);
    }

    public List<Runnable> getGlTasks() {
        return new ArrayList<>();
    }

}