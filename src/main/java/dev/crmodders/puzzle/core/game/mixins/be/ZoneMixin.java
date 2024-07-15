package dev.crmodders.puzzle.core.game.mixins.be;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.IntMap;
import dev.crmodders.puzzle.accessors.Point3DMapAccessor;
import dev.crmodders.puzzle.core.game.block_entities.interfaces.IRenderable;
import dev.crmodders.puzzle.core.game.block_entities.interfaces.ITickable;
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
        chunks.forEach((chunk) -> {
                if (chunk instanceof ITickable tickable) {
                    tickable.onTick(1f / 20f); // TODO get tps here
                }
        });
    }

    @Override
    public void onRender(Camera camera) {
        for (IntMap<Chunk> chunkIntMap : ((Point3DMapAccessor<Chunk>) chunks).getMap().values()) {
            for (Chunk chunk : chunkIntMap.values()) {
                if (chunk instanceof IRenderable renderable) {
                    renderable.onRender(camera);
                }
            }
        }
    }
}