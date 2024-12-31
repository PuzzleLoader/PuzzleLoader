package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.graphics.Pixmap;
import com.badlogic.gdx.utils.Json;
import com.github.puzzle.core.loader.util.AnsiColours;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.block.IModBlock;
import com.github.puzzle.game.block.PuzzleBlockAction;
import com.github.puzzle.game.block.generators.BlockEventGenerator;
import com.github.puzzle.game.block.generators.BlockGenerator;
import com.github.puzzle.game.block.generators.model.BlockModelGenerator;
import com.github.puzzle.game.engine.blocks.models.PuzzleBlockModel;
import com.github.puzzle.game.factories.IFactory;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.util.Identifier;
import sun.misc.Unsafe;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.function.BiConsumer;

public class ClientBlockLoader implements IBlockLoader {

    public IBlockModelFactory factory;

    public ClientBlockLoader() {
        factory = new BlockModelFactory();
    }

    public Json json = new Json();
    public List<BlockLoadException> errors = new ArrayList<>();

    /**
     * Call this method to register custom json models, this has to be called
     * before loading the block, else it will try to load it from disk, possibly
     * crashing the game or resulting in you block being replaced by the missing
     * block
     * @param modelName name of the model
     * @param rotXZ how to rotate the model valid values: 0, 90, 180, 270
     * @param modelJson regular json model from DataMods
     */
    public void registerBlockModel(String modelName, int rotXZ, String modelJson) {
        factory.createFromJson(modelName, rotXZ, modelJson);
    }

    /**
     * Call this method to register custom textures instead of loading pngs from disk,
     * this has to be called before loading the block, else it will possibly crash the
     * game, due to it trying to load the texture from disk
     * @param textureName name of the texture, these are global be warned
     *                    about name collision, flux's block generator will
     *                    use a combination of model and texture names here
     * @param texture a pixmap representing your texture, this has to follow the guidelines
     *                from data modding, width and height have to be equal
     *                Note: this method does not take ownership of the Pixmap
     */
    public void registerTexture(String textureName, Pixmap texture) {
        CustomTextureLoader.registerTex(textureName, texture);
    }

    public void registerTexture(Identifier texture) {
        Pixmap pixmap = PuzzleGameAssetLoader.LOADER.loadResourceSync(texture, Pixmap.class);
        CustomTextureLoader.registerTex(texture.toString(), pixmap);
        PuzzleGameAssetLoader.LOADER.unloadResource(texture);
    }

    /**
     * Call this method to register custom block events instead of loading them from
     * json files
     * @param eventName the id
     * @param eventJson the json
     */
    public void registerEvent(String eventName, String eventJson) {
        Json json = new Json();
        BlockEvents blockEvents = json.fromJson(BlockEvents.class, eventJson);
        BlockEvents.INSTANCES.put(eventName, blockEvents);

        LOGGER.info("Registered event with ID {}\"{}\"", AnsiColours.BLUE, eventName);
    }

    /**
     * Registers a block event action
     * @param actionId the id
     * @param action the action
     */
    public void registerEventAction(Identifier actionId, IFactory<PuzzleBlockAction> action) {
        PuzzleRegistries.BLOCK_EVENT_ACTION_FACTORIES.store(actionId, action);
    }

    /**
     * Call this method to load a block, it will use cached models and textures,
     * like those registered by registerBlockModel and registerTexture
     * @param modBlock the block to be generated
     * @return the block id extracted from the generated json
     */
    public Identifier loadBlock(IModBlock modBlock) {
        BlockGenerator blockGenerator;
        try {
            blockGenerator = modBlock.getBlockGenerator();
        } catch (Exception e) {
            throw new BlockLoadException(modBlock, null, null, null, e);
        }

        String blockJson;
        try {
            blockGenerator.register(this);
            blockJson = blockGenerator.generateJson();
        } catch (Exception e) {
            throw new BlockLoadException(modBlock, blockGenerator.blockId, null, null, e);
        }

        Block block;
        try {
            block = json.fromJson(Block.class, blockJson);
        } catch (Exception e) {
            throw new BlockLoadException(modBlock, blockGenerator.blockId, blockJson, null, e);
        }

        try {
            for(BlockModelGenerator modelGenerator : modBlock.getBlockModelGenerators(blockGenerator.blockId)) {
                modelGenerator.register(this);
                String modelName = modelGenerator.getModelName();
                int rotXZ = 0;
                String modelJson = modelGenerator.generateJson();
                registerBlockModel(modelName, rotXZ, modelJson);
            }

            List<BlockEventGenerator> eventGenerators = modBlock.getBlockEventGenerators(blockGenerator.blockId);
            if(eventGenerators.isEmpty()) {
                BlockEventGenerator eventGenerator = new BlockEventGenerator(blockGenerator.blockId, "puzzle_default");
                eventGenerators = List.of(eventGenerator);
            }

            for(BlockEventGenerator eventGenerator : eventGenerators) {
                eventGenerator.createTrigger("onInteract", Identifier.of("puzzle-loader:mod_block_interact"), Map.of("blockId", blockGenerator.blockId));
                eventGenerator.createTrigger("onPlace", Identifier.of("puzzle-loader:mod_block_place"), Map.of("blockId", blockGenerator.blockId));
                eventGenerator.createTrigger("onBreak", Identifier.of("puzzle-loader:mod_block_break"), Map.of("blockId", blockGenerator.blockId));
                eventGenerator.register(this);
                String eventName = eventGenerator.getEventName();
                String eventJson = eventGenerator.generateJson();
                registerEvent(eventName, eventJson);
            }

            for (String stateKey : block.blockStates.keys().toArray()) {
                BlockState blockState = block.blockStates.get(stateKey);
                blockState.stringId = stateKey;
                blockState.initialize(block);
                Block.allBlockStates.put(blockState.stringId, blockState);
            }
            Block.blocksByStringId.put(blockGenerator.blockId.toString(), block);
        } catch (Exception e) {
            for(BlockState blockState : block.blockStates.values()) {
                Block.allBlockStates.remove(blockState.stringId);
            }
            Block.allBlocks.removeValue(block, true);
            Block.blocksByStringId.remove(blockGenerator.blockId.toString());
            throw new BlockLoadException(modBlock, blockGenerator.blockId, blockJson, block, e);
        }
        return blockGenerator.blockId;
    }

