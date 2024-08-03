package io.github.puzzle.game.loot;

import com.badlogic.gdx.math.MathUtils;
import com.badlogic.gdx.math.Vector3;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.ImmutablePair;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.items.loot.Loot;
import finalforeach.cosmicreach.items.loot.LootOption;
import finalforeach.cosmicreach.world.Zone;
import io.github.puzzle.core.Identifier;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;

public class PuzzleLootTable {
    public PuzzleLootTable() {
    }

    @Contract("_ -> new")
    public static @NotNull Pair<Identifier, PuzzleLootTable> fromVanillaTable(@NotNull Loot loot) {
        Identifier lootId = Identifier.fromString(loot.lootId);

        PuzzleLootTable table = new PuzzleLootTable();
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

    public void addDrop(float weight, LootDrop... drops) {
        totalWeight += weight;
        lootDrops.add(new ImmutablePair<>(weight, drops));
    }

    public void addDrop(float weight, Item item, int min, int max) {
        totalWeight += weight;
        lootDrops.add(new ImmutablePair<>(weight, new LootDrop[]{new LootDrop(item, min, max)}));
    }

    public void addDrop(float weight, @NotNull BlockState state, int min, int max) {
        totalWeight += weight;
        lootDrops.add(new ImmutablePair<>(weight, new LootDrop[]{new LootDrop(state.getItem(), min, max)}));
    }

    float totalWeight;

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
