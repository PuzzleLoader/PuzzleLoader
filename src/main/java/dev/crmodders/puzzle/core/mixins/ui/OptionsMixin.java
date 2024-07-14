package dev.crmodders.puzzle.core.mixins.ui;

import dev.crmodders.puzzle.core.Globals;
import dev.crmodders.puzzle.core.rendering.TestZoneRenderer;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.gamestates.OptionsMenu;
import finalforeach.cosmicreach.rendering.BatchedZoneRenderer;
import finalforeach.cosmicreach.rendering.ExperimentalNaiveZoneRenderer;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.UIObject;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.injection.Inject;

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
