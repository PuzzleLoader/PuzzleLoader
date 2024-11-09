package com.github.puzzle.game.items;

import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.world.Zone;

/**
 * This interface allow IModItem(s) to tick as entity or in player inventory .
 * @see IModItem
 */
public interface ITickingItem {

    /**
     * This allows to add multiple textures to an item for later.
     * @param fixedUpdateTimeStep elapsed time since the last update.
     * @param stack The ItemStack that will be tick.
     * @param isBeingHeld If the ItemStack is being held by player.
     */
    void tickStack(float fixedUpdateTimeStep, ItemStack stack, boolean isBeingHeld);


    /**
     * This allows to add multiple textures to an item for later.
     * @param zone the zone this ItemStack is in.
     * @param deltaTime elapsed time since the last update.
     * @param entity the ItemEntity that contains the ItemStack.
     * @param stack The ItemStack that will be tick.
     */
    void tickEntity(Zone zone, double deltaTime, ItemEntity entity, ItemStack stack);

}
