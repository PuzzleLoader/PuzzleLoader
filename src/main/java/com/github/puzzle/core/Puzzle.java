package com.github.puzzle.core;

import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.Globals;
import com.github.puzzle.game.engine.shaders.ItemShader;
import com.github.puzzle.game.events.OnPreLoadAssetsEvent;
import com.github.puzzle.game.events.OnRegisterLanguageEvent;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.ITickingItem;
import com.github.puzzle.game.items.puzzle.*;
import com.github.puzzle.game.oredict.tags.BuiltInTags;
import com.github.puzzle.game.ui.modmenu.ConfigScreenFactory;
import com.github.puzzle.game.ui.modmenu.ModMenu;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PostModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PreModInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import com.github.puzzle.loader.mod.AdapterPathPair;
import com.github.puzzle.loader.mod.ModLocator;
import com.github.puzzle.util.PuzzleEntrypointUtil;
import com.google.common.collect.ImmutableCollection;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.ui.UI;
import org.greenrobot.eventbus.Subscribe;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;

import static finalforeach.cosmicreach.rendering.items.ItemRenderer.registerItemModelCreator;

public class Puzzle implements PreModInitializer, ModInitializer, PostModInitializer {
    public static final String MOD_ID = "puzzle-loader";
    public static final String VERSION;

    static {
        try {
            InputStream stream = getFile("/assets/puzzle-loader/version.txt");
            String bytez = new String(stream.readAllBytes()).strip();
            stream.close();
            if (!bytez.contains(".")) {
                VERSION = "69.69.69";
            } else VERSION = bytez;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public Puzzle() {
        PuzzleRegistries.EVENT_BUS.register(this);
    }

    @Subscribe
    public void onEvent(OnPreLoadAssetsEvent event) {
        try {
            ILanguageFile lang = LanguageFileVersion1.loadLanguageFile(PuzzleGameAssetLoader.locateAsset(Globals.LanguageEnUs));
            LanguageManager.registerLanguageFile(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getFile(String file) {
        InputStream input = Puzzle.class.getResourceAsStream(file);
        if (input == null) {
            input = PuzzleClassLoader.class.getClassLoader().getResourceAsStream(file);
        }
        return input;
    }

    public static IModItem DebugStick;
    public static IModItem CheckerBoard;
    public static IModItem BlockWrench;

    @Override
    public void onInit() {
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

        DebugStick = IModItem.registerItem(new NullStick());
        CheckerBoard = IModItem.registerItem(new CheckBoard());
        BlockWrench = IModItem.registerItem(new BlockWrench());

        IModItem.registerItem(new BuilderWand());
    }

    @Override
    public void onPostInit() {
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
                        if (slot.itemStack != null && slot.itemStack.getItem() instanceof ITickingItem tickingItem1) {
                            tickingItem1.tickStack(fixedUpdateTimeStep, slot.itemStack, false);
                        }
                    }
                }

                for (int ic = 0; ic < UI.openContainers.size; ic++) {
                    for (int i = 0; i < UI.openContainers.get(ic).getNumSlots(); i++) {
                        ItemSlot slot = UI.openContainers.get(ic).getSlot(i);

                        if (slot != null) {
                            if (slot.itemStack != null && slot.itemStack.getItem() instanceof ITickingItem tickingItem1) {
                                tickingItem1.tickStack(fixedUpdateTimeStep, slot.itemStack, false);
                            }
                        }
                    }
                }
            }
        });
    }

    @Override
    public void onPreInit() {

    }
}
