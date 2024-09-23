package com.github.puzzle.game.items.puzzle;

import com.github.puzzle.core.Puzzle;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.ITickingItem;
import com.github.puzzle.game.items.data.DataTag;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.data.attributes.IntDataAttribute;
import com.github.puzzle.game.items.data.attributes.ListDataAttribute;
import com.github.puzzle.game.util.DataTagUtil;
import finalforeach.cosmicreach.BlockSelection;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.util.Identifier;
import finalforeach.cosmicreach.world.Zone;

public class NullStick implements IModItem, ITickingItem {

    Identifier id = Identifier.of(Puzzle.MOD_ID, "null_stick");
    DataTagManifest tagManifest = new DataTagManifest();

    int texture_count = 0;

    public NullStick() {
        tagManifest.addTag(IModItem.IS_DEBUG_ATTRIBUTE.createTag(true));

        addTexture(
            IModItem.MODEL_2_5D_ITEM,
            Identifier.of(Puzzle.MOD_ID, "null_stick.png"),
            Identifier.of("base", "axe_stone.png"),
            Identifier.of("base", "pickaxe_stone.png"),
            Identifier.of("base", "shovel_stone.png"),
            Identifier.of("base", "medkit.png"),
            Identifier.of(Puzzle.MOD_ID, "block_wrench.png"),
            Identifier.of(Puzzle.MOD_ID, "checker_board.png"),
            Identifier.of(Puzzle.MOD_ID, "checker_board1.png"),
            Identifier.of(Puzzle.MOD_ID, "checker_board2.png")
        );

        addTexture(
                IModItem.MODEL_2D_ITEM,
                Identifier.of(Puzzle.MOD_ID, "null_stick.png"),
                Identifier.of("base", "axe_stone.png"),
                Identifier.of("base", "pickaxe_stone.png"),
                Identifier.of("base", "shovel_stone.png"),
                Identifier.of("base", "medkit.png"),
                Identifier.of(Puzzle.MOD_ID, "block_wrench.png"),
                Identifier.of(Puzzle.MOD_ID, "checker_board.png"),
                Identifier.of(Puzzle.MOD_ID, "checker_board1.png"),
                Identifier.of(Puzzle.MOD_ID, "checker_board2.png")
        );

        texture_count = ((ListDataAttribute) getTagManifest().getTag("textures").attribute).getValue().size() - 1;
    }

    @Override
    public void use(ItemSlot slot, Player player) {
        BlockState state = BlockSelection.getBlockLookingAt();
        BlockPosition position = BlockSelection.getBlockPositionLookingAt();
        if (state == null) return;
        if (position == null) return;
        ItemRenderer.swingHeldItem();

//        DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
//        if (!manifest.hasTag("currentEntry")) manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(0)));
//
//        Integer currentEntry = manifest.getTag("currentEntry").getTagAsType(Integer.class).getValue();
//        currentEntry = currentEntry >= texture_count ? 0 : currentEntry + 1;
//        manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(currentEntry)));
//        DataTagUtil.setManifestOnStack(manifest, slot.itemStack);

//        OrderedMap<String, BlockState> blockStates = state.getBlock().blockStates;
//        SlotContainer c = new SlotContainer(blockStates.size);
//        UI.addOpenContainer(c);
//        for (int i = 0; i < c.numberOfSlots; i++) {
//            c.addItemStack(new ItemStack(this));
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
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(stack);
        if (!manifest.hasTag("currentEntry")) manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(0)));

        Integer currentEntry = manifest.getTag("currentEntry").getTagAsType(Integer.class).getValue();
        currentEntry = currentEntry >= texture_count ? 0 : currentEntry + 1;
        manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(currentEntry)));
        DataTagUtil.setManifestOnStack(manifest, stack);
    }

    @Override
    public void tickEntity(Zone zone, double deltaTime, ItemEntity entity, ItemStack stack) {
        DataTagManifest manifest = DataTagUtil.getManifestFromStack(stack);
        if (!manifest.hasTag("currentEntry")) manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(0)));

        Integer currentEntry = manifest.getTag("currentEntry").getTagAsType(Integer.class).getValue();
        currentEntry = currentEntry >= texture_count ? 0 : currentEntry + 1;
        manifest.addTag(new DataTag<>("currentEntry", new IntDataAttribute(currentEntry)));
        DataTagUtil.setManifestOnStack(manifest, stack);
    }

    @Override
    public String getName() {
        return "Null Stick";
    }
}
