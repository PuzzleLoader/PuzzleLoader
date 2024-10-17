package com.github.puzzle.game.engine.stages;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.ServerGlobals;
import com.github.puzzle.game.block.DataModBlock;
import com.github.puzzle.game.block.IModBlock;
import com.github.puzzle.game.engine.GameLoader;
import com.github.puzzle.game.engine.LoadStage;
import com.github.puzzle.game.engine.ServerGameLoader;
import com.github.puzzle.game.engine.blocks.BlockLoadException;
import com.github.puzzle.game.engine.blocks.actions.OnBreakTrigger;
import com.github.puzzle.game.engine.blocks.actions.OnInteractTrigger;
import com.github.puzzle.game.engine.blocks.actions.OnPlaceTrigger;
import com.github.puzzle.game.events.OnRegisterBlockEvent;
import com.github.puzzle.game.factories.IFactory;
import com.github.puzzle.game.networking.packet.PacketInterceptor;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.resources.VanillaAssetLocations;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blockentities.BlockEntityCreator;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.items.ItemThing;
import finalforeach.cosmicreach.items.loot.Loot;
import finalforeach.cosmicreach.items.recipes.CraftingRecipes;
import finalforeach.cosmicreach.util.Identifier;
import org.greenrobot.eventbus.Subscribe;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import static finalforeach.cosmicreach.blockevents.BlockEvents.INSTANCES;

public class LoadingCosmicReach extends LoadStage {

    @Override
    public void initialize(GameLoader loader) {
        super.initialize(loader);
        title = new TranslationKey("puzzle-loader:loading_menu.loading_cosmic_reach");
    }

    @Subscribe
    public void onEvent(OnRegisterBlockEvent event) {
        List<Identifier> blockNames = new ArrayList<>(VanillaAssetLocations.getInternalFiles("blocks/", ".json"));
        if(ServerGlobals.EnabledVanillaMods.getValue()) {
            for (String space : GameAssetLoader.getAllNamespaces()) {
                blockNames.addAll(VanillaAssetLocations.getVanillaModFiles(space, "blocks/", ".json"));
            }
        }

        for(Identifier id : blockNames) {
            event.registerBlock(() -> new DataModBlock(id));
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
            INSTANCES.put(Reflection.getFieldContents(blockEvents, "stringId"), blockEvents);
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
        for (Identifier location : VanillaAssetLocations.getInternalFiles("block_events", ".json")) {
            BlockEventLocations.add(PuzzleGameAssetLoader.locateAsset(location));
        }
        BlockEventLocations.removeIf(handle -> handle.path().contains("example"));
        BlockEventLocations.addAll(List.of(Gdx.files.absolute(SaveLocation.getSaveFolderLocation() + "/mods/assets/block_events").list()));

        TranslationKey translationKey = new TranslationKey("puzzle-loader:loading_menu.creating_block_events");
        loader.setupProgressBar(loader.progressBar2, BlockEventLocations.size(), translationKey);
        int progress = 0;
        for(FileHandle handle : BlockEventLocations) {
            if (counter >= 20) {
                String str = LanguageManager.format(translationKey, progress, BlockEventLocations.size());
                loader.progressBarText2.setText(str);
                loader.progressBar2.setValue(progress);
                counter = 0;
            } else {
                counter++;
            }
            if (handle.name().endsWith(".json")) {
                getInstance(handle);
            }
            progress += 1;
        }

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
            int counterLimiter = ModLocator.locatedMods.size() >= 100 ? 10 : 3;
            if (counter >= counterLimiter) {
                String str = LanguageManager.format(translationKey, progress, blockFactories.size());
                loader.progressBarText2.setText(str);
                loader.progressBar2.setValue(progress);
                counter = 0;
            } else counter++;
            try {
                IModBlock block = blockFactory.generate();
                Identifier blockId = loader.blockLoader.loadBlock(block);
                PuzzleRegistries.BLOCKS.store(blockId, block);
            } catch (BlockLoadException e) {
                ModLocator.LOGGER.error("Cannot load block: \"{}\"", e.blockId, e);
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

        tasks.add(ItemThing::loadAll);
        tasks.add(Loot::loadLoot);
        tasks.add(CraftingRecipes::loadCraftingRecipes);
        tasks.add(BlockEntityCreator::registerBlockEntityCreators);

        return tasks;
    }
}
