package com.github.puzzle.game.common;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientModInitializer;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientPostModInitializer;
import com.github.puzzle.core.loader.launch.provider.mod.entrypoint.impls.ClientPreModInitializer;
import com.github.puzzle.core.loader.provider.mod.AdapterPathPair;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.loader.util.PuzzleEntrypointUtil;
import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.game.ClientGlobals;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.engine.shaders.ItemShader;
import com.github.puzzle.game.events.OnPreLoadAssetsEvent;
import com.github.puzzle.game.items.ITickingItem;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.ui.credits.CreditFile;
import com.github.puzzle.game.ui.credits.PuzzleCreditsMenu;
import com.github.puzzle.game.ui.credits.categories.ICreditElement;
import com.github.puzzle.game.ui.credits.categories.ImageCredit;
import com.github.puzzle.game.ui.credits.categories.ListCredit;
import com.github.puzzle.game.ui.modmenu.ConfigScreenFactory;
import com.github.puzzle.game.ui.modmenu.ModMenu;
import com.google.common.collect.ImmutableCollection;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.ui.UI;
import meteordevelopment.orbit.EventHandler;

import java.io.IOException;
import java.util.Objects;

public class Puzzle implements ClientPreModInitializer, ClientModInitializer, ClientPostModInitializer {
    public static final String VERSION = Constants.getPuzzleVersion();

    public Puzzle() {
        PuzzleRegistries.EVENT_BUS.subscribe(this);
    }

    @EventHandler
    public void onEvent(OnPreLoadAssetsEvent event) {
        try {
            ILanguageFile lang = LanguageFileVersion1.loadLanguageFile(Objects.requireNonNull(PuzzleGameAssetLoader.locateAsset(ClientGlobals.LanguageEnUs)));
            LanguageManager.registerLanguageFile(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onInit() {
        ICreditElement.TYPE_TO_ELEMENT.put("image", ImageCredit.class);
        ICreditElement.TYPE_TO_ELEMENT.put("list", ListCredit.class);

        Threads.runOnMainThread(ItemShader::initItemShader);
        PuzzleEntrypointUtil.invoke("modmenu", ConfigScreenFactory.class, (configScreen) -> {
            ModLocator.locatedMods.values().forEach(modContainer -> {
                ImmutableCollection<AdapterPathPair> collection = modContainer.INFO.Entrypoints.getOrDefault("modmenu", null);
                if (collection != null) {
                    collection.forEach(adapterPathPair -> {
                        if(adapterPathPair.getValue().equals(configScreen.getClass().getName())) {
                            ModMenu.registerModConfigScreen(modContainer.ID, configScreen);
                        }
                    });
                }
            });
        });

        Commands.register();

    }

    @Override
    public void onPostInit() {
//        Threads.runOnMainThread(() -> {
//            ClientPuzzleRegistries.BLOCK_MODEL_GENERATOR_FUNCTIONS.store(Identifier.of(Constants.MOD_ID, "base_block_model_generator"), (blockId) -> {
//                BlockModelGenerator generator = new BlockModelGenerator(blockId, "model");
//                generator.createTexture("all", Identifier.of("puzzle-loader", "textures/blocks/example_block.png"));
//                generator.createCuboid(0, 0, 0, 16, 16, 16, "all");
//                return List.of(generator);
//            });
//        });

//        BuiltInTags.ore.add(Block.getInstance("base:ore_gold"));
//        BuiltInTags.ore.add(Block.getInstance("base:ore_bauxite"));
//        BuiltInTags.ore.add(Block.getInstance("base:ore_iron"));
//
//        BuiltInTags.stone.add(Block.getInstance("base:stone_basalt"));
//        BuiltInTags.stone.add(Block.getInstance("base:stone_gabbro"));
//        BuiltInTags.stone.add(Block.getInstance("base:stone_limestone"));
//
//        BuiltInTags.glass.add(Block.getInstance("base:glass"));
//
//        BuiltInTags.grass.add(Block.getInstance("base:grass"));
//        BuiltInTags.dirt.add(Block.getInstance("base:dirt"));
//        BuiltInTags.dirt.add(Block.getInstance("base:lunar_soil"));
//        BuiltInTags.dirt.add(Block.getInstance("base:lunar_soil_packed"));
//
//        BuiltInTags.aluminum_block.add(Block.getInstance("base:aluminium_panel"));
//        BuiltInTags.aluminum_ore.add(Block.getInstance("base:ore_bauxite"));
//        BuiltInTags.aluminum_ingot.add(Item.getItem("base:ingot_aluminium"));
//
//        BuiltInTags.iron_ore.add(Block.getInstance("base:metal_panel"));
//        BuiltInTags.iron_block.add(Block.getInstance("base:ore_iron"));
//        BuiltInTags.iron_ingot.add(Item.getItem("base:ingot_iron"));
//
//        BuiltInTags.gold_ore.add(Block.getInstance("base:grass"));
//        BuiltInTags.gold_block.add(Block.getInstance("base:metal_panel"));
//        BuiltInTags.gold_ingot.add(Item.getItem("base:ingot_gold"));
//
//        BuiltInTags.logs.add(Block.getInstance("base:tree_log"));
//        BuiltInTags.planks.add(Block.getInstance("base:wood_planks"));
//        BuiltInTags.light.add(Block.getInstance("base:light"));

        GameSingletons.updateObservers.add(fixedUpdateTimeStep -> {
            if (InGame.getLocalPlayer() != null && UI.hotbar.getContainer() != null) {
                for (int i = 0; i < UI.hotbar.getContainer().getNumSlots(); i++) {
                    ItemSlot slot = UI.hotbar.getContainer().getSlot(i);

                    if (slot != null) {
                        if (slot.getItemStack() != null && slot.getItemStack().getItem() instanceof ITickingItem tickingItem1) {
                            if (UI.hotbar.getSelectedSlot() == slot){
                                tickingItem1.tickStack(fixedUpdateTimeStep, slot.getItemStack(), true);
                            }else {
                                tickingItem1.tickStack(fixedUpdateTimeStep, slot.getItemStack(), false);
                            }
                        }
                    }
                }

                for (int ic = 0; ic < UI.openContainers.size; ic++) {
                    for (int i = 0; i < UI.openContainers.get(ic).getNumSlots(); i++) {
                        ItemSlot slot = UI.openContainers.get(ic).getSlot(i);

                        if (slot != null) {
                            if (slot.getItemStack() != null && slot.getItemStack().getItem() instanceof ITickingItem tickingItem1) {
                                tickingItem1.tickStack(fixedUpdateTimeStep, slot.getItemStack(), false);
                            }
                        }
                    }
                }
            }
        });

        Threads.runOnMainThread(() -> {
            PuzzleCreditsMenu.addFile(CreditFile.fromJson(PuzzleGameAssetLoader.locateAsset("puzzle-loader:credits/credits.json").readString()));
        });
    }

    @Override
    public void onPreInit() {

    }
}
