package com.github.puzzle.game.mixins.refactors.be;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.IntMap;
import com.github.puzzle.game.blockentities.IRenderable;
import com.github.puzzle.game.mixins.accessors.Point3DMapAccessor;
import finalforeach.cosmicreach.util.Point3DMap;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Zone.class)
public class ZoneMixin implements IRenderable {
    @Shadow private @Final Point3DMap<Chunk> chunks;

    @Override
    public void onRender(Camera camera) {
        chunks.forEach((c) -> {
            if (c instanceof IRenderable renderable) {
                renderable.onRender(camera);
            }
        });
    }
}