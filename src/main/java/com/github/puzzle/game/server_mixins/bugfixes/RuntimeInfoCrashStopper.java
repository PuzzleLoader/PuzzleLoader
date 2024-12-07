package com.github.puzzle.game.server_mixins.bugfixes;

import com.badlogic.gdx.utils.BufferUtils;
import com.github.puzzle.core.Constants;
import finalforeach.cosmicreach.RuntimeInfo;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Mutable;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.IntBuffer;
import java.text.DecimalFormat;

@Mixin(RuntimeInfo.class)
public class RuntimeInfoCrashStopper {

    @Mutable
    @Shadow @Final private static String osName;

    @Mutable
    @Shadow @Final public static boolean isMac;

    @Mutable
    @Shadow @Final public static boolean isWindows;

    @Mutable
    @Shadow @Final public static String version;

    @Shadow private static DecimalFormat decimalFormat;

    @Shadow private static StringBuilder sb;

    @Mutable
    @Shadow @Final private static IntBuffer glInt;


    @Inject(method = "<clinit>", at = @At("HEAD"), cancellable = true)
    private static void init(CallbackInfo ci) {
        osName = System.getProperty("os.name").toLowerCase();
        isMac = osName.contains("mac");
        isWindows = osName.contains("windows");
        version = Constants.getGameVersion();
        decimalFormat = new DecimalFormat("#.###");
        sb = new StringBuilder();
        glInt = BufferUtils.newIntBuffer(1);

        ci.cancel();
    }

}
