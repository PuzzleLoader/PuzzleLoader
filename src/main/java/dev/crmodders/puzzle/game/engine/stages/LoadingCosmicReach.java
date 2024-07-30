package dev.crmodders.puzzle.game.engine.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.crmodders.puzzle.core.Identifier;
import dev.crmodders.puzzle.core.PuzzleRegistries;
import dev.crmodders.puzzle.core.localization.LanguageManager;
import dev.crmodders.puzzle.core.localization.TranslationKey;
import dev.crmodders.puzzle.core.resources.ResourceLocation;
import dev.crmodders.puzzle.core.resources.VanillaAssetLocations;
import dev.crmodders.puzzle.game.Globals;
import dev.crmodders.puzzle.game.block.DataModBlock;
import dev.crmodders.puzzle.game.block.IModBlock;
import dev.crmodders.puzzle.game.engine.GameLoader;
import dev.crmodders.puzzle.game.engine.LoadStage;
import dev.crmodders.puzzle.game.engine.blocks.BlockLoadException;
import dev.crmodders.puzzle.game.engine.blocks.actions.OnBreakTrigger;
import dev.crmodders.puzzle.game.engine.blocks.actions.OnInteractTrigger;
import dev.crmodders.puzzle.game.engine.blocks.actions.OnPlaceTrigger;
import dev.crmodders.puzzle.game.events.OnRegisterBlockEvent;
import dev.crmodders.puzzle.game.factories.IFactory;
import dev.crmodders.puzzle.game.ui.TranslationParameters;
import dev.crmodders.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blockentities.BlockEntityCreator;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.items.loot.Loot;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static dev.crmodders.puzzle.loader.mod.ModLocator.LOGGER;
import static finalforeach.cosmicreach.blockevents.BlockEvents.INSTANCES;

public class LoadingCosmicReach extends LoadStage {

    @Override
    public void initialize(GameLoader loader) {
        super.initialize(loader);
        title = new TranslationKey("puzzle-loader:loading_menu.loading_cosmic_reach");
    }

    @Subscribe
    public void onEvent(OnRegisterBlockEvent event) {
        List<String> blockNames = new ArrayList<>();
        for(ResourceLocation internal : VanillaAssetLocations.getInternalFiles("blocks/", ".json")) {
            blockNames.add(internal.name.replace("blocks/", "").replace(".json", ""));
        }
        if(Globals.EnabledVanillaMods.getValue()) {
            for(ResourceLocation internal : VanillaAssetLocations.getVanillaModFiles("blocks/", ".json")) {
                blockNames.add(internal.name.replace("blocks/", "").replace(".json", ""));
            }
        }

        for(String blockName : blockNames) {
            event.registerBlock(() -> new DataModBlock(blockName));
        }
    }

    public static BlockEvents getInstance(FileHandle handle) {
        String blockEventId = handle.nameWithoutExtension();
        if (blockEventId == null && !"base:block_events_default".equals(blockEventId)) {
            return BlockEvents.getInstance("base:block_events_default");
        } else if (INSTANCES.containsKey(blockEventId)) {
            return INSTANCES.get(blockEventId);
        } else {
            String jsonStr = handle.readString();
            Json json = new Json();
            json.setSerializer(Vector3.class, new Json.Serializer<>() {
                public void write(Json json, Vector3 object, Class knownType) {
                    json.writeValue(new float[]{object.x, object.y, object.z});
                }

                public Vector3 read(Json json, JsonValue jsonData, Class type) {
                    float[] f = jsonData.asFloatArray();
                    return new Vector3(f);
                }
            });
            BlockEvents blockEvents = json.fromJson(BlockEvents.class, jsonStr);
            try {
                INSTANCES.put(Reflection.getField(blockEvents, "stringId"), blockEvents);
            } catch (NoSuchFieldException | IllegalAccessException e) {
                throw new RuntimeException(e);
            }
            return blockEvents;
        }
    }

