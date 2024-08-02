package dev.crmodders.puzzle.game.mixins.refactors.ui;

import dev.crmodders.puzzle.game.Globals;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import finalforeach.cosmicreach.ui.UIElement;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;

@Mixin(targets = "finalforeach/cosmicreach/gamestates/OptionsMenu$6") //The lambda for 'rendererButton'
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
        if(Globals.rendererIndex == Globals.renderers.size())
            Globals.rendererIndex=0;
        var renderer = Globals.renderers.get(Globals.rendererIndex);
        GameSingletons.zoneRenderer = renderer;

        GraphicsSettings.renderer.setValue(GameSingletons.zoneRenderer.getName());
        GameSingletons.isAllFlaggedForRemeshing = true;
        this.updateText();
    }
}
