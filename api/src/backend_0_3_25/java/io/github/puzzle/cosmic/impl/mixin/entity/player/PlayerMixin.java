package io.github.puzzle.cosmic.impl.mixin.entity.player;

import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.World;
import finalforeach.cosmicreach.world.Zone;
import io.github.puzzle.cosmic.api.account.IPuzzleAccount;
import io.github.puzzle.cosmic.api.entity.IPuzzleEntity;
import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.api.item.IPuzzleItemStack;
import io.github.puzzle.cosmic.api.world.IPuzzleChunk;
import io.github.puzzle.cosmic.api.world.IPuzzleWorld;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;

@Mixin(Player.class)
public abstract class PlayerMixin implements IPuzzlePlayer {

    @Shadow private Entity entity;

    @Shadow public abstract void proneCheck(Zone zone);

    @Shadow public abstract void crouchCheck(Zone zone);

    @Shadow public abstract void respawn(Zone zone);

    @Shadow public abstract void respawn(World world);

    @Shadow public String zoneId;

    @Shadow public abstract Chunk getChunk(World world);

    @Shadow public abstract short getBlockLight(World world);

    @Shadow public abstract int getSkyLight(World world);

    @Shadow public abstract void spawnDroppedItem(World world, ItemStack itemStack);

    @Shadow public abstract void setZone(String zoneId);

    @Shadow public transient boolean loading;

    @Override
    public IPuzzleEntity getEntity() {
        return IPuzzleEntity.as(entity);
    }

    @Override
    public void proneCheck(IPuzzleZone iPuzzleZone) {
        proneCheck(iPuzzleZone.as());
    }

    @Override
    public void crouchCheck(IPuzzleZone iPuzzleZone) {
        crouchCheck(iPuzzleZone.as());
    }

    @Override
    public void respawn(IPuzzleWorld iPuzzleWorld) {
        respawn(iPuzzleWorld.as());
    }

    @Override
    public void respawn(IPuzzleZone iPuzzleZone) {
        respawn(iPuzzleZone.as());
    }

    @Override
    public void setPosition(float x, float y, float z) {
        entity.setPosition(x, y, z);
    }

    @Override
    public IPuzzleZone getZone() {
        return IPuzzleZone.as(GameSingletons.world.getZoneCreateIfNull(zoneId));
    }

    @Override
    public IPuzzleChunk getChunk(IPuzzleWorld world) {
        return IPuzzleChunk.as(getChunk(world.as()));
    }

    @Override
    public short getBlockLight(IPuzzleWorld iPuzzleWorld) {
        return getBlockLight(iPuzzleWorld.as());
    }

    @Override
    public int getSkyLight(IPuzzleWorld iPuzzleWorld) {
        return getSkyLight(iPuzzleWorld.as());
    }

    @Override
    public void spawnDroppedItem(IPuzzleWorld iPuzzleWorld, IPuzzleItemStack iPuzzleItemStack) {
        spawnDroppedItem(iPuzzleWorld.as(), iPuzzleItemStack.as());
    }

    @Override
    public boolean isLoading() {
        return loading;
    }

    @Override
    public IPuzzleAccount getAccount() {
        return IPuzzleAccount.as(GameSingletons.getAccountFromPlayer(as()));
    }

}
