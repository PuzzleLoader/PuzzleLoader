package io.github.puzzle.cosmic.api.entity.player;

import com.badlogic.gdx.math.Vector3;
import io.github.puzzle.cosmic.api.account.IPuzzleAccount;
import io.github.puzzle.cosmic.api.entity.IPuzzleEntity;
import io.github.puzzle.cosmic.api.item.IPuzzleItemStack;
import io.github.puzzle.cosmic.api.util.IPuzzleIdentifier;
import io.github.puzzle.cosmic.api.world.IPuzzleChunk;
import io.github.puzzle.cosmic.api.world.IPuzzleWorld;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzlePlayer.class, impl = "Player")
public interface IPuzzlePlayer {

    IPuzzleEntity getEntity();

    void proneCheck(IPuzzleZone zone);
    void crouchCheck(IPuzzleZone zone);

    void respawn(IPuzzleWorld world);
    void respawn(IPuzzleZone zone);

    void setPosition(float x, float y, float z);

    IPuzzleZone getZone();
    IPuzzleChunk getChunk(IPuzzleWorld world);

    short getBlockLight(IPuzzleWorld world);
    int getSkyLight(IPuzzleWorld world);

    void spawnDroppedItem(IPuzzleWorld world, IPuzzleItemStack itemStack);

    default Vector3 getPosition() {
        return getEntity().getPosition();
    }

    boolean isLoading();

    default void setZone(IPuzzleZone zone) {
        setZone(zone.getId());
    }

    default void setZone(IPuzzleIdentifier zoneId) {
        setZone(zoneId.asString());
    }

    void setZone(String zoneId);

    default boolean isDead() {
        return getEntity().isDead();
    }

    IPuzzleAccount getAccount();
    String getUsername();

}
