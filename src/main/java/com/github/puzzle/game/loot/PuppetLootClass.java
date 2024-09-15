package com.github.puzzle.game.loot;

import com.badlogic.gdx.math.Vector3;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.loot.Loot;
import finalforeach.cosmicreach.items.loot.LootOption;
import finalforeach.cosmicreach.world.Zone;
import org.jetbrains.annotations.NotNull;

public class PuppetLootClass extends Loot {
    PuzzleLootTable parentTable;

    public PuppetLootClass(@NotNull Identifier id, PuzzleLootTable table) {
        super(id.toString());
        parentTable = table;
    }

    public void dropAsItems(Zone zone, Vector3 position) {
        parentTable.drop(zone, position);
    }

    @Override
    public void addOption(LootOption option) {
        throw new RuntimeException("The method \"addOption(LootOption option)\" is not implemented");
    }

    @Override
    public void addOption(float weight, Item item, int min, int max) {
        throw new RuntimeException("The method \"addOption(float weight, Item item, int min, int max)\" is not implemented");
    }

    @Override
    public void addOption(float weight, BlockState blockState, int min, int max) {
        throw new RuntimeException("The method \"addOption(float weight, BlockState blockState, int min, int max)\" is not implemented");
    }

}
