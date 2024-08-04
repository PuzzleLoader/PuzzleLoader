package com.github.puzzle.game.mixins.refactors.logging;

import com.github.puzzle.util.AnsiColours;
import finalforeach.cosmicreach.accounts.Account;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.Redirect;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.LocalCapture;

import java.io.PrintStream;

@Mixin(Lwjgl3Launcher.class)
public class Lwjgl3LauncherMixin {
    @Unique
    private static final Logger LOGGER = LoggerFactory.getLogger("CosmicReach | Account");

    @Redirect(method = "lambda$main$0", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"), require = 0)
    private static void print1(PrintStream instance, String x) {
        LOGGER.info(x);
    }
    @Inject(method = "lambda$main$0",at = @At(value = "INVOKE_ASSIGN", target = "Lfinalforeach/cosmicreach/accounts/Account;getUniqueId()Ljava/lang/String;"),cancellable = true,locals = LocalCapture.CAPTURE_FAILHARD)
    private static void accountInfoPrint(CallbackInfo ci, String itchApiKey, Account account) {
        LOGGER.info(
                "Account: "+ AnsiColours.BLUE + "{}" + AnsiColours.WHITE +
                ", Username: " + AnsiColours.BLUE + "{}" + AnsiColours.WHITE +
                ", Display name: " +  AnsiColours.BLUE + "{}" + AnsiColours.WHITE,
                account.getUniqueId(),account.getUsername(),account.getDisplayName()
        );
        ci.cancel();
    }

}
