package com.github.puzzle.game.block;

import finalforeach.cosmicreach.util.Identifier;
import com.github.puzzle.game.engine.blocks.BlockLoader;
import com.github.puzzle.game.generators.BlockEventGenerator;
import com.github.puzzle.game.generators.BlockGenerator;
import com.github.puzzle.game.generators.BlockModelGenerator;
import com.github.puzzle.game.util.BlockEventActionFactory;
import finalforeach.cosmicreach.blockevents.actions.BlockActionItemDrop;
import finalforeach.cosmicreach.blockevents.actions.BlockActionPlaySound2D;
import finalforeach.cosmicreach.blockevents.actions.BlockActionReplaceBlockState;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.world.Zone;

import java.util.Collections;
import java.util.List;

/**
 * Adds a fully customizable block to Cosmic Reach
 */
public interface IModBlock {

    Identifier getIdentifier();

    /**
     * Triggered when a Player Interacts with a block
     * @param zone The Zone that this block is in
     * @param player The Player that interacted with this block
     * @param blockState The Blocks State
     * @param position The Blocks Position
     */
    default void onInteract(Zone zone, Player player, BlockState blockState, BlockPosition position) {}

    /**
     * Triggered when a Player Places this block
     * @param zone The Zone that this block is in
     * @param player The Player that interacted with this block
     * @param blockState The Blocks State
     * @param position The Blocks Position
     */
    default void onPlace(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        BlockActionReplaceBlockState replace = BlockEventActionFactory.createReplaceBlockEvent("self", 0 ,0, 0);
        replace.act(blockState, null, zone, position);
        BlockActionPlaySound2D sound2D = BlockEventActionFactory.createPlaySound2D("block-place.ogg", 1, 1, 0);
        sound2D.act(blockState, null, zone);
    }

    /**
     * Triggered when a Player Breaks this block
     * @param zone The Zone that this block is in
     * @param player The Player that interacted with this block
     * @param blockState The Blocks State
     * @param position The Blocks Position
     */
    default void onBreak(Zone zone, Player player, BlockState blockState, BlockPosition position) {
        BlockActionReplaceBlockState replace = BlockEventActionFactory.createReplaceBlockEvent("base:air[default]", 0 ,0, 0);
        replace.act(blockState, null, zone, position);
        BlockActionPlaySound2D sound2D = BlockEventActionFactory.createPlaySound2D("block-break.ogg", 1, 1, 0);
        sound2D.act(blockState, null, zone);
        BlockActionItemDrop drop = new BlockActionItemDrop();
        drop.act(blockState, null, zone, position);
    }

    /**
     * Used by Puzzle Loader for generating this block and registering it with Cosmic Reach
     * @return The {@link BlockGenerator} that is used for generating this Block
     */
    default BlockGenerator getBlockGenerator() {
        Identifier identifier = getIdentifier();
        BlockGenerator generator = new BlockGenerator(identifier);
        BlockGenerator.State state = generator.createBlockState("default", "model", true);
        state.dropId = identifier.getNamespace() + ":" + identifier.getName() + "[default]";
        state.blockEventsId = "base:block_events_default";
        return generator;
    }

    /**
     * Used by Puzzle Loader for generating any custom block models associated
     * with this block
     * @param blockId the blockId that has been extracted from getBlockGenerator()
     * @return a List of BlockModelGenerator used by this block
     */
    default List<BlockModelGenerator> getBlockModelGenerators(Identifier blockId) {
        BlockModelGenerator generator = new BlockModelGenerator(blockId, "model");
        generator.createTexture("all", Identifier.of("puzzle-loader", "textures/blocks/example_block.png"));
        generator.createCuboid(0, 0, 0, 16, 16, 16, "all");
        return List.of(generator);
    }

    /**
     * Used by Puzzle Loader for generating any custom block events associated
     * with this block, FluxAPI will register onInteract, onPlace and onBreak events by default see {@link IModBlock}
     * For registering Block Actions see {@link BlockLoader#registerEventAction}
     * @param blockId the blockId that has been extracted from getBlockGenerator()
     * @return a List of BlockEventGenerator used by this block
     */
    default List<BlockEventGenerator> getBlockEventGenerators(Identifier blockId) { return Collections.emptyList(); }

}