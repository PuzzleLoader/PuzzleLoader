package com.github.puzzle.game.mod;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.annotation.Internal;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.ModInitializer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PostModInitializer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.PreModInitializer;
import com.github.puzzle.core.terminal.PPLTerminalConsole;
import com.github.puzzle.game.account.OfflineAccountPZ;
import com.github.puzzle.game.commands.CommandManager;
import finalforeach.cosmicreach.accounts.AccountOffline;
import finalforeach.cosmicreach.networking.server.ServerSingletons;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;

@Internal
public class ServerPuzzle implements ModInitializer, PostModInitializer {

    @Env(EnvType.SERVER)
    public static final Logger SERVER_LOGGER = LoggerFactory.getLogger("Puzzle | Server");

    @Env(EnvType.SERVER)
    public static OfflineAccountPZ SERVER_ACCOUNT;

    @Env(EnvType.SERVER)
    public static Thread CONSOLE_THREAD;

    @Override
    public void onInit() {
        if (Constants.SIDE == EnvType.SERVER) {
            SERVER_ACCOUNT = new OfflineAccountPZ(
                    "Server", "SERVER_USER"
            );
        }
        CommandManager.registerCommands();
    }

    @Internal
    public static void initConsoleThread() {
        CONSOLE_THREAD = new Thread(()-> {
            try {
                System.in.available();
            } catch (IOException ignore) {
                return;
            }
            new PPLTerminalConsole(ServerSingletons.SERVER).start();
        });
        CONSOLE_THREAD.setName("Console Handler");
        CONSOLE_THREAD.setDaemon(true);
        CONSOLE_THREAD.start();
    }

    @Override
    public void onPostInit() {

    }
}
