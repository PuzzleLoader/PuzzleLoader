package dev.crmodders.puzzle.game;

import dev.crmodders.flux.tags.Identifier;
import dev.crmodders.puzzle.game.loot.PuzzleLootTable;
import dev.crmodders.puzzle.core.localization.Language;
import dev.crmodders.puzzle.core.registries.BasicFreezableRegistry;
import dev.crmodders.puzzle.core.registries.IRegistry;

public class PuzzleRegistries {

    public static final IRegistry.IFreezing<PuzzleLootTable> PuzzleLootTables = new BasicFreezableRegistry<>(new Identifier("puzzle", "loot_tables"));
    public static final IRegistry.IFreezing<Language> PuzzleLanguages = new BasicFreezableRegistry<>(new Identifier("puzzle", "languages"));

}
