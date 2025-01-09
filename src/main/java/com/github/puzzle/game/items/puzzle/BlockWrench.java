package com.github.puzzle.game.items.puzzle;

import com.github.puzzle.core.Constants;
import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.util.BlockSelectionUtil;
import com.github.puzzle.game.util.BlockUtil;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.util.Identifier;

import static com.github.puzzle.core.Constants.MOD_ID;

public class BlockWrench implements IModItem {

    Identifier id = Identifier.of(MOD_ID, "block_wrench");
    DataTagManifest tagManifest = new DataTagManifest();

    public BlockWrench() {
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(Identifier.of(MOD_ID, "block_wrench.png")));
    }

    @Override
    public void use(ItemSlot slot, Player player, BlockPosition targetPlaceBlockPos, BlockPosition targetBreakBlockPos) {
        BlockState state = targetBreakBlockPos.getBlockState();
        if (state == null) return;
        if (targetBreakBlockPos == null) return;

        BlockUtil.setBlockAt(targetBreakBlockPos.getZone(), ((ItemBlock) state.getItem().getNextSwapGroupItem()).getBlockState(), targetBreakBlockPos);
    }

    @Override
    public String toString() {
        return "Item: " + getID();
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public boolean isTool() {
        return true;
    }

    @Override
    public int getMaxStackSize() {
        return 1;
    }

    @Override
    public DataTagManifest getTagManifest() {
        return tagManifest;
    }

    @Override
    public String getName() {
        return "State Wrench";
    }

    @Override
    public boolean canBreakBlockWith(BlockState blockState) {
        return false;
    }
}