    @Override
    public List<BlockLoadException> getErrors() {
        return errors;
    }

    public void registerFinalizers() {

        LOGGER.info("Registering Block Model finalizers");

        // initialize models, fewer parents first order
        // it's very critical that registries are run in order here
        for (BlockModel model : factory.sort()) {
            if (model instanceof PuzzleBlockModel m) {
                PuzzleRegistries.BLOCK_MODEL_FINALIZERS.store(Identifier.of(m.getModelName() + "_" + m.rotXZ), m::initialize);
            }
        }
        PuzzleRegistries.BLOCK_MODEL_FINALIZERS.freeze();

        LOGGER.info("Registering Block finalizers");

        // fix culling flags
        for(Block block : Block.allBlocks) {
            for(BlockState blockState : block.blockStates.values()) {
                try {
                    if (blockState.getModel() instanceof PuzzleBlockModel model) {
                        String blockStateId = block.getStringId() + "[" + blockState.stringId + "]";
                        PuzzleRegistries.BLOCK_FINALIZERS.store(Identifier.of(blockStateId), () -> blockState.setBlockModel(model.modelName));
                    }
                } catch (Exception ignored) {}
            }
        }
        PuzzleRegistries.BLOCK_FINALIZERS.freeze();

    }

    /**
     * This hooks the original block constants as those are not loaded statically
     * anymore, this has to be called after all blocks from the vanilla game are loaded
     */
    public void hookOriginalBlockConstants() {
        BiConsumer<String, Block> setBlockStaticFinalField = (name, block) -> {
            try {
                Field unsafeField = Unsafe.class.getDeclaredField("theUnsafe");
                unsafeField.setAccessible(true);
                Unsafe verySafeClassThatIsVeryUseful = (Unsafe) unsafeField.get(null);

                Field field = Block.class.getDeclaredField(name);
                Object fieldBase = verySafeClassThatIsVeryUseful.staticFieldBase(field);
                long fieldOffset = verySafeClassThatIsVeryUseful.staticFieldOffset(field);
                verySafeClassThatIsVeryUseful.putObject(fieldBase, fieldOffset, block);
            } catch (IllegalAccessException | NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        };

        LOGGER.info("Hooking original block constants");

        setBlockStaticFinalField.accept("AIR", Block.getInstance("base:air"));
        setBlockStaticFinalField.accept("GRASS", Block.getInstance("base:grass"));
        setBlockStaticFinalField.accept("STONE_BASALT", Block.getInstance("base:stone_basalt"));
        setBlockStaticFinalField.accept("DIRT", Block.getInstance("base:dirt"));
        setBlockStaticFinalField.accept("WOODPLANKS", Block.getInstance("base:wood_planks"));
        setBlockStaticFinalField.accept("CRATE_WOOD", Block.getInstance("base:crate_wooden"));
        setBlockStaticFinalField.accept("HAZARD", Block.getInstance("base:hazard"));
        setBlockStaticFinalField.accept("SAND", Block.getInstance("base:sand"));
        setBlockStaticFinalField.accept("TREELOG", Block.getInstance("base:tree_log"));
        setBlockStaticFinalField.accept("LEAVES", Block.getInstance("base:leaves"));
        setBlockStaticFinalField.accept("COCONUT", Block.getInstance("base:coconut"));
        setBlockStaticFinalField.accept("SNOW", Block.getInstance("base:snow"));
        setBlockStaticFinalField.accept("WATER", Block.getInstance("base:water"));
        setBlockStaticFinalField.accept("LUNAR_SOIL", Block.getInstance("base:lunar_soil"));
    }

}