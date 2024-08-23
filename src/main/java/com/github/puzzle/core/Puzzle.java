package com.github.puzzle.core;

import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.game.Globals;
import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.PuzzleCommandSource;
import com.github.puzzle.game.engine.items.InstanceModelWrapper;
import com.github.puzzle.game.engine.shaders.ItemShader;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.ITickingItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.puzzle.BlockWrench;
import com.github.puzzle.game.items.puzzle.CheckBoard;
import com.github.puzzle.game.items.puzzle.ItemInstance;
import com.github.puzzle.game.items.puzzle.NullStick;
import com.github.puzzle.game.oredict.tags.BuiltInTags;
import com.github.puzzle.game.util.DataTagUtil;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PostModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PreModInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.blocks.Block;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.rendering.items.ItemModel;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.ui.UI;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.HashMap;

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

        LiteralArgumentBuilder<PuzzleCommandSource> getAttributes = CommandManager.literal("getattributes");
        getAttributes.executes(context -> {
            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                ItemSlot slot = UI.hotbar.getSelectedSlot();
                if (slot.itemStack == null) return 0;
                DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
                Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, manifest.toString());
            }
            return 0;
        });
        CommandManager.dispatcher.register(getAttributes);

        LiteralArgumentBuilder<PuzzleCommandSource> getItemAttribs = CommandManager.literal("getitemattributes");
        getItemAttribs.executes(context -> {
            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                ItemSlot slot = UI.hotbar.getSelectedSlot();
                if (slot.itemStack == null) return 0;
                if (slot.itemStack.getItem() instanceof IModItem item) {
                    Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, item.getTagManifest().toString());
                    return 0;
                }
                Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, "Not a ModItem");
            }
            return 0;
        });
        CommandManager.dispatcher.register(getItemAttribs);

        DebugStick = IModItem.registerItem(new NullStick());
        CheckerBoard = IModItem.registerItem(new CheckBoard());
        BlockWrench = IModItem.registerItem(new BlockWrench());

        Item.registerItem(new ItemInstance(null));

        registerItemModelCreator(ItemInstance.class, (inst) -> {
//            ObjectMap<Class<? extends Item>, Function<?, ItemModel>> modelCreators = Reflection.getFieldContents(ItemRenderer.class, "modelCreators");
//            ObjectMap<WeakReference<Item>, ItemModel> models = Reflection.getFieldContents(ItemRenderer.class, "models");
//
//            Item item = Objects.requireNonNull(inst.get()).getParentItem();
//            WeakReference<Item> ref = ItemRenderAccessor.getRefMap().get(item);
//
//            Function<WeakReference<Item>, ItemModel> itemModelFunction = (Function<WeakReference<Item>, ItemModel>) modelCreators.get(item.getClass());
//            ItemModel model = itemModelFunction.apply(ref);
//            models.put(ref, model);
//            Reflection.setFieldContents(ItemRenderer.class, "models", models);
//            return model;
//            if (Objects.requireNonNull(inst.get()).getParentItem() instanceof IModItem modItem) {
//                if (!modelHashMap.containsKey(modItem.getClass())) {
//                    Threads.runOnMainThread(() -> {
////                        ObjectMap<WeakReference<Item>, ItemModel> models = Reflection.getFieldContents(ItemRenderer.class, "models");
////
////                        Puzzle.references.put(modItem.getClass(), new WeakReference<>(modItem));
////                        Puzzle.references2.put(Puzzle.references.get(modItem.getClass()).get(), Puzzle.references.get(modItem.getClass()));
////                        modelHashMap.put(modItem.getClass(), new ExperimentalItemModel(references.get(modItem.getClass()), modItem).wrap());
////                        WeakHashMap<Item, WeakReference<Item>> I = ItemRenderAccessor.getRefMap();
////                        I.put(modItem, Puzzle.references.get(modItem.getClass()));
////                        ItemRenderAccessor.setRefMap(I);
////                        models.put(Puzzle.references.get(modItem.getClass()), modelHashMap.get(modItem.getClass()));
////                        Reflection.setFieldContents(ItemRenderer.class, "models", models);
//                    });
//                }
//                return ;
//            }
            return new InstanceModelWrapper(inst.get(), ItemRenderer.getModel(inst.get().getParentItem(), true));
        });
    }

    @Override
    public void onPostInit() {
        BuiltInTags.stone.add(Block.getInstance("block_stone_basalt"));
        BuiltInTags.stone.add(Block.getInstance("block_stone_gabbro"));
        BuiltInTags.stone.add(Block.getInstance("block_stone_limestone"));

        BuiltInTags.glass.add(Block.getInstance("block_glass"));

        BuiltInTags.grass.add(Block.getInstance("block_grass"));
        BuiltInTags.dirt.add(Block.getInstance("block_dirt"));
        BuiltInTags.dirt.add(Block.getInstance("block_lunar_soil"));
        BuiltInTags.dirt.add(Block.getInstance("block_lunar_soil_packed"));

        BuiltInTags.aluminum_block.add(Block.getInstance("block_aluminium_panel"));
        BuiltInTags.steel_block.add(Block.getInstance("block_metal_panel"));

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
