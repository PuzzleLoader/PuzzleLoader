package dev.crmodders.puzzle.core.mixins.be;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.LongMap;
import dev.crmodders.puzzle.accessors.Point3DMapAccessor;
import dev.crmodders.puzzle.core.entities.blocks.interfaces.IRenderable;
import dev.crmodders.puzzle.core.entities.blocks.interfaces.ITickable;
import finalforeach.cosmicreach.util.Point3DMap;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Zone.class)
public class ZoneMixin implements IRenderable {

    @Shadow private @Final Point3DMap<Chunk> chunks;

    @Inject(method = "runScheduledTriggers", at = @At("HEAD"))
    private void zoneUpdate(CallbackInfo ci) {
        for (LongMap.Entry<IntMap<Chunk>> intMapEntry : ((Point3DMapAccessor<Chunk>) chunks).getMap()) {
            if (intMapEntry.value != null) {
                for (IntMap.Entry<Chunk> chunkEntry : intMapEntry.value) {
                    if(chunkEntry.value instanceof ITickable tickable) {
                        tickable.onTick(1f / 20f); // TODO get tps here
                    }
                }
            }
        }
    }

    @Override
    public void onRender(Camera camera) {
        for (LongMap.Entry<IntMap<Chunk>> intMapEntry : ((Point3DMapAccessor<Chunk>) chunks).getMap()) {
            if (intMapEntry.value != null) {
                for (IntMap.Entry<Chunk> chunkEntry : intMapEntry.value) {
                    if(chunkEntry.value instanceof IRenderable renderable) {
                        renderable.onRender(camera);
                    }
                }
            }
        }
    }
}