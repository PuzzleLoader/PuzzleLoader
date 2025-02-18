package io.github.puzzle.cosmic.impl.mixin.entity;

import com.badlogic.gdx.math.Vector3;
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

@Mixin(Entity.class)
public class EntityMixin implements IPuzzleEntity {


    @Shadow public Vector3 position;

    @Shadow public Vector3 viewDirection;

    @Shadow public float hitpoints;

    @Override
    public Vector3 getPosition() {
        return position;
    }

    @Override
    public Vector3 getDirection() {
        return viewDirection;
    }

    @Override
    public boolean isDead() {
        return this.hitpoints <= 0.0F;
    }
}
