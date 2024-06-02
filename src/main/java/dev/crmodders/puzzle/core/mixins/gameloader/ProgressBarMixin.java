package dev.crmodders.puzzle.core.mixins.gameloader;

import com.badlogic.gdx.graphics.Color;
import dev.crmodders.flux.gui.ProgressBarElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(ProgressBarElement.class)
public class ProgressBarMixin {

    @Shadow
    public Color progressColor = Color.valueOf("#0C7000");

}
