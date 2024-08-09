package com.github.puzzle.game.mixins.refactors.items;

import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.settings.ControlSettings;
import finalforeach.cosmicreach.ui.UI;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(InGame.class)
public class InGameMixin {

    boolean isPressed;

    @Inject(method = "update", at = @At("HEAD"))
    private void update(float deltaTime, CallbackInfo ci) {
            if (UI.hotbar.getSelectedSlot() != null){
                ItemStack stack = UI.hotbar.getSelectedSlot().itemStack;
                if (stack != null && stack.getItem() instanceof IModItem modItem) {
                    if (ControlSettings.keyUsePlace.isPressed() && !isPressed) {
                        modItem.onInteract(stack);
                        isPressed = true;
                    }
                    if (isPressed && !ControlSettings.keyUsePlace.isPressed()) isPressed = false;
                }
            }
    }

}
