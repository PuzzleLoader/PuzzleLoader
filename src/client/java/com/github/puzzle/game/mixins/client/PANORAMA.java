package com.github.puzzle.game.mixins.client;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Queue;
import com.badlogic.gdx.utils.viewport.Viewport;
import com.github.puzzle.core.Constants;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.settings.Keybind;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.io.File;

@Mixin(InGame.class)
public abstract class PANORAMA extends GameState {

    @Shadow
    public static Player getLocalPlayer() {
        return null;
    }

    @Shadow private Viewport viewport;
    @Unique
    Keybind puzzleLoader$keybind = Keybind.fromDefaultKey("Take Panorama Photo", Input.Keys.P);

    @Unique
    private static boolean needsToWait = false;
    private static boolean didRenderDebug = false;
    private static boolean didRenderUI = false;
    private static Vector3 oldViewDir = new Vector3();
    Queue<Runnable> runnables = new Queue<>();

    private static int h;
    private static int w;
    private static int x;
    private static int y;
    private static int ww;
    private static int wh;

    @Inject(method = "render", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        if (!Constants.getVersion().equals("69.69.69")) return;

        if (needsToWait && !runnables.isEmpty()) {
            runnables.removeFirst().run();
        } else if (needsToWait && runnables.isEmpty()) {
            Gdx.graphics.setWindowedMode(ww, wh);
            viewport.setScreenBounds(x, y, w, h);
            UI.renderUI = didRenderUI;
            UI.renderDebugInfo = didRenderDebug;
            getLocalPlayer().getEntity().viewDirection.set(oldViewDir);
            needsToWait = false;
        }
        if (puzzleLoader$keybind.isPressed() && puzzleLoader$keybind.isJustPressed() && GameState.currentGameState == this) {
            didRenderUI = UI.renderUI;
            didRenderDebug = UI.renderDebugInfo;
            h = viewport.getScreenHeight();
            w = viewport.getScreenWidth();
            x = viewport.getScreenX();
            y = viewport.getScreenY();
            ww = Gdx.graphics.getWidth();
            wh = Gdx.graphics.getHeight();
            viewport.setScreenBounds(0, 0, 1000, 1000);
            Gdx.graphics.setWindowedMode(1000, 1000);
            Entity playerEntity = getLocalPlayer().getEntity();
            oldViewDir.set(playerEntity.viewDirection);

            UI.renderUI = false;
            UI.renderDebugInfo = false;

            runnables.addFirst(() -> puzzleLoader$screenshot("py"));
            runnables.addFirst(() -> playerEntity.viewDirection.set(new Vector3(1.1822068E-6f,0.9999999f,4.572117E-4f)));
            runnables.addFirst(() -> puzzleLoader$screenshot("ny"));
            runnables.addFirst(() -> playerEntity.viewDirection.set(new Vector3(-4.204668E-8f,-1.0f,1.4082887E-4f)));
            runnables.addFirst(() -> puzzleLoader$screenshot("nx"));
            runnables.addFirst(() -> playerEntity.viewDirection.set(new Vector3(1, 0, 0)));
            runnables.addFirst(() -> puzzleLoader$screenshot("px"));
            runnables.addFirst(() -> playerEntity.viewDirection.set(new Vector3(-1, 0, 0)));
            runnables.addFirst(() -> puzzleLoader$screenshot("pz"));
            runnables.addFirst(() -> playerEntity.viewDirection.set(new Vector3(0, 0, 1)));
            runnables.addFirst(() -> puzzleLoader$screenshot("nz"));
            runnables.addFirst(() -> playerEntity.viewDirection.set(new Vector3(0, 0, -1)));

            needsToWait = true;
        };
    }

    @Unique
    private void puzzleLoader$screenshot(String name) {
        String oldName = takeScreenshot();
        String screenshotDirLoc = SaveLocation.getScreenshotFolderLocation();
        String screenshotFileName = screenshotDirLoc + "/" + name + ".png";
        new File(oldName).renameTo(new File(screenshotFileName));
    }

}
