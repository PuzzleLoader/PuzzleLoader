package com.github.puzzle.game.mixins.client.ui;

import com.github.puzzle.game.ui.credits.PuzzleCreditsMenu;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.widgets.CRButton;
import org.spongepowered.asm.mixin.Mixin;

@Mixin(targets = {"finalforeach/cosmicreach/gamestates/MainMenu$5"})
public class CreditsButtonMixin extends CRButton {

    public CreditsButtonMixin(String labelText) {
        super(labelText);
    }

    @Override
    public void onClick() {
        super.onClick();
        GameState.switchToGameState(new PuzzleCreditsMenu());
    }

}
