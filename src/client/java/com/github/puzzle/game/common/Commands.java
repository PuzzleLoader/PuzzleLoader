package com.github.puzzle.game.common;

import com.github.puzzle.game.commands.ClientCommandManager;
import com.github.puzzle.game.commands.ClientCommandSource;
import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.puzzle.BuilderWand;
import com.github.puzzle.game.util.DataTagUtil;
import com.github.puzzle.game.worldgen.schematics.Schematic;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.io.SaveLocation;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.ui.UI;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

public class Commands {

    public static void register() {
        ClientCommandManager.DISPATCHER.register(CommandManager.createHelp(ClientCommandSource.class, "?", '?', ClientCommandManager.DISPATCHER));
        ClientCommandManager.DISPATCHER.register(CommandManager.createHelp(ClientCommandSource.class, "help", '?', ClientCommandManager.DISPATCHER));

        LiteralArgumentBuilder<ClientCommandSource> genSchmatic = CommandManager.literal(ClientCommandSource.class, "gs");
        genSchmatic.executes(context -> {
            if(BuilderWand.pos1 == null || BuilderWand.pos2 == null) return 0;
            BuilderWand.clipBoard = Schematic.generateASchematic(BuilderWand.pos1, BuilderWand.pos2, InGame.getLocalPlayer().getZone());
            return 0;
        });
        ClientCommandManager.DISPATCHER.register(genSchmatic);

        LiteralArgumentBuilder<ClientCommandSource> saveSchmatic = CommandManager.literal(ClientCommandSource.class, "saveschematic");
        saveSchmatic.then(CommandManager.argument(ClientCommandSource.class, "filename", StringArgumentType.string()).executes(commandContext -> {
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
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                    return 0;
                }));

        ClientCommandManager.DISPATCHER.register(saveSchmatic);

        LiteralArgumentBuilder<ClientCommandSource> loadSchmatic = CommandManager.literal(ClientCommandSource.class, "loadschematic");
        loadSchmatic.then(CommandManager.argument(ClientCommandSource.class, "filename", StringArgumentType.string()).executes( commandContext -> {
            String filename = StringArgumentType.getString(commandContext, "filename");

            File schematicFile = new File(SaveLocation.getSaveFolderLocation() + "/schematic/" + filename + ".schematic");
            if(!schematicFile.exists()) {
                Chat.MAIN_CLIENT_CHAT.addMessage(null,"Can not load schematic: " + filename);
                return 0;
            }

            try {
                BuilderWand.clipBoard = Schematic.loadSchematic(schematicFile.getPath());
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return 0;
        }));
        ClientCommandManager.DISPATCHER.register(loadSchmatic);

        LiteralArgumentBuilder<ClientCommandSource> getAttributes = CommandManager.literal(ClientCommandSource.class, "attributes");
        getAttributes.then(CommandManager.literal(ClientCommandSource.class, "stack")
                .then(CommandManager.literal(ClientCommandSource.class, "get").then(CommandManager.argument(ClientCommandSource.class, "attrib", StringArgumentType.string())
                                .executes(context -> {
                                    String attr = StringArgumentType.getString(context, "attrib");
                                    if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                        ItemSlot slot = UI.hotbar.getSelectedSlot();
                                        if (slot.itemStack == null) return 0;
                                        DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
                                        Chat.MAIN_CLIENT_CHAT.addMessage(null, String.valueOf(manifest.getTag(attr)));
                                    }
                                    return 0;
                                }))
                        .executes(context -> {
                            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                ItemSlot slot = UI.hotbar.getSelectedSlot();
                                if (slot.itemStack == null) return 0;
                                DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
                                Chat.MAIN_CLIENT_CHAT.addMessage(null, String.valueOf(manifest));
                            }
                            return 0;
                        }))
        );
        getAttributes.then(CommandManager.literal(ClientCommandSource.class, "item")
                .then(CommandManager.literal(ClientCommandSource.class, "get").then(CommandManager.argument(ClientCommandSource.class, "attrib", StringArgumentType.string())
                                .executes(context -> {
                                    String attr = StringArgumentType.getString(context, "attrib");
                                    if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                        ItemSlot slot = UI.hotbar.getSelectedSlot();
                                        if (slot.itemStack == null) return 0;
                                        if (slot.itemStack.getItem() instanceof IModItem item) {
                                            Chat.MAIN_CLIENT_CHAT.addMessage(null, String.valueOf(item.getTagManifest().getTag(attr)));
                                            return 0;
                                        }
                                        Chat.MAIN_CLIENT_CHAT.addMessage(null, "Not an IModItem");
                                    }
                                    return 0;
                                }))
                        .executes(context -> {
                            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                ItemSlot slot = UI.hotbar.getSelectedSlot();
                                if (slot.itemStack == null) return 0;
                                if (slot.itemStack.getItem() instanceof IModItem item) {
                                    Chat.MAIN_CLIENT_CHAT.addMessage(null, String.valueOf(item.getTagManifest()));
                                    return 0;
                                }
                                Chat.MAIN_CLIENT_CHAT.addMessage(null, "Not an IModItem");

//                                else if (slot.itemStack.getItem() instanceof ItemThing thing) {
//                                    StringBuilder builder = new StringBuilder();
//                                    builder.append(MapUtil.toString(thing.itemProperties));
//                                    builder.append(MapUtil.toString(new MapUtil(thing.itemIntProperties)));
//                                    builder.append(MapUtil.toString(new MapUtil(thing.itemFloatProperties)));
//                                    Chat.MAIN_CLIENT_CHAT.addMessage(null, builder.toString());
//                                }
                            }
                            return 0;
                        }))
        );
        ClientCommandManager.DISPATCHER.register(getAttributes);
    }

}
