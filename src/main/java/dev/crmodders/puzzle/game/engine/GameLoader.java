package dev.crmodders.puzzle.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.NinePatch;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Image;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.badlogic.gdx.scenes.scene2d.utils.NinePatchDrawable;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import dev.crmodders.puzzle.core.Puzzle;
import dev.crmodders.puzzle.core.localization.LanguageManager;
import dev.crmodders.puzzle.core.localization.TranslationKey;
import dev.crmodders.puzzle.core.localization.TranslationLocale;
import dev.crmodders.puzzle.game.Globals;
import dev.crmodders.puzzle.game.engine.blocks.BlockLoader;
import dev.crmodders.puzzle.game.engine.stages.*;
import dev.crmodders.puzzle.game.events.OnPreLoadAssetsEvent;
import dev.crmodders.puzzle.game.ui.CosmicReachFont;
import dev.crmodders.puzzle.game.ui.TranslationParameters;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.PrealphaPreamble;
import finalforeach.cosmicreach.settings.Preferences;
import finalforeach.cosmicreach.ui.debug.DebugInfo;
import finalforeach.cosmicreach.ui.debug.DebugStringItem;
import org.greenrobot.eventbus.Subscribe;
import org.lwjgl.opengl.GL11;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.concurrent.CountDownLatch;

import static dev.crmodders.puzzle.core.PuzzleRegistries.EVENT_BUS;
import static dev.crmodders.puzzle.core.resources.PuzzleGameAssetLoader.LOADER;

public class GameLoader extends GameState {

    public static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | GameLoader");

    private static final TranslationKey TEXT_RAM_USAGE = new TranslationKey("puzzle-loader:loading_menu.ram_usage");

    public Stage gdxStage;
    public OrthographicCamera gdxStageCamera;
    public Viewport gdxStageViewport;
    protected Color background = Color.BLACK;

    private Texture textLogo;
    private Texture puzzleIcon;
    private NinePatch hp9Patch;

    private Label ramUsageText;
    private ProgressBar ramUsageBar;

    public Label progressBarText1;
    public ProgressBar progressBar1;

    public Label progressBarText2;
    public ProgressBar progressBar2;

    public Label progressBarText3;
    public ProgressBar progressBar3;

    private final Queue<LoadStage> stages = new LinkedList<>();
    private final Queue<Runnable> glQueue = new LinkedList<>();

    public BlockLoader blockLoader;

    @Subscribe
    public void onEvent(OnPreLoadAssetsEvent event) {
//        puzzleIcon = LOADER.loadSync("puzzle-loader:icons/PuzzleLoaderIconx128.png", Texture.class);
        puzzleIcon = LOADER.loadSync("puzzle-loader:icons/Puzzle Loader x16 - Copy.png", Texture.class);
        textLogo = LOADER.loadSync("base:textures/text-logo-hd.png", Texture.class);
        hp9Patch = new NinePatch(LOADER.loadSync("base:textures/ui/healthbar.png", Texture.class), 4, 4, 6, 6);
    }

