package com.github.puzzle.game.mixins.refactors.ui;

import com.github.puzzle.game.ui.credits.PuzzleCreditsMenu;
import finalforeach.cosmicreach.gamestates.CreditsMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.UIElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = {"finalforeach/cosmicreach/gamestates/MainMenu$6"})
public class CreditsButtonMixin extends UIElement {

    public CreditsButtonMixin(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    @Override
    public void onClick() {
        super.onClick();
        GameState.switchToGameState(new PuzzleCreditsMenu());
    }

}
