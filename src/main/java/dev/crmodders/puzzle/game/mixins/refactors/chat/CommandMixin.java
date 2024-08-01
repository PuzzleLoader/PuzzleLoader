package dev.crmodders.puzzle.game.mixins.refactors.chat;

import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.builder.LiteralArgumentBuilder;
import dev.crmodders.puzzle.game.commands.CommandManager;
import dev.crmodders.puzzle.game.commands.PuzzleCommandSource;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.chat.Chat;
import finalforeach.cosmicreach.chat.commands.Command;
import finalforeach.cosmicreach.entities.Player;
import finalforeach.cosmicreach.util.exceptions.ChatCommandException;
import finalforeach.cosmicreach.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.util.function.Supplier;

@Mixin(Command.class)
public abstract class CommandMixin {

    @Shadow
    protected static void printHelp(World world, Player player) {
    }

    /**
     * @author Mr_Zombii
     * @reason Change Command Registerer
     */
    @Overwrite
    public static void registerCommand(String commandName, Supplier<Command> commandSupplier) {
        LiteralArgumentBuilder<PuzzleCommandSource> builder = LiteralArgumentBuilder.literal(commandName);
        com.mojang.brigadier.Command<PuzzleCommandSource> command2 = (commandContext -> {
            final Account account = commandContext.getSource().getAccount();
            final Chat chat = commandContext.getSource().getChat();
            final World world = commandContext.getSource().getWorld();
            final Player player = commandContext.getSource().getPlayer();

            String[] args = commandContext.getInput().substring(1).split(" ");
            String commandStr = args[0];
            if (!commandStr.equalsIgnoreCase("help") && !commandStr.equals("?")) {
                if (commandSupplier != null) {
                    try {
                        Command command = commandSupplier.get();
                        command.setup(account, world, player);
                        command.run(chat, args);
                    } catch (ChatCommandException var8) {
                        ChatCommandException cce = var8;
                        chat.sendMessage(world, player, null, "ERROR: " + cce.getMessage());
                    } catch (Exception var9) {
                        Exception ex = var9;
                        ex.printStackTrace();
                        chat.sendMessage(world, player, null, "ERROR: An exception occured running the command: " + String.join(" ", args));
                    }

                } else {
                    chat.sendMessage(world, player, null, "Unknown command: " + commandStr);
                }
            } else {
                printHelp(world, player);
            }
            return 0;
        });
        builder.executes(command2);
        builder.then(CommandManager.argument("mkprgasewdkrgasfewkllmkgsfdz", StringArgumentType.greedyString()).executes(command2));
        CommandManager.dispatcher.register(builder);
    }

    @Redirect(method = "<clinit>", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/chat/commands/Command;registerCommand(Ljava/lang/String;Ljava/util/function/Supplier;)V"))
    private static void registerCommand2(String commandName, Supplier<Command> commandSupplier) {
        registerCommand(commandName, commandSupplier);
    }

}
