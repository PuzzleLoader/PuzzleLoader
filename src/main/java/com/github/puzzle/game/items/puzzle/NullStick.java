package com.github.puzzle.game.items.puzzle;

import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.ResourceLocation;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.ITickingItem;
import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.data.attributes.*;
import com.github.puzzle.game.util.DataTagUtil;
import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.world.Zone;

public class NullStick implements IModItem, ITickingItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "null_stick");
    DataTagManifest tagManifest = new DataTagManifest();

    public NullStick() {
        tagManifest.addTag(IModItem.IS_DEBUG_ATTRIBUTE.createTag(false));

        addTexture(IModItem.MODEL_2_5D_ITEM, new ResourceLocation(Puzzle.MOD_ID, "textures/items/null_stick.png"));
        addTexture(IModItem.MODEL_2D_ITEM, new ResourceLocation(Puzzle.MOD_ID, "textures/items/null_stick.png"));
        addTexture(IModItem.MODEL_2_5D_ITEM, new ResourceLocation("base", "textures/items/axe_stone.png"));
        addTexture(IModItem.MODEL_2D_ITEM, new ResourceLocation("base", "textures/items/axe_stone.png"));
        addTexture(IModItem.MODEL_2_5D_ITEM, new ResourceLocation("base", "textures/items/pickaxe_stone.png"));
        addTexture(IModItem.MODEL_2D_ITEM, new ResourceLocation("base", "textures/items/pickaxe_stone.png"));
        addTexture(IModItem.MODEL_2_5D_ITEM, new ResourceLocation("base", "textures/items/shovel_stone.png"));
        addTexture(IModItem.MODEL_2D_ITEM, new ResourceLocation("base", "textures/items/shovel_stone.png"));
    }

    @Override
    public void use(ItemSlot slot, Player player) {
//        BlockState state = BlockSelection.getBlockLookingAt();
//        BlockPosition position = BlockSelection.getBlockPositionLookingAt();
//        if (state == null) return;
//        if (position == null) return;
//        ItemRenderer.swingHeldItem();

        DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
        if (!manifest.hasTag("currentEntry")) manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(0)));

        Integer currentEntry = manifest.getTag("currentEntry").getTagAsType(Integer.class).getValue();
        currentEntry = currentEntry >= 7 ? 0 : currentEntry + 1;
        manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(currentEntry)));
        DataTagUtil.setManifestOnStack(manifest, slot.itemStack);

//        OrderedMap<String, BlockState> blockStates = state.getBlock().blockStates;
//        SlotContainer c = new SlotContainer(blockStates.size);
//        UI.addOpenContainer(c);
//        for (int i = 0; i < c.numberOfSlots; i++) {
//            c.addItemStack(new ItemStack(blockStates.values().toArray(new Array<>()).get(i).getItem()));
//        }

//        BlockUtil.setBlockAt(position.getZone(), ((ItemBlock) state.getItem().getNextSwapGroupItem()).getBlockState(), position);
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
    public void tickStack(float fixedUpdateTimeStep, ItemStack stack, boolean isBeingHeld) {
        if (isBeingHeld) System.out.println("I AM A REAL TOOL NOW :D");
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(stack);
        if (!manifest.hasTag("currentEntry")) manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(0)));

        Integer currentEntry = manifest.getTag("currentEntry").getTagAsType(Integer.class).getValue();
        currentEntry = currentEntry >= 7 ? 0 : currentEntry + 1;
        manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(currentEntry)));
        DataTagUtil.setManifestOnStack(manifest, stack);
    }

    @Override
    public void tickEntity(Zone zone, double deltaTime, ItemEntity entity, ItemStack stack) {
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(stack);

        Integer currentEntry = manifest.getTag("currentEntry").getTagAsType(Integer.class).getValue();
        currentEntry = currentEntry >= 7 ? 0 : currentEntry + 1;
        manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(currentEntry)));
        DataTagUtil.setManifestOnStack(manifest, stack);
    }


}
