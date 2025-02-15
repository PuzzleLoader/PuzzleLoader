package io.github.puzzle.cosmic.api.item;

import io.github.puzzle.cosmic.api.block.IPuzzleBlockPosition;
import io.github.puzzle.cosmic.api.block.IPuzzleBlockState;
import io.github.puzzle.cosmic.api.entity.player.IPuzzlePlayer;
import io.github.puzzle.cosmic.util.ChangeType;

public interface IPuzzleItemStack {

    IPuzzleItemStack copy();
    IPuzzleItem getItem();
    void setItem(IPuzzleItem item);
    void cycleSwapGroupItem();

//    IPuzzleEntity spawnItemEntityAt(IPuzzleZone zone, Vector3 pos);
    void spawnItemEntityAt(IPuzzleBlockPosition position);

    boolean useItem(IPuzzleItemSlot slot, IPuzzlePlayer player, IPuzzleBlockPosition position);

    int getDurability();
    int getMaxDurability();
    boolean hasDurability();

    void damage(int damage);
    boolean isBroken();

    boolean canTargetBlockForBreaking(IPuzzleBlockState state);
    float getEffectiveBreaking(IPuzzleBlockState state);

    IPuzzleItemStack setAmount(int amount);

    String getName();

    @ChangeType("ItemStack")
    Object as();

}
