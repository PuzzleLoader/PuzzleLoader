package com.github.puzzle.game.commands;

import com.mojang.brigadier.CommandDispatcher;
import com.mojang.brigadier.arguments.ArgumentType;
import com.mojang.brigadier.arguments.IntegerArgumentType;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.ArgumentBuilder;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import com.mojang.brigadier.builder.RequiredArgumentBuilder;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.tree.CommandNode;
import com.mojang.brigadier.tree.LiteralCommandNode;
import finalforeach.cosmicreach.chat.commands.Command;

//Do not use puzzle specific things in this file, shared with paradox
public class CommandManager {
    public static CommandDispatcher<ConsoleCommandSource> CONSOLE_DISPATCHER = new CommandDispatcher<>();
    public static CommandDispatcher<ServerCommandSource> DISPATCHER = new CommandDispatcher<>();

    public static <S extends CommandSource> LiteralArgumentBuilder<S> literal(String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <S extends CommandSource, T> RequiredArgumentBuilder<S, T> argument(String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static <S extends CommandSource> LiteralArgumentBuilder<S> literal(Class<S> sourceType, String literal) {
        return LiteralArgumentBuilder.literal(literal);
    }

    public static <S extends CommandSource, T> RequiredArgumentBuilder<S, T> argument(Class<S> sourceType, String name, ArgumentType<T> type) {
        return RequiredArgumentBuilder.argument(name, type);
    }

    public static boolean hasInitialized;

    public static void initCommands() {
        if (hasInitialized) return;
        hasInitialized = true;
        Command.registerCommand(() -> new Command() {
            @Override
            public String getShortDescription() {
                return "";
            }
        }, "blank command");

        CommandManager.DISPATCHER.register(createHelp(ServerCommandSource.class, "?", '/', DISPATCHER));
        CommandManager.DISPATCHER.register(createHelp(ServerCommandSource.class, "help", '/', DISPATCHER));
    }

    @SuppressWarnings("unchecked")
    public static <T extends CommandSource> LiteralArgumentBuilder<T> createHelp(Class<T> sourceType, String name, char cmdChar, CommandDispatcher<T> dispatcher) {
        LiteralArgumentBuilder<T> help = LiteralArgumentBuilder.literal(name);
        help.executes((com.mojang.brigadier.Command<T>) help(-1, cmdChar, (CommandDispatcher<CommandSource>) dispatcher));
        help.then(argument(sourceType, "page", IntegerArgumentType.integer(0, Integer.MAX_VALUE)).executes(ctx -> {
            int page = IntegerArgumentType.getInteger(ctx, "page");
            return help(page, cmdChar, dispatcher).run(ctx);
        }));
        help.then(argument(sourceType, "command", StringArgumentType.greedyString()).executes(ctx -> {
            String cmd = StringArgumentType.getString(ctx, "command");
            return help(cmd, cmdChar, dispatcher).run(ctx);
        }));
        return help;
    }

    static <T extends CommandSource> com.mojang.brigadier.Command<T> help(String cmd, char cmdChar, CommandDispatcher<T> dispatcher) {
        return (ctx) -> {
            CommandNode<T> node = dispatcher.getRoot().getChild(cmd.strip());
            if (node == null || cmd.strip().equals("blank command")) {
                ctx.getSource().getChat().addMessage(null, "Command \"" + cmd + "\" does not exist.");
                return 1;
            }
            String[] usages = dispatcher.getAllUsage(node, ctx.getSource(), false);
            StringBuilder helpStr = new StringBuilder();

            for (String usage : usages) {
                helpStr.append(cmdChar);
                helpStr.append(node.getUsageText());
                helpStr.append(" ");
                helpStr.append(usage);
                helpStr.append('\n');
            }

            ctx.getSource().getChat().addMessage(null, helpStr.toString());
            return 0;
        };
    }

    static <T extends CommandSource> com.mojang.brigadier.Command<T> help(int page, char cmdChar, CommandDispatcher<T> dispatcher) {
        return (ctx) -> {
            String[] usages = dispatcher.getAllUsage(dispatcher.getRoot(), ctx.getSource(), false);
            StringBuilder helpStr = new StringBuilder();

            if (page < 0) {
                for (String usage : usages) {
                    if (!usage.strip().contains("blank command")) {
                        helpStr.append(cmdChar);
                        helpStr.append(" ");
                        helpStr.append(usage);
                        helpStr.append('\n');
                    }
                }
            } else {
                int startIndex = page * 25;
                int endIndex = startIndex + 25;

                for (int i = startIndex; i < endIndex + 1; i++) {
                    if (usages.length < i + 1) break;
                    String usage = usages[i];

                    if (!usage.strip().contains("blank command")) {
                        helpStr.append(cmdChar);
                        helpStr.append(" ");
                        helpStr.append(usage);
                        helpStr.append('\n');
                    }
                }
                helpStr.append("Page " + (page + 1) + " out of " + (int) (Math.ceil(usages.length / 25d)));
            }

            ctx.getSource().getChat().addMessage(null, helpStr.toString());
            return 0;
        };
    }

}