    int counter = 0;
    @Override
    public void doStage() {
        super.doStage();

        // Load Block Event Actions
        BlockEvents.initBlockEvents();

        List<FileHandle> BlockEventLocations = new ArrayList<>();
        for (ResourceLocation location : VanillaAssetLocations.getInternalFiles("block_events", ".json")) {
            BlockEventLocations.add(location.locate());
        }
        BlockEventLocations.removeIf(handle -> handle.path().contains("example"));
        BlockEventLocations.addAll(List.of(Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/block_events").list()));

        TranslationKey translationKey = new TranslationKey("puzzle-loader:loading_menu.creating_block_events");
        loader.setupProgressBar(loader.progressBar2, BlockEventLocations.size(), translationKey);
        int progress = 0;
        loader.progressBar2.setValue(100);
        for(FileHandle handle : BlockEventLocations) {
            if (counter >= 10) {
                String str = LanguageManager.format(translationKey, progress, BlockEventLocations.size());
                loader.progressBarText2.setText(str);
                counter = 0;
            } else {
                counter++;
            }
            loader.progressBar2.setValue(loader.progressBar2.getValue() + 1);
            if (handle.name().endsWith(".json")) {
                getInstance(handle);
            }
            progress += 1;
        }

        BlockEntityCreator.registerBlockEntityCreators();

        BlockEvents.registerBlockEventAction(OnPlaceTrigger.class);
        BlockEvents.registerBlockEventAction(OnBreakTrigger.class);
        BlockEvents.registerBlockEventAction(OnInteractTrigger.class);

        List<IFactory<IModBlock>> blockFactories = new ArrayList<>();
        PuzzleRegistries.EVENT_BUS.post(new OnRegisterBlockEvent(blockFactories));

        translationKey = new TranslationKey("puzzle-loader:loading_menu.creating_blocks");
        loader.setupProgressBar(loader.progressBar2, blockFactories.size(), translationKey);
        counter = 0;
        progress = 0;
        for(IFactory<IModBlock> blockFactory : blockFactories) {
            if (counter >= 10) {
                String str = LanguageManager.format(translationKey, progress, blockFactories.size());
                loader.progressBarText2.setText(str);
                counter = 0;
            } else {
                counter++;
            }
            try {
                IModBlock block = blockFactory.generate();
                Identifier blockId = loader.blockLoader.loadBlock(block);
                PuzzleRegistries.BLOCKS.store(blockId, block);
            } catch (BlockLoadException e) {
                LOGGER.error("Cannot load block: \"{}\"", e.blockName, e);
                loader.blockLoader.errors.add(e);
            }
            progress++;
        }
        PuzzleRegistries.BLOCKS.freeze();

        loader.blockLoader.registerFinalizers();
        loader.blockLoader.hookOriginalBlockConstants();
    }

    @Override
    public List<Runnable> getGlTasks() {
        List<Runnable> tasks = super.getGlTasks();

        Set<Identifier> modelIds = PuzzleRegistries.BLOCK_MODEL_FINALIZERS.names();
        Set<Identifier> blockStateIds = PuzzleRegistries.BLOCK_FINALIZERS.names();

        tasks.add( () -> loader.setupProgressBar(loader.progressBar2, modelIds.size(), "Creating Models") );
        for(Identifier modelId : modelIds) {
            tasks.add( () -> loader.incrementProgress(loader.progressBar2, modelId.toString()) );
            tasks.add( PuzzleRegistries.BLOCK_MODEL_FINALIZERS.get(modelId) );
        }


        tasks.add( () -> loader.setupProgressBar(loader.progressBar2, blockStateIds.size(), "Finalizing Blocks") );
        for(Identifier blockStateId : blockStateIds) {
            tasks.add( () -> loader.incrementProgress(loader.progressBar2, blockStateId.toString()) );
            tasks.add( PuzzleRegistries.BLOCK_FINALIZERS.get(blockStateId) );
        }

        tasks.add(Loot::loadLoot);

        return tasks;
    }
}
