package dev.crmodders.puzzle.game.mixins.refactors.be;

import dev.crmodders.puzzle.game.Globals;
import finalforeach.cosmicreach.ClientSingletons;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.rendering.BatchedZoneRenderer;
import finalforeach.cosmicreach.settings.GraphicsSettings;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.Objects;

@Mixin(ClientSingletons.class)
public class ClientSingletonsMixin {

    @Inject(method = "create",at = @At(value = "FIELD", target = "Lfinalforeach/cosmicreach/GameSingletons;soundManager:Lfinalforeach/cosmicreach/ISoundManager;",shift = At.Shift.BEFORE))
    private static void rendererFromSettings(CallbackInfo ci){

        GameSingletons.zoneRenderer.unload();

        for(var renderer : Globals.renderers){
            if(renderer.getName().equals(GraphicsSettings.renderer.getValue())){
                GameSingletons.zoneRenderer = renderer;
                return;
            }
        }
        var batchedRendererIndex = Globals.rendererIndexMap.get(BatchedZoneRenderer.class.getName());
        if(batchedRendererIndex == null){
            throw new RuntimeException("Can't find default renderer(batch)");
        }
        GameSingletons.zoneRenderer = Globals.renderers.get(batchedRendererIndex);
        Globals.rendererIndex = batchedRendererIndex;

    }
}
