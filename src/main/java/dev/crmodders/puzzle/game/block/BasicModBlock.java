package dev.crmodders.puzzle.game.block;

import com.badlogic.gdx.graphics.Pixmap;
import dev.crmodders.puzzle.core.Identifier;
import dev.crmodders.puzzle.game.generators.BasicCubeModelGenerator;
import dev.crmodders.puzzle.game.generators.BlockGenerator;
import dev.crmodders.puzzle.game.generators.BlockModelGenerator;

import java.util.List;

/**
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