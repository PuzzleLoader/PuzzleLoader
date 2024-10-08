package com.github.puzzle.game.common;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.PuzzleCommandSource;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.puzzle.BuilderWand;
import com.github.puzzle.game.util.DataTagUtil;
import com.github.puzzle.game.worldgen.schematics.Schematic;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.ui.UI;

import java.io.*;

public class Commands {

    public static void register() {

        LiteralArgumentBuilder<PuzzleCommandSource> genSchmatic = CommandManager.literal("gs");
        genSchmatic.executes(context -> {
            if(BuilderWand.pos1 == null || BuilderWand.pos2 == null) return 0;
            BuilderWand.clipBoard = Schematic.generateASchematic(BuilderWand.pos1, BuilderWand.pos2, InGame.getLocalPlayer().getZone(GameSingletons.world));
            return 0;
        });
        CommandManager.dispatcher.register(genSchmatic);

        LiteralArgumentBuilder<PuzzleCommandSource> saveSchmatic = CommandManager.literal("saveschematic");
        saveSchmatic.then(CommandManager.argument("filename", StringArgumentType.string())
                .executes(commandContext -> {
                    String filename = StringArgumentType.getString(commandContext, "filename");

                    File schematicFolder = new File(SaveLocation.getSaveFolderLocation() + "/schematic/");
                    if(!schematicFolder.exists()) schematicFolder.mkdirs();
                    if(!schematicFolder.isDirectory()) throw new RuntimeException("schematic folder is a file!");
                    File schematicFile = new File(SaveLocation.getSaveFolderLocation() + "/schematic/" + filename + ".schematic");
                    if(!schematicFile.exists()) {
                        try {
                            schematicFile.createNewFile();
                        } catch (IOException e) {
                            throw new RuntimeException("Can not create schematic: " + filename + ".schematic" );
                        }
                    }
                    if(!schematicFile.canWrite()) throw new RuntimeException("cant write! " + filename);


                    try {
                        DataOutputStream stream = new DataOutputStream(new FileOutputStream(schematicFile));
                        BuilderWand.clipBoard.write(stream);
                    } catch (FileNotFoundException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return 0;
                }));

        CommandManager.dispatcher.register(saveSchmatic);

        LiteralArgumentBuilder<PuzzleCommandSource> loadSchmatic = CommandManager.literal("loadschematic");
        loadSchmatic.then(CommandManager.argument("filename", StringArgumentType.string()).executes( commandContext -> {
            String filename = StringArgumentType.getString(commandContext, "filename");

            File schematicFile = new File(SaveLocation.getSaveFolderLocation() + "/schematic/" + filename + ".schematic");
            if(!schematicFile.exists()) {
                Chat.MAIN_CHAT.addMessage(GameSingletons.world, InGame.getLocalPlayer(), null,"Can not load schematic: " + filename);
                return 0;
            }

            try {
                BuilderWand.clipBoard = Schematic.loadSchematic(schematicFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return 0;
        }));
        CommandManager.dispatcher.register(loadSchmatic);

        LiteralArgumentBuilder<PuzzleCommandSource> getAttributes = CommandManager.literal("attributes");
        getAttributes.then(CommandManager.literal("stack")
                .then(CommandManager.literal("get").then(CommandManager.argument("attrib", StringArgumentType.string())
                                .executes(context -> {
                                    String attr = StringArgumentType.getString(context, "attrib");
                                    if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                        ItemSlot slot = UI.hotbar.getSelectedSlot();
                                        if (slot.itemStack == null) return 0;
                                        DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
                                        Chat.MAIN_CHAT.addMessage(GameSingletons.world, InGame.getLocalPlayer(), null, String.valueOf(manifest.getTag(attr)));
                                    }
                                    return 0;
                                }))
                        .executes(context -> {
                            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                ItemSlot slot = UI.hotbar.getSelectedSlot();
                                if (slot.itemStack == null) return 0;
                                DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
                                Chat.MAIN_CHAT.addMessage(GameSingletons.world, InGame.getLocalPlayer(), null, String.valueOf(manifest));
                            }
                            return 0;
                        }))
        );
        getAttributes.then(CommandManager.literal("item")
                .then(CommandManager.literal("get").then(CommandManager.argument("attrib", StringArgumentType.string())
                                .executes(context -> {
                                    String attr = StringArgumentType.getString(context, "attrib");
                                    if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                        ItemSlot slot = UI.hotbar.getSelectedSlot();
                                        if (slot.itemStack == null) return 0;
                                        if (slot.itemStack.getItem() instanceof IModItem item) {
                                            Chat.MAIN_CHAT.addMessage(GameSingletons.world, InGame.getLocalPlayer(), null, String.valueOf(item.getTagManifest().getTag(attr)));
                                            return 0;
                                        }
                                        Chat.MAIN_CHAT.addMessage(GameSingletons.world, InGame.getLocalPlayer(), null, "Not a ModItem");
                                    }
                                    return 0;
                                }))
                        .executes(context -> {
                            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                ItemSlot slot = UI.hotbar.getSelectedSlot();
                                if (slot.itemStack == null) return 0;
                                if (slot.itemStack.getItem() instanceof IModItem item) {
                                    Chat.MAIN_CHAT.addMessage(GameSingletons.world, InGame.getLocalPlayer(), null, String.valueOf(item.getTagManifest()));
                                    return 0;
                                }
                                Chat.MAIN_CHAT.addMessage(GameSingletons.world, InGame.getLocalPlayer(), null, "Not a ModItem");
                            }
                            return 0;
                        }))
        );
        CommandManager.dispatcher.register(getAttributes);
    }

}