    @Override
    public void create() {
        super.create();

        DebugInfo.items.insert(1,new DebugStringItem(
                true,
                () -> Puzzle.VERSION,
                (v) -> "Puzzle Loader Version: " + v
        ));
        gdxStageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gdxStageViewport = new ExtendViewport(800.0F, 600.0F, gdxStageCamera);
        gdxStage = new Stage(gdxStageViewport, batch);
        gdxStageCamera.position.set(0, 0, 0);
        gdxStageViewport.apply(false);

        // create singletons
        blockLoader = new BlockLoader();
        GameSingletons.blockModelInstantiator = blockLoader.factory;

        // register to eventbus
        EVENT_BUS.register(this);

        // preload all resources
        EVENT_BUS.post(new OnPreLoadAssetsEvent());

        // setup gui
        Label.LabelStyle labelStyle = new Label.LabelStyle(CosmicReachFont.FONT, Color.WHITE);

        ProgressBar.ProgressBarStyle progressBarStyle = new ProgressBar.ProgressBarStyle();
        progressBarStyle.knobBefore = new NinePatchDrawable(hp9Patch);

        ramUsageBar = new ProgressBar(0, 1, 1, false, progressBarStyle);
        ramUsageBar.setColor(Color.GREEN);
        ramUsageBar.setSize(500, 40);
        ramUsageBar.setPosition(0, 220, Align.center);
        gdxStage.addActor(ramUsageBar);

        ramUsageText = new Label(TEXT_RAM_USAGE.getIdentifier(), labelStyle);
        ramUsageText.setUserObject(new TranslationParameters.Builder(TEXT_RAM_USAGE).withProgressBar(ramUsageBar).build());
        ramUsageText.setSize(500, 40);
        ramUsageText.setPosition(0, 240, Align.center);
        gdxStage.addActor(ramUsageText);

        Image title = new Image(textLogo);
        title.setSize(768, 256);
        title.setPosition(0, 96, Align.center);
        gdxStage.addActor(title);

        Image icon = new Image(puzzleIcon);
        icon.setSize(96, 96);
        icon.setPosition(460, -240, Align.center);
        gdxStage.addActor(icon);

        progressBarText1 = new Label("", labelStyle);
        progressBarText1.setSize(500, 40);
        progressBarText1.setPosition(0, -40, Align.center);
        gdxStage.addActor(progressBarText1);

        progressBar1 = new ProgressBar(0, 1, 1, false, progressBarStyle);
        progressBar1.setColor(Color.GREEN);
        progressBar1.setSize(500, 40);
        progressBar1.setPosition(0, -60, Align.center);
        progressBar1.setUserObject(progressBarText1);
        gdxStage.addActor(progressBar1);

        progressBarText2 = new Label("", labelStyle);
        progressBarText2.setSize(500, 40);
        progressBarText2.setPosition(0, -100, Align.center);
        gdxStage.addActor(progressBarText2);

        progressBar2 = new ProgressBar(0, 1, 1, false, progressBarStyle);
        progressBar2.setColor(Color.GREEN);
        progressBar2.setSize(500, 40);
        progressBar2.setPosition(0, -120, Align.center);
        progressBar2.setUserObject(progressBarText2);
        gdxStage.addActor(progressBar2);

        progressBarText3 = new Label("", labelStyle);
        progressBarText3.setSize(500, 40);
        progressBarText3.setPosition(0, -160, Align.center);
        gdxStage.addActor(progressBarText3);

        progressBar3 = new ProgressBar(0, 1, 1, false, progressBarStyle);
        progressBar3.setColor(Color.GREEN);
        progressBar3.setSize(500, 40);
        progressBar3.setPosition(0, -180, Align.center);
        progressBar3.setUserObject(progressBarText3);
        gdxStage.addActor(progressBar3);

        // select flux language
        String chosen = Preferences.chosenLang.getValue();
        if(chosen != null) {
            TranslationLocale locale = TranslationLocale.fromLanguageTag(chosen);
            if(LanguageManager.hasLanguageInstalled(locale)) LanguageManager.selectLanguage(locale);
        }
        if(Globals.SelectedLanguage == null) {
            TranslationLocale locale = TranslationLocale.fromLanguageTag("en-US");
            LanguageManager.selectLanguage(locale);
        }
        LanguageManager.updateLabels(gdxStage);

        // setup loading stages
        addStage(new LoadingAssets());
        addStage(new PreInitialize());
        addStage(new Initialize());
        addStage(new LoadingCosmicReach());
        addStage(new PostInitialize());

        // setup loading thread
        Thread loadingThread = new Thread(this::gameLoaderThread, "GameLoader");
        loadingThread.setUncaughtExceptionHandler(this::uncaughtException);
        loadingThread.start();
    }

