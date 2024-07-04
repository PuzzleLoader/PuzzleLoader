package dev.crmodders.puzzle.core.entities;

import dev.crmodders.flux.tags.Identifier;
import finalforeach.cosmicreach.entities.Entity;

public class PuzzleEntity extends Entity {

    public PuzzleEntity(String entityTypeId) {
        super(entityTypeId);
    }

    public PuzzleEntity(Identifier entityTypeId) {
        super(entityTypeId.toString());
    }

}
