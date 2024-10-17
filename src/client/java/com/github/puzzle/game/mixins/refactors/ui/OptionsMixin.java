package com.github.puzzle.game.mixins.refactors.ui;

import com.github.puzzle.game.ClientGlobals;
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
        ClientGlobals.rendererIndex++;
        if(ClientGlobals.rendererIndex == ClientGlobals.renderers.size())
            ClientGlobals.rendererIndex=0;
        var renderer = ClientGlobals.renderers.get(ClientGlobals.rendererIndex);
        GameSingletons.zoneRenderer = renderer;

        GraphicsSettings.renderer.setValue(GameSingletons.zoneRenderer.getName());
        GameSingletons.isAllFlaggedForRemeshing = true;
        this.updateText();
    }
}
