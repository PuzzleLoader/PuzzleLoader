package io.github.puzzle.cosmic.api.entity;

import com.badlogic.gdx.math.Vector3;
import io.github.puzzle.cosmic.api.world.IPuzzleWorld;
import io.github.puzzle.cosmic.api.world.IPuzzleZone;
import io.github.puzzle.cosmic.util.ApiDeclaration;

@ApiDeclaration(api = IPuzzleEntity.class, impl = "Entity")
public interface IPuzzleEntity {

    Vector3 getPosition();
    Vector3 getDirection();

    boolean isDead();
}
