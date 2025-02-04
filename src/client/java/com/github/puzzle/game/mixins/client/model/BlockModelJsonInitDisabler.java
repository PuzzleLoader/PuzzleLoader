package com.github.puzzle.game.mixins.client.model;

import com.github.puzzle.game.engine.blocks.BlockModelJsonInitializer;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonCuboidFace;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BlockModelJson.class)
public abstract class BlockModelJsonInitDisabler implements BlockModelJsonInitializer.BLOCK_MODEL_JSON_INITIALIZER {

    @Shadow protected abstract void initialize(int rotXZ);

    @Shadow private transient BlockModelJsonCuboidFace[] allFaces;
    int rotXZ;
    boolean canInit;

    @Inject(method = "initialize", cancellable = true, at = @At("HEAD"))
    public void init0(int rotXZ, CallbackInfo ci) {
        this.rotXZ = rotXZ;

        if (!canInit) {
            ci.cancel();
            return;
        }
    }

    @Override
    public BlockModelJson init() {
        canInit = true;
        initialize(rotXZ);
        return (BlockModelJson) (Object) this;
    }
}
