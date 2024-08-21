package com.github.puzzle.game.items.puzzle;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.util.BlockUtil;
import finalforeach.cosmicreach.BlockSelection;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;

public class BlockWrench implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "block_wrench");
    DataTagManifest tagManifest = new DataTagManifest();

    public BlockWrench() {
        tagManifest.addTag(IModItem.MODEL_ID_PRESET.createTag(IModItem.MODEL_2_5D_ITEM));
        tagManifest.addTag(IModItem.TEXTURE_LOCATION_PRESET.createTag(new ResourceLocation(Puzzle.MOD_ID, "textures/items/block_wrench.png")));
    }

    @Override
    public void use(ItemSlot slot, Player player) {
        BlockState state = BlockSelection.getBlockLookingAt();
        BlockPosition position = BlockSelection.getBlockPositionLookingAt();
        if (state == null) return;
        if (position == null) return;
        ItemRenderer.swingHeldItem();

        BlockUtil.setBlockAt(position.getZone(), ((ItemBlock) state.getItem().getNextSwapGroupItem()).getBlockState(), position);
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

}