    @Override
    public void render() {
        Runtime runtime = Runtime.getRuntime();
        int ramValue = (int) ((runtime.totalMemory() - runtime.freeMemory()) / (1024 * 1024));
        int ramRange = (int) (runtime.maxMemory() / (1024 * 1024));
        try {
            ramUsageBar.setRange(0, ramRange);
            ramUsageBar.setStepSize(1);
            ramUsageBar.setValue(ramValue);
        } catch (Exception e) {
            LOGGER.warn("got ya you little bug", e);
        }
        LanguageManager.updateLabel(ramUsageText, (TranslationParameters) ramUsageText.getUserObject());

        long endTime = System.currentTimeMillis() + 50;
        while (!glQueue.isEmpty() && System.currentTimeMillis() < endTime) {
            Runnable glTask = glQueue.poll();
            if(glTask != null) {
                glTask.run();
            }
        }

        if(Globals.GameLoaderHasLoaded) {
            if(blockLoader.errors.isEmpty())
                switchToGameState(new PrealphaPreamble());
            else
                switchToGameState(new BlockErrorScreen(blockLoader, new PrealphaPreamble()));
        }

        super.render();
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        gdxStageViewport.apply(false);
        gdxStage.act();
        try {
            gdxStage.draw();
        } catch (Exception e) {
            LOGGER.info("Fuck this bug");
        }
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gdxStageViewport.update(width, height, false);
    }

    private void gameLoaderThread() {
        setupProgressBar(progressBar1, stages.size());
        while(!stages.isEmpty()) {
            LoadStage stage = stages.poll();
            incrementProgress(progressBar1, LanguageManager.string(stage.title));
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
        Globals.GameLoaderHasLoaded = true;
    }

    private void uncaughtException(Thread t, Throwable e) {
        LOGGER.error("Thread '{}' threw an Exception", t.getName(), e);
    }

    public void addStage(LoadStage stage) {
        stages.add(stage);
        stage.initialize(this);
    }

    public void setupProgressBar(ProgressBar bar, int range) {
        Label text = (Label) bar.getUserObject();
        text.setUserObject(null);
        text.setText("");
        bar.setRange(0, range);
        bar.setValue(0);
        text.setVisible(range != 0);
        bar.setVisible(range != 0);
    }

    public void setupProgressBar(ProgressBar bar, int range, TranslationKey key) {
        Label text = (Label) bar.getUserObject();
        TranslationParameters parameters = new TranslationParameters.Builder(key).withProgressBar(bar).build();
        text.setUserObject(parameters);
        bar.setRange(0, range);
        bar.setValue(0);
        LanguageManager.updateLabel(text, parameters);
        text.setVisible(range != 0);
        bar.setVisible(range != 0);
    }

    public void setupProgressBar(ProgressBar bar, int range, String str) {
        Label text = (Label) bar.getUserObject();
        text.setUserObject(null);
        text.setText(str);
        bar.setRange(0, range);
        bar.setValue(0);
        text.setVisible(range != 0);
        bar.setVisible(range != 0);
    }

    public void incrementProgress(ProgressBar bar) {
        Label text = (Label) bar.getUserObject();
        if(text.getUserObject() != null) {
            TranslationParameters parameters = (TranslationParameters) text.getUserObject();
            bar.setValue(bar.getValue() + 1);
            LanguageManager.updateLabel(text, parameters);
        }
    }

    public void incrementProgress(ProgressBar bar, TranslationKey key) {
        Label text = (Label) bar.getUserObject();
        text.setUserObject(new TranslationParameters.Builder(key).withProgressBar(bar).build());
        if(text.getUserObject() != null) {
            TranslationParameters parameters = (TranslationParameters) text.getUserObject();
            bar.setValue(bar.getValue() + 1);
            LanguageManager.updateLabel(text, parameters);
        }
    }

    public void incrementProgress(ProgressBar bar, String str) {
        Label text = (Label) bar.getUserObject();
        bar.setValue(bar.getValue() + 1);
        text.setText(str + " %d/%d".formatted((int)bar.getValue(), (int) bar.getMaxValue()));
    }

}