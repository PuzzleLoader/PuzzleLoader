package dev.crmodders.puzzle.core.game.mixins.be;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.utils.IntMap;
import com.llamalad7.mixinextras.sugar.Local;
import dev.crmodders.puzzle.accessors.Point3DMapAccessor;
import dev.crmodders.puzzle.core.game.block_entities.ExtendedBlockEntity;
import dev.crmodders.puzzle.core.game.block_entities.interfaces.INeighborUpdateListener;
import dev.crmodders.puzzle.core.game.block_entities.interfaces.IRenderable;
import dev.crmodders.puzzle.core.game.block_entities.interfaces.ITickable;
import dev.crmodders.puzzle.utils.DirectionUtil;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.constants.Direction;
import finalforeach.cosmicreach.util.IPoint3DMap;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

@Mixin(Chunk.class)
public abstract class ChunkMixin implements ITickable, IRenderable {

    @Shadow
    IPoint3DMap<BlockEntity> blockEntities;

    @Shadow public Region region;

    @Shadow public int blockX;

    @Shadow public int blockY;

    @Shadow public int blockZ;

    @Shadow public int chunkX;

    @Shadow public int chunkY;

    @Shadow public int chunkZ;

    @Override
    public void onTick(float tps) {
        if(blockEntities != null)
            for (IntMap<BlockEntity> blockEntityMap : ((Point3DMapAccessor<BlockEntity>) blockEntities).getMap().values()) {
                for (BlockEntity blockEntity : blockEntityMap.values()) {
                    if (blockEntity instanceof ITickable tickable) {
                        tickable.onTick(tps);
                    }
                }
            }
//            blockEntities.forEach(entity -> {
//                if(entity instanceof ITickable tickable) {
//                    tickable.onTick(tps);
//                }
//            });
    }

    @Override
    public void onRender(Camera camera) {
        if(blockEntities != null)
            for (IntMap<BlockEntity> blockEntityMap : ((Point3DMapAccessor<BlockEntity>) blockEntities).getMap().values()) {
                for (BlockEntity blockEntity : blockEntityMap.values()) {
                    if (blockEntity instanceof IRenderable renderable) {
                        renderable.onRender(camera);
                    }
                }
            }
    }

    @Inject(method = "setBlockEntity", at= @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blockentities/BlockEntity;onRemove()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void destroyBlockEntity(BlockState blockState, int localX, int localY, int localZ, CallbackInfoReturnable<BlockEntity> cir, @Local BlockEntity blockEntity) {
        if(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
            extendedBlockEntity.position = null;
        }
    }

    @Inject(method = "setBlockEntity", at= @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blockentities/BlockEntity;onCreate(Lfinalforeach/cosmicreach/blocks/BlockState;)V", shift = At.Shift.BEFORE))
    private void initializeBlockEntity(BlockState blockState, int localX, int localY, int localZ, CallbackInfoReturnable<BlockEntity> cir, @Local BlockEntity blockEntity) {
        if(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
            extendedBlockEntity.initialize((Chunk) (Object) this, localX, localY, localZ);
        }
    }

    @Inject(method = "setBlockEntityDirect", at= @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blockentities/BlockEntity;onRemove()V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void destroyBlockEntity2(BlockState blockState, BlockEntity blockEntity, int localX, int localY, int localZ, CallbackInfo ci) {
        if(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
            extendedBlockEntity.position = null;
        }
    }

    @Inject(method = "setBlockEntityDirect", at= @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blockentities/BlockEntity;onCreate(Lfinalforeach/cosmicreach/blocks/BlockState;)V", shift = At.Shift.BEFORE))
    private void initializeBlockEntity2(BlockState blockState, BlockEntity blockEntity, int localX, int localY, int localZ, CallbackInfo ci) {
        if(blockEntity instanceof ExtendedBlockEntity extendedBlockEntity) {
            extendedBlockEntity.initialize((Chunk) (Object) this, localX, localY, localZ);
        }
    }

    @Inject(method = "setBlockEntity", at= @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blockentities/BlockEntity;onCreate(Lfinalforeach/cosmicreach/blocks/BlockState;)V", shift = At.Shift.AFTER), locals = LocalCapture.CAPTURE_FAILHARD)
    private void fireNeighbors(BlockState blockState, int localX, int localY, int localZ, CallbackInfoReturnable<BlockEntity> cir, @Local BlockEntity blockEntity) {
        for(Direction face : Direction.values()) {
            int neighborX = blockX + localX + face.getXOffset();
            int neighborY = blockY + localY + face.getYOffset();
            int neighborZ = blockZ + localZ + face.getZOffset();

            int cx = Math.floorDiv(neighborX, 16);
            int cy = Math.floorDiv(neighborY, 16);
            int cz = Math.floorDiv(neighborZ, 16);

            boolean neighborIsInThisChunk = (cx == chunkX && cy == chunkY && cz == chunkZ);
            Chunk neighbor = null;
            if(neighborIsInThisChunk) neighbor = (Chunk) (Object) this;
            else if(region != null && region.zone != null) neighbor = region.zone.getChunkAtBlock(neighborX, neighborY, neighborZ);
            else System.err.println("Region or Zone is not initialized, problems will occur");

            if(neighbor != null) {
                int neighborLocalX = neighborX - neighbor.blockX;
                int neighborLocalY = neighborY - neighbor.blockY;
                int neighborLocalZ = neighborZ - neighbor.blockZ;

                BlockEntity neighborEntity = neighbor.getBlockEntity(neighborLocalX, neighborLocalY, neighborLocalZ);

                if(blockEntity instanceof INeighborUpdateListener neighborChangeListener) {
                    BlockState neighborBlockState = neighbor.getBlockState(neighborLocalX, neighborLocalY, neighborLocalZ);
                    neighborChangeListener.onNeighborUpdate(face, neighborBlockState, neighborEntity);
                }

                if(neighborEntity instanceof INeighborUpdateListener neighborChangeListener) {
                    neighborChangeListener.onNeighborUpdate(DirectionUtil.opposite(face), blockState, blockEntity);
                }
            }

        }
    }

}