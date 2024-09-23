package com.github.puzzle.game.loot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.core.PuzzleRegistries;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.items.loot.Loot;
import finalforeach.cosmicreach.items.loot.LootOption;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.Zone;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

/**
 * This is a replacement class for the vanilla loot-table that
 * is not so messy and inefficient.
 * @see Loot
 */
public class PuzzleLootTable {

    public static final PuzzleLootTable EMPTY = new PuzzleLootTable(Identifier.of("base", "empty_loot_table"));

    public static PuzzleLootTable registerLootTable(Identifier id, PuzzleLootTable lootTable) {
        PuzzleRegistries.LOOT_TABLES.store(id, lootTable);
        return lootTable;
    }

    public static PuzzleLootTable getLootTable(Identifier id) {
        PuzzleLootTable lootTable = PuzzleRegistries.LOOT_TABLES.get(id);
        if (lootTable == null) return EMPTY;
        return lootTable;
    }

    public static PuzzleLootTable readLootTable(String jsonStr) {
        JsonObject object = JsonObject.readHjson(jsonStr).asObject();
        PuzzleLootTable lootTable = new PuzzleLootTable(Identifier.of(object.getString("id", "base:empty_loot_table")));
        for (JsonValue value : object.get("options").asArray()) {
            JsonObject option = value.asObject();
            float weight = option.getFloat("weight", 100);
            List<LootDrop> drops = new ArrayList<>();
            if (option.get("drop") != null) {
                JsonObject drop = object.get("drop").asObject();

                String type = drop.getString("type", "blockstate");
                String dropId = drop.getString("id", "base:debug[default]").strip();
                dropId = dropId.isEmpty() ? "base:debug[default]" : dropId;
                dropId = type.equals("item") ? "base:medkit" : dropId;

                Identifier id = Identifier.of(dropId);
                int min = Math.min(drop.getInt("min", 1), 1);
                int max = drop.getInt("max", 10);
                switch (type) {
                    case "blockstate": drops.add(new LootDrop(BlockState.getInstance(id.toString()).getItem(), min, max));
                    case "item": drops.add(new LootDrop(Item.getItem(id.toString()), min, max));
                }
            } else if (option.get("drops") != null) {
                for (JsonValue value1 : option.get("drops").asArray()) {
                    JsonObject drop = value1.asObject();

                    String type = drop.getString("type", "blockstate");
                    String dropId = drop.getString("id", "base:debug[default]").strip();
                    dropId = dropId.isEmpty() ? "base:debug[default]" : dropId;
                    dropId = type.equals("item") ? "base:medkit" : dropId;

                    Identifier id = Identifier.of(dropId);
                    int min = Math.min(drop.getInt("min", 1), 1);
                    int max = drop.getInt("max", 10);
                    switch (type) {
                        case "blockstate": drops.add(new LootDrop(BlockState.getInstance(id.toString()).getItem(), min, max));
                        case "item": drops.add(new LootDrop(Item.getItem(id.toString()), min, max));
                    }
                }
            }
            lootTable.addDrop(weight, drops.toArray(new LootDrop[0]));
        }
        return lootTable;
    }

    public final Identifier id;

    public PuzzleLootTable(Identifier id) {
        this.id = id;
    }

    /**
     * This is a method that converts an old vanilla loot-table to a new
     * fast simple PuzzleLootTable converting all the options and elements.
     * @see Loot
     * @see LootOption
     * @see finalforeach.cosmicreach.items.loot.LootStack
     * @see LootDrop
     */
    @Contract("_ -> new")
    public static @NotNull Pair<Identifier, PuzzleLootTable> fromVanillaTable(@NotNull Loot loot) {
        Identifier lootId = Identifier.of(loot.lootId);

        PuzzleLootTable table = new PuzzleLootTable(lootId);
        for (LootOption option : loot.options) {
            LootDrop[] drops = new LootDrop[option.lootStacks.length];
            for (int i = 0; i < option.lootStacks.length; i++) {
                drops[i] = new LootDrop(
                        option.lootStacks[i].item,
                        option.lootStacks[i].minAmount,
                        option.lootStacks[i].maxAmount
                );
            }
            table.addDrop(option.weight, drops);
        }

        return new ImmutablePair<>(lootId, table);
    }

    /**
     * Add a drop option to the loot table with a simple weight attached.
     * @see LootDrop
     */
    public void addDrop(float weight, LootDrop... drops) {
        totalWeight += weight;
        lootDrops.add(new ImmutablePair<>(weight, drops));
    }

    /**
     * Add a drop option to the loot table with a min/max factor and weight but this allows you to input an item.
     * @see LootDrop
     * @see Item
     */
    public void addDrop(float weight, Item item, int min, int max) {
        totalWeight += weight;
        lootDrops.add(new ImmutablePair<>(weight, new LootDrop[]{new LootDrop(item, min, max)}));
    }

    /**
     * Adds a drop option to the loot table with a min/max factor and weight using a block state.
     * @see LootDrop
     * @see BlockState
     */
    public void addDrop(float weight, @NotNull BlockState state, int min, int max) {
        totalWeight += weight;
        lootDrops.add(new ImmutablePair<>(weight, new LootDrop[]{new LootDrop(state.getItem(), min, max)}));
    }

    float totalWeight;

    /**
     * Drops a random item in the loot table in a certain pos in the world, depending on the weight values.
     * @see LootDrop
     * @see Zone
     * @see Vector3
     */
    public void drop(Zone zone, Vector3 pos) {
        if (!this.lootDrops.isEmpty()) {
            float weightChoice = MathUtils.random(0.0F, this.totalWeight);
            float accumulativeWeight = 0.0F;

            for(Pair<Float, LootDrop[]> drops : this.lootDrops) {
                accumulativeWeight += drops.getLeft();
                if (weightChoice < accumulativeWeight) {
                    for (LootDrop drop : drops.getRight())
                        drop.drop(zone, pos);
                    break;
                }
            }

        }
    }

    List<Pair<Float, LootDrop[]>> lootDrops = new ArrayList<>();

    public static class LootDrop {

        int min;
        int max;
        Item item;

        public LootDrop(Item item, int min, int max) {
            this.min = min;
            this.max = max;
            this.item = item;
        }

        public void drop(Zone zone, Vector3 pos) {
            ItemEntity itemDrop = new ItemEntity(
                    new ItemStack(
                            item,
                            MathUtils.random(this.min, this.max)
                    )
            );
            itemDrop.setPosition(pos);
            itemDrop.velocity.setToRandomDirection().scl(1.0F);
            zone.addEntity(itemDrop);
        }

    }
}
