package com.github.puzzle.game.mixins.refactors.ui;

import com.github.puzzle.core.localization.Language;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.ui.ModMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.LanguagesMenu;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.UIElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import static com.github.puzzle.game.Globals.SelectedLanguage;

@Mixin(MainMenu.class)
public class MainMenuMixin extends GameState{
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
        UIElement modsButton = new UIElement((275.0F/4)+2, 140.0F, (275.0F/2)-5, 35.0F) {
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

}
