package dev.crmodders.puzzle.core.commands;

import com.badlogic.gdx.files.FileHandle;
import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import dev.crmodders.puzzle.core.commands.lua.CosmicReachUtil;
import dev.crmodders.puzzle.core.resources.PuzzleGameAssetLoader;
import org.luaj.vm2.Globals;
import org.luaj.vm2.LuaValue;
import org.luaj.vm2.lib.jse.CoerceJavaToLua;
import org.luaj.vm2.lib.jse.JsePlatform;

import java.io.IOException;
import java.io.StringReader;

public class CommandManager {

    public static CommandDispatcher<PuzzleCommandSource> dispatcher = new CommandDispatcher<>();

    public static LiteralArgumentBuilder<PuzzleCommandSource> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <T> RequiredArgumentBuilder<PuzzleCommandSource, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    static {
        var function = literal("function");
        function
                .then(argument("path", StringArgumentType.greedyString())
                .executes(context -> {
                    String path = StringArgumentType.getString(context, "path");

                    Globals globals = JsePlatform.standardGlobals();
                    globals.set("CosmicReach", CoerceJavaToLua.coerce(new CosmicReachUtil()));
//                    globals.set("chat", CoerceJavaToLua.coerce(Chat.MAIN_CHAT));
//                    globals.set("world", CoerceJavaToLua.coerce(InGame.world));
//                    globals.set("player", CoerceJavaToLua.coerce(InGame.getLocalPlayer()));

                    FileHandle handle = PuzzleGameAssetLoader.locateAsset(path);
                    if (handle != null) {
                        try {
                            LuaValue chunk = globals.load(new StringReader(new String(handle.read().readAllBytes())), handle.name());
                            chunk.call();
                        } catch (IOException e) {
                            context.getSource().chat.sendMessage(
                                    context.getSource().getWorld(),
                                    context.getSource().getPlayer(),
                                    null,
                                    "This file may not exist or be corrupt"
                            );
                        } catch (Exception e) {
                            context.getSource().chat.sendMessage(
                                    context.getSource().getWorld(),
                                    context.getSource().getPlayer(),
                                    null,
                                    "The command had an error"
                            );
                            e.printStackTrace();
                        }
                    } else {
                        context.getSource().chat.sendMessage(
                                context.getSource().getWorld(),
                                context.getSource().getPlayer(),
                                null,
                                "This file may not exist or the path may not be correct"
                        );
                    }
                    return 0;
                }));
        dispatcher.register(function);
    }

}
