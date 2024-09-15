package com.github.puzzle.game.mixins.refactors.loot;

import com.badlogic.gdx.utils.ObjectMap;
import finalforeach.cosmicreach.util.Identifier;
import com.github.puzzle.core.PuzzleRegistries;
import com.github.puzzle.core.registries.RegistryObject;
import com.github.puzzle.core.registries.exception.NotReadableException;
import com.github.puzzle.game.loot.PuppetLootClass;
import com.github.puzzle.game.loot.PuzzleLootTable;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.items.loot.Loot;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Loot.class)
public class LootTableMixin {
    @Shadow public static ObjectMap<String, Loot> lootMap;

    @Redirect(method = "loadLoot(Lcom/badlogic/gdx/utils/JsonValue;)V", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/items/loot/Loot;registerLoot(Lfinalforeach/cosmicreach/items/loot/Loot;)V"))
    private static void registerLoot0(Loot loot) {
        if (lootMap.containsKey(loot.lootId)) {
            throw new RuntimeException("Cannot register loot with a duplicate id: " + loot.lootId);
        } else {
            lootMap.put(loot.lootId, loot);
        }
    }

    /**
     * @author Mr_Zombii
     * @reason Force the game to get the new lootTables
     * @see PuzzleLootTable
     */
    @Overwrite
    public static Loot get(String lootId) {
        try {
            Identifier id = Identifier.of(lootId);
            RegistryObject<PuzzleLootTable> table = new RegistryObject<>(PuzzleRegistries.LOOT_TABLES, id);
            return new PuppetLootClass(id, table.get());
        } catch (NotReadableException e) {
            throw new RuntimeException(e);
        }
    }

    /**
     * @author Mr_Zombii
     * @reason Force the game to use my new lootTable
     */
    @Overwrite
    public static void registerLoot(Loot loot) {
        Pair<Identifier, PuzzleLootTable> table = PuzzleLootTable.fromVanillaTable(loot);
        RegistryObject.register(PuzzleRegistries.LOOT_TABLES, table.getLeft(), table::getRight);
    }

    @Inject(method = "loadLoot", at = @At("TAIL"))
    private static void loadLootTables(CallbackInfo ci) {
        for (Loot loot : lootMap.values()) {
            Pair<Identifier, PuzzleLootTable> table = PuzzleLootTable.fromVanillaTable(loot);
            RegistryObject.register(PuzzleRegistries.LOOT_TABLES, table.getLeft(), table::getRight);
        }
        lootMap.clear();
        PuzzleRegistries.LOOT_TABLES.freeze();
    }

}
