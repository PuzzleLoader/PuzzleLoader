package dev.crmodders.puzzle.core;

import dev.crmodders.puzzle.core.localization.LanguageRegistry;
import dev.crmodders.puzzle.core.registries.GenericRegistry;
import dev.crmodders.puzzle.core.registries.IRegistry;
import dev.crmodders.puzzle.game.loot.PuzzleLootTable;

import static dev.crmodders.puzzle.core.Puzzle.MOD_ID;

public class PuzzleRegistries {
    public static final IRegistry<PuzzleLootTable> LOOT_TABLES = new GenericRegistry<>(Identifier.of(MOD_ID, "loot_tables"));
    public static final LanguageRegistry LANGUAGES = new LanguageRegistry(Identifier.of(MOD_ID, "languages"));
}
