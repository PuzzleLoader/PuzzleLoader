package dev.crmodders.puzzle.core.entities.util;

import com.badlogic.gdx.utils.Array;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.world.Zone;

public interface PuzzleEntityUtil {

    static Entity getClosestEntity(Zone zone, Entity sourceEntity) {
        Entity closest = null;
        float closestDst = Float.MAX_VALUE;
        float range = sourceEntity.sightRange;

        for (Entity entity : zone.allEntities) {
            if (entity != null) {
                if (entity != sourceEntity) {
                    float dst = sourceEntity.position.dst(entity.position);
                    if (!(dst > range)) {
                        if (closest == null) {
                            closest = entity;
                        } else if (closestDst > dst) {
                            closestDst = dst;
                            closest = entity;
                        }
                    }
                }
            }
        }

        return closest;
    }

    static Entity getClosestPlayerToEntity(Zone zone, Entity sourceEntity) {
        Array<Player> players = zone.players;
        Entity closest = null;
        float closestDst = Float.MAX_VALUE;
        float range = sourceEntity.sightRange;

        for (Player p : players) {
            Entity pe = p.getEntity();
            if (pe != null) {
                float dst = sourceEntity.position.dst(pe.position);
                if (!(dst > range)) {
                    if (closest == null) {
                        closest = pe;
                    } else if (closestDst > dst) {
                        closestDst = dst;
                        closest = pe;
                    }
                }
            }
        }

        return closest;
    }
}
