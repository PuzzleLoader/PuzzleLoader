package com.github.puzzle.core;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.PuzzleCommandSource;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.util.DataTagUtil;
import com.github.puzzle.game.worldgen.schematics.Schematic;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.gamestates.InGame;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.ui.UI;

public class Commands {

    public static Vector3 vecpos1;
    public static Vector3 vecpos2;
    public static Schematic clipBoard;

    public static void register() {
        LiteralArgumentBuilder<PuzzleCommandSource> pos1 = CommandManager.literal("pos1");
        pos1.executes(context -> {
            Vector3 playerPositon = InGame.getLocalPlayer().getPosition();
            vecpos1 = new Vector3((float) Math.floor(playerPositon.x), (float) Math.floor(playerPositon.y), (float) Math.floor(playerPositon.z));
            return 0;
        });
        CommandManager.dispatcher.register(pos1);

        LiteralArgumentBuilder<PuzzleCommandSource> pos2 = CommandManager.literal("pos2");
        pos2.executes(context -> {
            Vector3 playerPositon = InGame.getLocalPlayer().getPosition();
            vecpos2 = new Vector3((float) Math.floor(playerPositon.x), (float) Math.floor(playerPositon.y), (float) Math.floor(playerPositon.z));
            return 0;
        });
        CommandManager.dispatcher.register(pos2);

        LiteralArgumentBuilder<PuzzleCommandSource> paste = CommandManager.literal("paste");
        paste.executes(context -> {
            if(clipBoard == null) {

                return 0;
            }
            Player player = InGame.getLocalPlayer();
            Vector3 playerPositon = player.getPosition();
            System.out.println(playerPositon);
            Schematic.genSchematicStructureAtGlobal(clipBoard, player.getZone(InGame.world), player.getChunk(InGame.world),(int) Math.floor(playerPositon.x), (int) Math.floor(playerPositon.y), (int) Math.floor(playerPositon.z));
            return 0;
        });
        CommandManager.dispatcher.register(paste);

        LiteralArgumentBuilder<PuzzleCommandSource> genSchmatic = CommandManager.literal("gs");
        genSchmatic.executes(context -> {
            clipBoard = Schematic.generateASchematic(vecpos1, vecpos2, InGame.getLocalPlayer().getZone(InGame.world));
            return 0;
        });
        CommandManager.dispatcher.register(genSchmatic);

        LiteralArgumentBuilder<PuzzleCommandSource> getAttributes = CommandManager.literal("attributes");
        getAttributes.then(CommandManager.literal("stack")
                .then(CommandManager.literal("get").then(CommandManager.argument("attrib", StringArgumentType.string())
                                .executes(context -> {
                                    String attr = StringArgumentType.getString(context, "attrib");
                                    if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                        ItemSlot slot = UI.hotbar.getSelectedSlot();
                                        if (slot.itemStack == null) return 0;
                                        DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
                                        Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, String.valueOf(manifest.getTag(attr)));
                                    }
                                    return 0;
                                }))
                        .executes(context -> {
                            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                ItemSlot slot = UI.hotbar.getSelectedSlot();
                                if (slot.itemStack == null) return 0;
                                DataTagManifest manifest = DataTagUtil.getManifestFromStack(slot.itemStack);
                                Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, String.valueOf(manifest));
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
                                            Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, String.valueOf(item.getTagManifest().getTag(attr)));
                                            return 0;
                                        }
                                        Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, "Not a ModItem");
                                    }
                                    return 0;
                                }))
                        .executes(context -> {
                            if (UI.hotbar != null && UI.hotbar.getSelectedSlot() != null) {
                                ItemSlot slot = UI.hotbar.getSelectedSlot();
                                if (slot.itemStack == null) return 0;
                                if (slot.itemStack.getItem() instanceof IModItem item) {
                                    Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, String.valueOf(item.getTagManifest()));
                                    return 0;
                                }
                                Chat.MAIN_CHAT.sendMessage(InGame.world, InGame.getLocalPlayer(), null, "Not a ModItem");
                            }
                            return 0;
                        }))
        );
    }

}
