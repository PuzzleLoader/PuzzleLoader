package com.github.puzzle.game.mixins.refactors.ui;

import com.github.puzzle.game.Globals;
import com.github.puzzle.game.ui.menus.PuzzleKeybinds;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.KeybindsMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(KeybindsMenu.class)
public abstract class OptionsMixin2 extends GameState {

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(GameState previousState, CallbackInfo ci) {
        UIElement puzzleKeybinds = new UIElement(275.0F, -50.0F, 250.0F, 50.0F) {
            public void onClick() {
                super.onClick();
                GameState.switchToGameState(new PuzzleKeybinds(OptionsMixin2.this));
            }
        };
        puzzleKeybinds.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
        puzzleKeybinds.setText("Puzzle Keybinds");
        puzzleKeybinds.show();
        uiObjects.add(puzzleKeybinds);
    }
}
