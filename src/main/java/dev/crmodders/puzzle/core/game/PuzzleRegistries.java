package dev.crmodders.puzzle.core.game;

import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.core.game.loot.PuzzleLootTable;
import dev.crmodders.puzzle.core.loader.registries.BasicFreezableRegistry;
import dev.crmodders.puzzle.core.loader.registries.IRegistry;

public class PuzzleRegistries {

    public static final IRegistry.IFreezing<PuzzleLootTable> PuzzleLootTables = new BasicFreezableRegistry<>(new Identifier("puzzle", "loot_tables"));

}
