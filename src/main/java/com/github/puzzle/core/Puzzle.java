package com.github.puzzle.core;

import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.game.Globals;
import com.github.puzzle.game.engine.items.InstanceModelWrapper;
import com.github.puzzle.game.engine.shaders.ItemShader;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.ITickingItem;
import com.github.puzzle.game.items.puzzle.*;
import com.github.puzzle.game.oredict.tags.BuiltInTags;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PostModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PreModInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.ui.UI;

import java.io.IOException;
import java.io.InputStream;

import static finalforeach.cosmicreach.rendering.items.ItemRenderer.registerItemModelCreator;

public class Puzzle implements PreModInitializer, ModInitializer, PostModInitializer {
    public static final String MOD_ID = "puzzle-loader";
    public static final String VERSION;

    static {
        try {
            InputStream stream = getFile("assets/puzzle-loader/version.txt");
            String bytez = new String(stream.readAllBytes()).strip();
            stream.close();
            if (!bytez.contains(".")) {
                VERSION = "69.69.69";
            } else VERSION = bytez;
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

    @Override
    public void onPreInit() {
        try {
            ILanguageFile lang = LanguageFileVersion1.loadLanguageFromString(new String(getFile(Globals.LanguageEnUs.toPath()).readAllBytes()));
            LanguageManager.registerLanguageFile(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static IModItem DebugStick;
    public static IModItem CheckerBoard;
    public static IModItem BlockWrench;

    @Override
    public void onInit() {
        Threads.runOnMainThread(ItemShader::initItemShader);

        Commands.register();

        DebugStick = IModItem.registerItem(new NullStick());
        CheckerBoard = IModItem.registerItem(new CheckBoard());
        BlockWrench = IModItem.registerItem(new BlockWrench());

        Item.registerItem(new ItemInstance(null));
//        Item.registerItem(new ItemThingWrapper(null));
        IModItem.registerItem(new BuilderWand());

        registerItemModelCreator(ItemInstance.class, (inst) -> {
            return new InstanceModelWrapper(inst.get(), ItemRenderer.getModel(inst.get().getParentItem(), false));
        });

//        registerItemModelCreator(ItemThingWrapper.class, (inst) -> {
//            ItemModel model = ItemRenderer.getModel(inst.get().getParentItem(), false);

//            return new InstanceModelWrapper(inst.get(), model);
//            System.out.println(inst.get().getParentItem());
//            return new ItemThingItemModel((ItemThing) inst.get().getParentItem()).wrap();
//        });
    }

    @Override
    public void onPostInit() {
//        for (String id : Item.allItems.keys()) {
//            Item item = Item.allItems.get(id);
//
//            if (ItemThing.class.isAssignableFrom(item.getClass())) {
//                System.out.println("Reassigning -> " + item + " is Instance of " + ItemThing.class.isAssignableFrom(item.getClass()));
//                Item.allItems.put(id, new ItemThingWrapper(item));
//            }
//        }
//
        BuiltInTags.ore.add(Block.getInstance("block_ore_gold"));
        BuiltInTags.ore.add(Block.getInstance("block_ore_bauxite"));
        BuiltInTags.ore.add(Block.getInstance("block_ore_iron"));

        BuiltInTags.stone.add(Block.getInstance("block_stone_basalt"));
        BuiltInTags.stone.add(Block.getInstance("block_stone_gabbro"));
        BuiltInTags.stone.add(Block.getInstance("block_stone_limestone"));

        BuiltInTags.glass.add(Block.getInstance("block_glass"));

        BuiltInTags.grass.add(Block.getInstance("block_grass"));
        BuiltInTags.dirt.add(Block.getInstance("block_dirt"));
        BuiltInTags.dirt.add(Block.getInstance("block_lunar_soil"));
        BuiltInTags.dirt.add(Block.getInstance("block_lunar_soil_packed"));

        BuiltInTags.aluminum_block.add(Block.getInstance("block_aluminium_panel"));
        BuiltInTags.aluminum_ore.add(Block.getInstance("block_ore_bauxite"));
        BuiltInTags.aluminum_ingot.add(Item.getItem("base:ingot_aluminium"));

        BuiltInTags.iron_ore.add(Block.getInstance("block_metal_panel"));
        BuiltInTags.iron_block.add(Block.getInstance("block_ore_iron"));
        BuiltInTags.iron_ingot.add(Item.getItem("base:ingot_iron"));

        BuiltInTags.gold_ore.add(Block.getInstance("block_grass"));
        BuiltInTags.gold_block.add(Block.getInstance("block_metal_panel"));
        BuiltInTags.gold_ingot.add(Item.getItem("base:ingot_gold"));

        BuiltInTags.logs.add(Block.getInstance("block_tree_log"));
        BuiltInTags.planks.add(Block.getInstance("block_wood_planks"));
        BuiltInTags.light.add(Block.getInstance("block_light"));

        GameSingletons.updateObservers.add(fixedUpdateTimeStep -> {
            if (InGame.getLocalPlayer() != null && UI.hotbar.getContainer() != null) {
                for (int i = 0; i < UI.hotbar.getContainer().getNumSlots(); i++) {
                    ItemSlot slot = UI.hotbar.getContainer().getSlot(i);

                    if (slot != null) {
                        if (slot.itemStack != null && slot.itemStack.getItem() instanceof ItemInstance inst) {
                            if (inst.getParentItem() instanceof ITickingItem tickingItem) {
                                tickingItem.tickStack(fixedUpdateTimeStep, slot.itemStack, false);
                            }
                        }
                        if (slot.itemStack != null && slot.itemStack.getItem() instanceof ITickingItem tickingItem1) {
                            tickingItem1.tickStack(fixedUpdateTimeStep, slot.itemStack, false);
                        }
                    }
                }

                for (int ic = 0; ic < UI.openContainers.size; ic++) {
                    for (int i = 0; i < UI.openContainers.get(ic).getNumSlots(); i++) {
                        ItemSlot slot = UI.openContainers.get(ic).getSlot(i);

                        if (slot != null) {
                            if (slot.itemStack != null && slot.itemStack.getItem() instanceof ItemInstance inst) {
                                if (inst.getParentItem() instanceof ITickingItem tickingItem) {
                                    tickingItem.tickStack(fixedUpdateTimeStep, slot.itemStack, false);
                                }
                            }
                            if (slot.itemStack != null && slot.itemStack.getItem() instanceof ITickingItem tickingItem1) {
                                tickingItem1.tickStack(fixedUpdateTimeStep, slot.itemStack, false);
                            }
                        }
                    }
                }
            }
        });
    }
}
