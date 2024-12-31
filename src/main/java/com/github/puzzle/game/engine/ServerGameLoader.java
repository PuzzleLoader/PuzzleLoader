package com.github.puzzle.game.engine;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.block.DataModBlock;
import com.github.puzzle.game.engine.blocks.BlockLoadException;
import com.github.puzzle.game.engine.blocks.IBlockLoader;
import com.github.puzzle.game.engine.blocks.ServerBlockLoader;
import com.github.puzzle.game.engine.stages.common.LoadingCosmicReach;
import com.github.puzzle.game.engine.stages.server.Initialize;
import com.github.puzzle.game.engine.stages.server.LoadingAssets;
import com.github.puzzle.game.engine.stages.server.PostInitialize;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.invoke.MethodHandles;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import static com.github.puzzle.game.PuzzleRegistries.EVENT_BUS;

public class ServerGameLoader implements IGameLoader {

    public static ServerGameLoader INSTANCE;

    public static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | ServerGameLoader");
    final static ProgressBar EMPTY_BAR = new ProgressBar(0, 100, 1, false, new ProgressBar.ProgressBarStyle());
    final static Label EMPTY_LABEL = new Label("", (Label.LabelStyle) null) {
        @Override
        public void setStyle(LabelStyle style) {

        }
    };

    private final Queue<LoadStage> stages = new LinkedList<>();
    private final Queue<Runnable> glQueue = new LinkedList<>();

    public ServerBlockLoader blockLoader;

    public ServerGameLoader() {
        INSTANCE = this;
    }

    public void create() {
        ModLocator.locatedMods.values().forEach((modContainer -> modContainer.INFO.Entrypoints.values().forEach(adapterPathPairs -> {
            adapterPathPairs.forEach(adapterPathPair -> {
                EVENT_BUS.registerLambdaFactory(adapterPathPair.getValue().substring(0, adapterPathPair.getValue().lastIndexOf(".")), (lookupInMethod, klass) -> (MethodHandles.Lookup) lookupInMethod.invoke(null, klass, MethodHandles.lookup()));
            });
        })));


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
            LoadStage stage = stages.poll();
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

    public void addStage(LoadStage stage) {
        stages.add(stage);
        stage.initialize(this);
    }

    @Override
    public void setupProgressBar(ProgressBar bar, int range) {}

    @Override
    public void setupProgressBar(ProgressBar bar, int range, TranslationKey key) {}

    @Override
    public void setupProgressBar(ProgressBar bar, int range, String str) {}

    @Override
    public void incrementProgress(ProgressBar bar) {}

    @Override
    public void incrementProgress(ProgressBar bar, TranslationKey key) {}

    @Override
    public void incrementProgress(ProgressBar bar, String str) {}

    @Override
    public boolean isServer() {
        return true;
    }

    @Override
    public IBlockLoader getBlockLoader() {
        return blockLoader;
    }

    @Override
    public ProgressBar getProgressBar1() {
        return EMPTY_BAR;
    }

    @Override
    public ProgressBar getProgressBar2() {
        return EMPTY_BAR;
    }

    @Override
    public ProgressBar getProgressBar3() {
        return EMPTY_BAR;
    }

    @Override
    public Label getProgressBarText1() {
        return EMPTY_LABEL;
    }

    @Override
    public Label getProgressBarText2() {
        return EMPTY_LABEL;
    }

    @Override
    public Label getProgressBarText3() {
        return EMPTY_LABEL;
    }
}