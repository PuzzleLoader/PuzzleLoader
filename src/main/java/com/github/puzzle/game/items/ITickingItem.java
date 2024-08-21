package com.github.puzzle.game.items;

import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.world.Zone;

public interface ITickingItem {

    void tickStack(float fixedUpdateTimeStep, ItemStack stack, boolean isBeingHeld);
    void tickEntity(Zone zone, double deltaTime, ItemEntity entity, ItemStack stack);

}
