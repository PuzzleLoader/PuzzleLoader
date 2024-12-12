package com.github.puzzle.game.mixins.refactors.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.common.Puzzle;
import com.github.puzzle.game.engine.rendering.text.FormattedTextRenderer;
import com.github.puzzle.game.ui.modmenu.ModMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(MainMenu.class)
public class MainMenuMixin extends GameState {
    /**
     * @author replet
     * @reason Change pos and size of the language button
     */
    @Inject(method = "create",at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/ui/UIElement;setText(Ljava/lang/String;)V",ordinal = 3),locals = LocalCapture.CAPTURE_FAILHARD)
    void moveLanguage(CallbackInfo ci, GameState thisState, UIElement startButton, UIElement loadButton, UIElement optionsButton, UIElement langButton){
        langButton.x = -(275.0F/4)-2;
        langButton.w = (275.0F/2)-5;
    }
    /**
     * @author replet
     * @reason adds the mods button
    */
    @Inject(method = "create",at = @At("TAIL"))
    void addModsButton(CallbackInfo ci) {
         UIElement modsButton = new UIElement((275.0F / 4) + 2, 95f, (275.0F / 2) - 5, 35.0F) {
             public void onClick() {
                 super.onClick();
                 GameState.switchToGameState(new ModMenu(currentGameState));
             }
         };
         modsButton.hAnchor = HorizontalAnchor.CENTERED;
         modsButton.setText(LanguageManager.string(new TranslationKey("puzzle-loader:menu.mods")));
         modsButton.show();
         uiObjects.add(modsButton);
    }

    @Inject(method = "render", at = @At("TAIL"))
    void addPuzzleVersion(CallbackInfo ci) {
        batch.setProjectionMatrix(this.uiCamera.combined);
        batch.begin();

        String versionText = "Puzzle Loader Version: " + Puzzle.VERSION;
        FontRenderer.getTextDimensions(this.uiViewport, versionText, new Vector2());

        batch.setColor(Color.WHITE);
        FontRenderer.drawText(batch, this.uiViewport, versionText, -8.0F,  -60.0F, HorizontalAnchor.RIGHT_ALIGNED, VerticalAnchor.BOTTOM_ALIGNED);

        batch.end();
    }
}
