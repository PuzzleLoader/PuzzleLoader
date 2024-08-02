package dev.crmodders.puzzle.game.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;

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
                    if (path.endsWith(".lua")) runLua(context, path);
                    return 0;
                }));
        dispatcher.register(function);
    }
//
//    public static void runPython(CommandContext<PuzzleCommandSource> context, String path) {
//        FileHandle handle = PuzzleGameAssetLoader.locateAsset(path);
//        if (handle != null) {
//            try {
//                PythonInterpreter pyInterp = new PythonInterpreter();
//                PyCode code = pyInterp.compile(new StringReader(new String(handle.read().readAllBytes())), handle.name());
//                pyInterp.exec(code);
//            } catch (IOException e) {
//                context.getSource().chat.sendMessage(
//                        context.getSource().getWorld(),
//                        context.getSource().getPlayer(),
//                        null,
//                        "This file may not exist or be corrupt"
//                );
//            } catch (Exception e) {
//                context.getSource().chat.sendMessage(
//                        context.getSource().getWorld(),
//                        context.getSource().getPlayer(),
//                        null,
//                        "The command had an error"
//                );
//                e.printStackTrace();
//            }
//        } else {
//            context.getSource().chat.sendMessage(
//                    context.getSource().getWorld(),
//                    context.getSource().getPlayer(),
//                    null,
//                    "This file may not exist or the path may not be correct"
//            );
//        }
//    }

    public static void runLua(CommandContext<PuzzleCommandSource> context, String path) {
//        FileHandle handle = PuzzleGameAssetLoader.locateAsset(path);
//        if (handle != null) {
//            try {
//                LuaValue chunk = LuaGlobals.globals.load(new StringReader(new String(handle.read().readAllBytes())), handle.name());
//                chunk.call();
//            } catch (IOException e) {
//                context.getSource().chat.sendMessage(
//                        context.getSource().getWorld(),
//                        context.getSource().getPlayer(),
//                        null,
//                        "This file may not exist or be corrupt"
//                );
//            } catch (Exception e) {
//                context.getSource().chat.sendMessage(
//                        context.getSource().getWorld(),
//                        context.getSource().getPlayer(),
//                        null,
//                        "The command had an error"
//                );
//                e.printStackTrace();
//            }
//        } else {
//            context.getSource().chat.sendMessage(
//                    context.getSource().getWorld(),
//                    context.getSource().getPlayer(),
//                    null,
//                    "This file may not exist or the path may not be correct"
//            );
//        }
    }

}
