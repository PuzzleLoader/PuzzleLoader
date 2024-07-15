package dev.crmodders.puzzle.core.game.mixins.ui;

import dev.crmodders.puzzle.core.game.Globals;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.rendering.BatchedZoneRenderer;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.ui.UIElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "finalforeach/cosmicreach/gamestates/OptionsMenu$6")
public abstract class OptionsMixin extends UIElement {

    public OptionsMixin(float x, float y, float w, float h) {
        super(x, y, w, h);
    }

    /**
     * @author replet
     * @reason allow more zone renderers
     */
    @Overwrite
    public void onClick(){
        super.onClick();
        GameSingletons.zoneRenderer.unload();
        Globals.rendererIndex++;
        if((Globals.rendererIndex) == Globals.renderers.size())
            Globals.rendererIndex=0;

            var rndr = Globals.renderers.get(Globals.rendererIndex);
            if(rndr==null)
                GameSingletons.zoneRenderer=new BatchedZoneRenderer();
            else
                GameSingletons.zoneRenderer = rndr;

        GraphicsSettings.renderer.setValue("batched");
        GameSingletons.isAllFlaggedForRemeshing = true;
        this.updateText();
    }
}
