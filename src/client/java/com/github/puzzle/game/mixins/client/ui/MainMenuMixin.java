package com.github.puzzle.game.mixins.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.scenes.scene2d.Action;
import com.badlogic.gdx.utils.Align;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.common.Puzzle;
import com.github.puzzle.game.ui.modmenu.ModMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import finalforeach.cosmicreach.ui.actions.AlignXAction;
import finalforeach.cosmicreach.ui.actions.AlignYAction;
import finalforeach.cosmicreach.ui.widgets.CRButton;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.ModifyArg;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@SuppressWarnings("removal")
@Mixin(MainMenu.class)
public class MainMenuMixin extends GameState {
    /**
     * @author replet
     * @reason Change size of the options button
     */
    @ModifyArg(method = "create",at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/gamestates/MainMenu$4;setSize(FF)V"),index = 0)
    float resizeOptions(float x){
       return (275.0F/2)-5;
    }
    /**
     * @author replet
     * @reason Change pos of the options button
     */
    @ModifyArg(method = "create",at= @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/gamestates/MainMenu$4;addAction(Lcom/badlogic/gdx/scenes/scene2d/Action;)V",ordinal = 0),index = 0)
    Action moveOptions(Action unused){
        return new AlignXAction(1,0.5f, -(275.0F/4)-2 );
    }

    /**
     * @author replet
     * @reason Change pos of the lang button
     */
    @ModifyArg(method = "create",at= @At(value = "INVOKE", target ="Lfinalforeach/cosmicreach/ui/widgets/CRButton;addAction(Lcom/badlogic/gdx/scenes/scene2d/Action;)V",ordinal = 1),index = 0)
    Action moveLang(Action unused){
        return new AlignYAction(4, 0.0F, 80.0F);
    }
    /**
     * @author replet
     * @reason adds the mods button
    */
    @Inject(method = "create",at = @At("TAIL"))
    void addModsButton(CallbackInfo ci) {
        CRButton modsButton = new CRButton(LanguageManager.string(new TranslationKey("puzzle-loader:menu.mods"))) {
            public void onClick() {
                super.onClick();
                GameState.switchToGameState(new ModMenu(currentGameState));
            }
        };
        modsButton.addAction(new AlignXAction(1, 0.5F, (275.0F / 4) + 2));
        modsButton.addAction(new AlignYAction(1, 0.5F, -95.0F));
        modsButton.setSize((275.0F / 2) - 5, 35.0F);
        this.stage.addActor(modsButton);
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
