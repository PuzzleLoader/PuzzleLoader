package com.github.puzzle.core.loader.util;

import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.util.List;

public class MixinUtil {

    final static String MIXIN_START = "start";
    final static String MIXIN_DO_INIT = "doInit";
    final static String MIXIN_INJECT = "inject";
    final static String MIXIN_GOTO_PHASE = "gotoPhase";

    public static void start() {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_START));
    }

    public static void doInit(List<String> options) {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_DO_INIT, CommandLineOptions.class), CommandLineOptions.of(options));
    }

    public static void doInit(String[] options) {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_DO_INIT, CommandLineOptions.class), CommandLineOptions.of(List.of(options)));
    }

    public static void doInit(CommandLineOptions options) {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_DO_INIT, CommandLineOptions.class), options);
    }

    public static void inject() {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_INJECT));
    }

    public static void goToPhase(MixinEnvironment.Phase phase) {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_GOTO_PHASE, MixinEnvironment.Phase.class), phase);
    }

}
