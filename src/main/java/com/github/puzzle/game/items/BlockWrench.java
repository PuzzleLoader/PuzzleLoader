package com.github.puzzle.game.items;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.util.BlockUtil;
import finalforeach.cosmicreach.BlockSelection;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;

public class BlockWrench implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "block_wrench");
    DataTagManifest tagManifest = new DataTagManifest();

    public BlockWrench() {
    }

    @Override
    public void use(ItemSlot slot, Player player) {
        BlockState state = BlockSelection.getBlockLookingAt();
        BlockPosition position = BlockSelection.getBlockPositionLookingAt();
        ItemRenderer.swingHeldItem();
        System.out.println(state);
        if (state == null) return;
        if (position == null) return;
//        Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, "Interacting with block \"" + state + "\"");
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

    @Override
    public ResourceLocation getTexturePath() {
        return new ResourceLocation(Puzzle.MOD_ID, "textures/items/block_wrench.png");
    }

    @Override
    public boolean isCatalogHidden() {
        return false;
    }
}
