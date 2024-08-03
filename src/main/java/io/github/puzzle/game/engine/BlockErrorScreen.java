package io.github.puzzle.game.engine;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ScrollPane;
import com.badlogic.gdx.utils.Align;
import com.badlogic.gdx.utils.viewport.ExtendViewport;
import com.badlogic.gdx.utils.viewport.Viewport;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import io.github.puzzle.game.block.DataModBlock;
import io.github.puzzle.game.engine.blocks.BlockLoadException;
import io.github.puzzle.game.engine.blocks.BlockLoader;
import io.github.puzzle.game.ui.CosmicReachFont;
import org.lwjgl.opengl.GL11;

import java.io.PrintWriter;
import java.io.StringWriter;

public class BlockErrorScreen extends GameState {

    public Stage gdxStage;
    public OrthographicCamera gdxStageCamera;
    public Viewport gdxStageViewport;
    protected Color background = Color.BLACK;

    private final BlockLoader blockLoader;
    private final GameState next;

    private ScrollPane scrollPane;

    public BlockErrorScreen(BlockLoader blockLoader, GameState next) {
        this.blockLoader = blockLoader;
        this.next = next;
    }

    @Override
    public void create() {
        super.create();
        gdxStageCamera = new OrthographicCamera(Gdx.graphics.getWidth(), Gdx.graphics.getHeight());
        gdxStageViewport = new ExtendViewport(800.0F, 600.0F, gdxStageCamera);
        gdxStage = new Stage(gdxStageViewport, batch);
        Gdx.input.setInputProcessor(gdxStage);
        gdxStageCamera.position.set(0, 0, 0);
        gdxStageViewport.apply(false);

        Label title = new Label("Error while loading Blocks", new Label.LabelStyle(CosmicReachFont.FONT_BIG, Color.WHITE));
        title.layout();
        title.setSize(title.getGlyphLayout().width, title.getGlyphLayout().height);
        title.setPosition(0.0F, 250.0F, Align.center);
        gdxStage.addActor(title);

        StringBuilder errorText = new StringBuilder();

        for(BlockLoadException error : blockLoader.errors) {

            StringWriter writer = new StringWriter();
            error.getCause().printStackTrace(new PrintWriter(writer));
            String exception = writer.toString();

            String className = error.iModBlock != null ? error.iModBlock.getClass().getSimpleName() : "Unknown";
            String fileName = error.iModBlock instanceof DataModBlock dataModBlock && dataModBlock.debugResourceLocation != null ? dataModBlock.debugResourceLocation.locate().name() : "Unknown";
            String blockName = error.blockName != null ? error.blockName : "Unknown";
            String blockId = error.blockId != null ? error.blockId.toString() : "Unknown";

            errorText
                    .append("Error while loading Block (Class: ").append(className)
                    .append(", File: \"").append(fileName)
                    .append("\", Name: \"").append(blockName)
                    .append("\", Id: \"").append(blockId)
                    .append("\")\n")
                    .append("\nError Stacktrace:\n");
            for(String line : exception.lines().toList()) {
                errorText.append("  ").append(line).append("\n");
            }
            errorText.append("\n\n");

        }

        // don't waste memory
        blockLoader.errors.clear();

        try {
            FileHandle errorFile = Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/flux-error-latest.txt");
            errorFile.writeString(errorText.toString(), false);
        } catch (Exception ignored) {}

        Label error = new Label("\n" + errorText, new Label.LabelStyle(CosmicReachFont.FONT, Color.WHITE));
        error.setWrap(true);
        error.invalidate();
        error.pack();
        error.layout();

        scrollPane = new ScrollPane(error);
        scrollPane.setSize(800.0F, 450.0F);
        scrollPane.setPosition(0, 0, Align.center);
        scrollPane.setScrollbarsVisible(true);
        scrollPane.layout();
        gdxStage.addActor(scrollPane);

        UIElement returnButton = new UIElement(0.0F, -16.0F, 250.0F, 50.0F) {
            public void onClick() {
                super.onClick();
                GameState.switchToGameState(next);
            }
        };
        returnButton.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
        returnButton.setText("Continue");
        returnButton.show();
        uiObjects.add(returnButton);
    }

    @Override
    public void render() {
        super.render();
        Gdx.gl.glClearColor(background.r, background.g, background.b, background.a);
        Gdx.gl.glClear(GL11.GL_COLOR_BUFFER_BIT | GL11.GL_DEPTH_BUFFER_BIT);
        gdxStageViewport.apply(false);
        gdxStage.act();
        gdxStage.draw();
        drawUIElements();
    }

    @Override
    public void resize(int width, int height) {
        super.resize(width, height);
        gdxStageViewport.update(width, height, false);
    }


}