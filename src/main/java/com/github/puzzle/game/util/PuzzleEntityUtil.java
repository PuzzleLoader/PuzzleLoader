package com.github.puzzle.game.util;

import com.badlogic.gdx.utils.Array;
import com.github.puzzle.core.Identifier;
import finalforeach.cosmicreach.entities.Entity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.world.Zone;
import org.jetbrains.annotations.NotNull;

public class PuzzleEntityUtil {
    public static Entity getClosestEntity(@NotNull Zone zone, @NotNull Entity sourceEntity) {
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

    public static Entity getClosestEntity(@NotNull Zone zone, @NotNull Entity sourceEntity, Identifier entityId) {
        Entity closest = null;
        float closestDst = Float.MAX_VALUE;
        float range = sourceEntity.sightRange;

        for (Entity entity : zone.allEntities) {
            if (entity != null) {
                if (entity != sourceEntity) {
                    if (Identifier.fromString(entity.entityTypeId).equals(entityId)) {
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
        }

        return closest;
    }

    public static Entity getClosestPlayerToEntity(@NotNull Zone zone, @NotNull Entity sourceEntity) {
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
