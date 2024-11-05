package com.github.puzzle.game.engine;

import com.github.puzzle.game.PuzzleRegistries;

import java.util.ArrayList;
import java.util.List;

public class ServerLoadStage {

    public ServerGameLoader loader;

    public void initialize(ServerGameLoader loader) {
        this.loader = loader;
        try {
            PuzzleRegistries.EVENT_BUS.subscribe(this);
        } catch (Exception ignored) {}
    }

    public void doStage() {
    }

    public List<Runnable> getGlTasks() {
        return new ArrayList<>();
    }

}