package net.appel.mixin;

import net.appel.main.Main;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LogWrapper;
import org.spongepowered.asm.launch.GlobalProperties;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.service.mojang.MixinServiceLaunchWrapper;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.List;

public class LaunchWrapperMixinService extends MixinServiceLaunchWrapper {
    @Override
    public MixinEnvironment.CompatibilityLevel getMaxCompatibilityLevel() {
        return MixinEnvironment.CompatibilityLevel.JAVA_17;
    }

    private static int findInStackTrace(String className, String methodName) {
        Thread currentThread = Thread.currentThread();
        if (!"main".equals(currentThread.getName())) {
            return 0;
        } else {
            StackTraceElement[] stackTrace = currentThread.getStackTrace();
            StackTraceElement[] var4 = stackTrace;
            int var5 = stackTrace.length;

            for(int var6 = 0; var6 < var5; ++var6) {
                StackTraceElement s = var4[var6];
                if (className.equals(s.getClassName()) && methodName.equals(s.getMethodName())) {
                    return s.getLineNumber();
                }
            }

            return 0;
        }
    }

    public void init() {
        if (findInStackTrace("net.minecraft.launchwrapper.Launch", "launch") < 4) {
            LogWrapper.fine("MixinBootstrap.doInit() called during a tweak constructor!");
        }

        List<String> tweakClasses = GlobalProperties.get(BLACKBOARD_KEY_TWEAKCLASSES);
        if (tweakClasses != null) {
            tweakClasses.add("org.spongepowered.asm.mixin.EnvironmentStateTweaker");
        }

        super.init();


    }
}
