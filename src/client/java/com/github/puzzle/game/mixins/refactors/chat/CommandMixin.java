package com.github.puzzle.game.mixins.refactors.chat;

import com.github.puzzle.game.commands.CommandManager;
import com.github.puzzle.game.commands.PuzzleCommandSource;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.IChat;
import finalforeach.cosmicreach.chat.commands.Command;
import finalforeach.cosmicreach.util.exceptions.ChatCommandException;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
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

    @Shadow
    protected static void printHelp(IChat chat) {
    }

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
            List<LiteralArgumentBuilder<PuzzleCommandSource>> builders = new ArrayList<>();
            builders.add(LiteralArgumentBuilder.literal(commandName));
            for (String alias : aliases) {
                builders.add(LiteralArgumentBuilder.literal(alias));
            }
            com.mojang.brigadier.Command<PuzzleCommandSource> command2 = (commandContext -> {
                final Account account = commandContext.getSource().getAccount();
                final Chat chat = commandContext.getSource().getChat();

                String[] args = commandContext.getInput().substring(1).split(" ");
                String commandStr = args[0];
                if (!commandStr.equalsIgnoreCase("help") && !commandStr.equals("?")) {
                    if (commandSupplier != null) {
                        try {
                            Command command = commandSupplier.get();
                            command.setup(account, args);
                            command.run(chat);
                        } catch (ChatCommandException var8) {
                            ChatCommandException cce = var8;
                            chat.addMessage(null, "ERROR: " + cce.getMessage());
                        } catch (Exception var9) {
                            Exception ex = var9;
                            ex.printStackTrace();
                            chat.addMessage(null, "ERROR: An exception occured running the command: " + String.join(" ", args));
                        }

                    } else {
                        chat.addMessage(null, "Unknown command: " + commandStr);
                    }
                } else {
                    printHelp(chat);
                }
                return 0;
            });
            for (LiteralArgumentBuilder<PuzzleCommandSource> builder : builders) {
                builder.executes(command2);
                builder.then(CommandManager.argument("vanillaCommandArgument", StringArgumentType.greedyString()).executes(command2));
                CommandManager.dispatcher.register(builder);
            }
        }
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/chat/commands/Command;registerCommand(Ljava/util/function/Supplier;Ljava/lang/String;[Ljava/lang/String;)V"))
    private static void registerCommand2(Supplier<Command> commandSupplier, String commandName, String... aliases) {
        puzzleLoader$registerCommand0(commandSupplier, commandName, aliases);
    }

}
