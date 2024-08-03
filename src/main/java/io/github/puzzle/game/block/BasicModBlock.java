package io.github.puzzle.game.block;

import com.badlogic.gdx.graphics.Pixmap;
import io.github.puzzle.core.Identifier;
import io.github.puzzle.game.generators.BasicCubeModelGenerator;
import io.github.puzzle.game.generators.BlockGenerator;
import io.github.puzzle.game.generators.BlockModelGenerator;

import java.util.List;

/**
 * @see IModBlock
 * This class creates a basic Block with top, bottom and side
 * textures, a simple default block state and a single custom
 * model
 */
public class BasicModBlock implements IModBlock {

    public Identifier blockId;
    public String blockName;
    public Pixmap top, bottom, side;

    public BasicModBlock(Identifier blockId, String blockName) {
        this.blockId = blockId;
        this.blockName = blockName;
    }

    @Override
    public BlockGenerator getBlockGenerator() {
        BlockGenerator generator = new BlockGenerator(blockId, blockName);
        generator.createBlockState("default", "model", true);
        return generator;
    }

    @Override
    public List<BlockModelGenerator> getBlockModelGenerators(Identifier blockId) {
        BlockModelGenerator generator = new BasicCubeModelGenerator(blockId, "model", true, top, bottom, side);
        return List.of(generator);
    }

}