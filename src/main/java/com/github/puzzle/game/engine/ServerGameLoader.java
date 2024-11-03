package com.github.puzzle.game.engine;

import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.block.DataModBlock;
import com.github.puzzle.game.engine.blocks.BlockLoadException;
import com.github.puzzle.game.engine.blocks.ServerBlockLoader;
import com.github.puzzle.game.engine.server_stages.Initialize;
import com.github.puzzle.game.engine.server_stages.LoadingAssets;
import com.github.puzzle.game.engine.server_stages.LoadingCosmicReach;
import com.github.puzzle.game.engine.server_stages.PostInitialize;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.rendering.blockmodels.DummyBlockModel;
import finalforeach.cosmicreach.rendering.blockmodels.IBlockModelInstantiator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

public class ServerGameLoader {

    public static ServerGameLoader INSTANCE;

    static long startTime = System.currentTimeMillis();

    public static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | ServerGameLoader");

    private final Queue<ServerLoadStage> stages = new LinkedList<>();
    private final Queue<Runnable> glQueue = new LinkedList<>();

    public ServerBlockLoader blockLoader;

    public ServerGameLoader() {
        INSTANCE = this;
    }

    public void create() {

        // create singletons
        blockLoader = new ServerBlockLoader();
        GameSingletons.blockModelInstantiator = blockLoader.factory;
        // setup loading stages
        addStage(new LoadingAssets());
        addStage(new Initialize());
        addStage(new LoadingCosmicReach());
        addStage(new PostInitialize());

        // setup loading thread
        Thread loadingThread = new Thread(this::gameLoaderThread, "GameLoader");
        loadingThread.setUncaughtExceptionHandler(this::uncaughtException);
        loadingThread.start();
    }

    public void update() {
        long endTime = System.currentTimeMillis() + 50;
        while (!glQueue.isEmpty() && System.currentTimeMillis() < endTime) {
            Runnable glTask = glQueue.poll();
            if(glTask != null) {
                glTask.run();
            }
        }

        if(ServerGlobals.GameLoaderHasLoaded) {
            if (!blockLoader.errors.isEmpty()) {
                StringBuilder errorText = new StringBuilder();

                for (BlockLoadException error : blockLoader.errors) {

                    StringWriter writer = new StringWriter();
                    error.getCause().printStackTrace(new PrintWriter(writer));
                    String exception = writer.toString();

                    String className = error.iModBlock != null ? error.iModBlock.getClass().getSimpleName() : "Unknown";
                    String fileName = error.iModBlock instanceof DataModBlock dataModBlock && dataModBlock.debugResourceLocation != null ? PuzzleGameAssetLoader.locateAsset(dataModBlock.debugResourceLocation).name() : "Unknown";
                    String blockId = error.blockId != null ? error.blockId.toString() : "Unknown";

                    errorText
                            .append("Error while loading Block (Class: ").append(className)
                            .append(", File: \"").append(fileName)
                            .append("\", Id: \"").append(blockId)
                            .append("\")\n")
                            .append("\nError Stacktrace:\n");
                    for (String line : exception.lines().toList()) {
                        errorText.append("  ").append(line).append("\n");
                    }
                    errorText.append("\n\n");

                }
                throw new RuntimeException(errorText.toString());
            }
        }
    }

    private void gameLoaderThread() {
        while(!stages.isEmpty()) {
            ServerLoadStage stage = stages.poll();
            stage.doStage();

            System.gc();

            CountDownLatch glLock = new CountDownLatch(1);
            List<Runnable> glTasks = stage.getGlTasks();
            glQueue.addAll(glTasks);
            glQueue.add( glLock::countDown );

            try {
                glLock.await();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        ServerGlobals.GameLoaderHasLoaded = true;
    }

    private void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("Thread '{}' threw an Exception", t.getName(), e);
        Threads.stopAllThreads();
        Threads.runOnMainThread(() -> {
            Threads.stopAllThreads();
            throw new RuntimeException(e);
        });
    }

    public void addStage(ServerLoadStage stage) {
        stages.add(stage);
        stage.initialize(this);
    }

}