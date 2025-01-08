package com.github.puzzle.game.mixins.client.ui;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.Vector2;
import com.github.puzzle.game.common.Puzzle;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.PrealphaPreamble;
import finalforeach.cosmicreach.ui.FontRenderer;
import finalforeach.cosmicreach.ui.HorizontalAnchor;
import finalforeach.cosmicreach.ui.VerticalAnchor;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(PrealphaPreamble.class)
public class PrealphaPreambleMixin extends GameState {

    @Inject(method = "render", at = @At("HEAD"))
    void addPuzzleVersion(CallbackInfo ci) {
        batch.setColor(Color.WHITE);
    }
}
