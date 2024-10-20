package com.github.puzzle.game.block;

import com.github.puzzle.core.Constants;
import com.github.puzzle.game.block.generators.BlockEventGenerator;
import com.github.puzzle.game.block.generators.BlockGenerator;
import com.github.puzzle.game.util.BlockEventActionFactory;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.blockevents.actions.BlockActionItemDrop;
import finalforeach.cosmicreach.blockevents.actions.BlockActionPlaySound2D;
import finalforeach.cosmicreach.blockevents.actions.BlockActionReplaceBlockState;
import finalforeach.cosmicreach.util.Identifier;

import java.util.Collections;
import java.util.List;

/**
 * Adds a fully customizable block to Cosmic Reach
 */
public interface IModBlock {

    Identifier getIdentifier();

    /**
     * Triggered when a Player Interacts with a block
     * @param args The arguments for the blockEvent.
     */
    default void onInteract(BlockEventArgs args) {}

    /**
     * Triggered when a Player Places this block
     * @param args The arguments for the blockEvent.
     */
    default void onPlace(BlockEventArgs args) {
        BlockActionReplaceBlockState replace = BlockEventActionFactory.createReplaceBlockEvent("self", 0 ,0, 0);
        replace.act(args);
        BlockActionPlaySound2D sound2D = BlockEventActionFactory.createPlaySound2D("block-place.ogg", 1, 1, 0);
        sound2D.act(args);
    }

    /**
     * Triggered when a Player Breaks this block
     * @param args The arguments for the blockEvent.
     */
    default void onBreak(BlockEventArgs args) {
        BlockActionReplaceBlockState replace = BlockEventActionFactory.createReplaceBlockEvent("base:air[default]", 0 ,0, 0);
        replace.act(args);
        BlockActionPlaySound2D sound2D = BlockEventActionFactory.createPlaySound2D("block-break.ogg", 1, 1, 0);
        sound2D.act(args);
        BlockActionItemDrop drop = new BlockActionItemDrop();
        drop.act(args);
    }

    /**
     * Used by Puzzle Loader for generating this block and registering it with Cosmic Reach
     * @return The {@link BlockGenerator} that is used for generating this Block
     */
    default BlockGenerator getBlockGenerator() {
        Identifier identifier = getIdentifier();
        BlockGenerator generator = new BlockGenerator(identifier);
        BlockGenerator.State state = generator.createBlockState("default", "model", true, Identifier.of(Constants.MOD_ID, "base_block_model_generator"));
        state.dropId = identifier.getNamespace() + ":" + identifier.getName() + "[default]";
        state.blockEventsId = "base:block_events_default";
        return generator;
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