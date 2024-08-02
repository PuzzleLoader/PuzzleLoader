package dev.crmodders.puzzle.game.commands.lua;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.*;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import dev.crmodders.puzzle.game.commands.CommandManager;
//import dev.crmodders.puzzle.game.commands.LuaCommand;
import dev.crmodders.puzzle.game.commands.PuzzleCommandSource;
//import org.luaj.vm2.LuaClosure;
//import org.luaj.vm2.LuaValue;
//import org.luaj.vm2.lib.jse.CoerceJavaToLua;

public class LCommandUtil {

    public static CommandDispatcher<PuzzleCommandSource> getCommandDispatcher() {
        return CommandManager.dispatcher;
    }

    public static LiteralArgumentBuilder<PuzzleCommandSource> makeLiteral(String literal) {
        return CommandManager.literal(literal);
    }

//    public static LuaValue closureToCommand(LuaClosure closure) {
//        return CoerceJavaToLua.coerce(new LuaCommand(closure));
//    }

    // Lua Limitations
    public static ArgumentBuilder<PuzzleCommandSource, ?> _thenL(ArgumentBuilder<PuzzleCommandSource, ?> literal, ArgumentBuilder<PuzzleCommandSource, ?> argument) {
        return literal.then(argument);
    }

    public static ArgumentBuilder<PuzzleCommandSource, ?> _thenN(ArgumentBuilder<PuzzleCommandSource, ?> literal, CommandNode<PuzzleCommandSource> argument) {
//        return literal.then(argument);
        return literal.then(argument);
    }

    // Bool Argument
    public static RequiredArgumentBuilder<PuzzleCommandSource, Boolean> makeBoolArgument(String name) {
        return CommandManager.argument(name, BoolArgumentType.bool());
    }

    public static boolean getBoolArgument(CommandContext<PuzzleCommandSource> context, String name) {
        return BoolArgumentType.getBool(context, name);
    }

    // Double Argument
    public static RequiredArgumentBuilder<PuzzleCommandSource, Double> makeDoubleArgument(String name) {
        return CommandManager.argument(name, DoubleArgumentType.doubleArg());
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Double> makeDoubleArgumentM(String name, double min) {
        return CommandManager.argument(name, DoubleArgumentType.doubleArg(min));
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Double> makeDoubleArgumentMM(String name, double min, double max) {
        return CommandManager.argument(name, DoubleArgumentType.doubleArg(min, max));
    }

    public static double getDoubleArgument(CommandContext<PuzzleCommandSource> context, String name) {
        return DoubleArgumentType.getDouble(context, name);
    }

    // Float Argument
    public static RequiredArgumentBuilder<PuzzleCommandSource, Float> makeFloatArgument(String name) {
        return CommandManager.argument(name, FloatArgumentType.floatArg());
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Float> makeFloatArgumentM(String name, float min) {
        return CommandManager.argument(name, FloatArgumentType.floatArg(min));
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Float> makeFloatArgumentMM(String name, float min, float max) {
        return CommandManager.argument(name, FloatArgumentType.floatArg(min, max));
    }

    public static float getFloatArgument(CommandContext<PuzzleCommandSource> context, String name) {
        return FloatArgumentType.getFloat(context, name);
    }

    // Long Argument
    public static RequiredArgumentBuilder<PuzzleCommandSource, Long> makeLongArgument(String name) {
        return CommandManager.argument(name, LongArgumentType.longArg());
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Long> makeLongArgumentM(String name, long min) {
        return CommandManager.argument(name, LongArgumentType.longArg(min));
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Long> makeLongArgumentMM(String name, long min, long max) {
        return CommandManager.argument(name, LongArgumentType.longArg(min, max));
    }

    public static long getLongArgument(CommandContext<PuzzleCommandSource> context, String name) {
        return LongArgumentType.getLong(context, name);
    }

    // Integer Argument
    public static RequiredArgumentBuilder<PuzzleCommandSource, Integer> makeIntArgument(String name) {
        return CommandManager.argument(name, IntegerArgumentType.integer());
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Integer> makeIntArgumentM(String name, int min) {
        return CommandManager.argument(name, IntegerArgumentType.integer(min));
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, Integer> makeIntArgumentMM(String name, int min, int max) {
        return CommandManager.argument(name, IntegerArgumentType.integer(min, max));
    }

    public static int getIntArgument(CommandContext<PuzzleCommandSource> context, String name) {
        return IntegerArgumentType.getInteger(context, name);
    }

    // String Argument
    public static RequiredArgumentBuilder<PuzzleCommandSource, String> makeStringArgument(String name) {
        return CommandManager.argument(name, StringArgumentType.string());
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, String> makeWordArgument(String name) {
        return CommandManager.argument(name, StringArgumentType.word());
    }

    public static RequiredArgumentBuilder<PuzzleCommandSource, String> makeGreedyStringArgument(String name) {
        return CommandManager.argument(name, StringArgumentType.greedyString());
    }

    public static String getStringArgument(CommandContext<PuzzleCommandSource> context, String name) {
        return StringArgumentType.getString(context, name);
    }
}
