package com.github.puzzle.game.server_mixins.refactors.chat;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.ServerCommandSource;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.chat.commands.Command;
import finalforeach.cosmicreach.util.exceptions.ChatCommandException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.ArrayList;
import java.util.List;
import java.util.function.Supplier;

@Mixin(Command.class)
public abstract class CommandMixin {

    @Inject(method = "registerCommand", at = @At("HEAD"), cancellable = true)
    private static void registerCommand(Supplier<Command> commandSupplier, String commandName, String[] aliases, CallbackInfo ci) {
        puzzleLoader$registerCommand0(commandSupplier, commandName, aliases);
        ci.cancel();
    }

    @Unique
    private static void puzzleLoader$registerCommand0(Supplier<Command> commandSupplier, String commandName, String[] aliases) {
        if (commandName == null) {
            throw new NullPointerException("Command cannot be null.");
        } else if (commandSupplier == null) {
            throw new NullPointerException("Command supplier cannot be null.");
        } else {
            List<LiteralArgumentBuilder<ServerCommandSource>> builders = new ArrayList<>();
            builders.add(LiteralArgumentBuilder.literal(commandName));
            for (String alias : aliases) {
                builders.add(LiteralArgumentBuilder.literal(alias));
            }
            com.mojang.brigadier.Command<ServerCommandSource> command2 = ctx -> {
                final Account account = ctx.getSource().getAccount();
                final IChat chat = ctx.getSource().getChat();

                String[] args = ctx.getInput().split(" ");
                String commandStr = args[0];
                if (commandSupplier != null) {
                    try {
                        Command command = commandSupplier.get();
                        command.setup(account, args);
                        command.run(chat);
                    } catch (ChatCommandException var8) {
                        chat.addMessage(null, "ERROR: " + var8.getMessage());
                    } catch (Exception var9) {
                        var9.printStackTrace();
                        chat.addMessage(null, "ERROR: An exception occured running the command: " + String.join(" ", args));
                    }
                } else {
                    chat.addMessage(null, "Unknown command: " + commandStr);
                }
                return 0;
            };
            for (LiteralArgumentBuilder<ServerCommandSource> builder : builders) {
                builder.executes(command2);
                builder.then(CommandManager.argument(ServerCommandSource.class, "vanilla args...", StringArgumentType.greedyString()).executes(command2));
                CommandManager.DISPATCHER.register(builder.requires(ServerCommandSource::hasOperator)).getUsageText();
            }
        }
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/chat/commands/Command;registerCommand(Ljava/util/function/Supplier;Ljava/lang/String;[Ljava/lang/String;)V"))
    private static void registerCommand2(Supplier<Command> commandSupplier, String commandName, String... aliases) {
        puzzleLoader$registerCommand0(commandSupplier, commandName, aliases);
    }

}
