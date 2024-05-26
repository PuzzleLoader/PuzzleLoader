package net.appel.tweakers;

import net.appel.main.Main;
import net.appel.mod.ModLocator;
import net.appel.mod.info.ModJsonInfo;
import net.appel.registry.BuiltInRegistries;
import net.minecraft.launchwrapper.ITweaker;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.LaunchClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.util.*;

public class CosmicReachClientTweaker implements ITweaker {

    public CosmicReachClientTweaker() {
        MixinBootstrap.start();
    }

    @Override
    public void acceptOptions(List<String> args, File gameDir, File assetsDir, String profile) {
        MixinBootstrap.doInit(CommandLineOptions.of(args));
    }

    @Override
    public void injectIntoClassLoader(LaunchClassLoader classLoader) {
        ModLocator.locateModsOnClasspath(List.of(Launch.classLoader.getURLs()));
        Set<String> mixinConfigs = new HashSet<>();
        mixinConfigs.add("internal.mixins.json");
        Arrays.stream(BuiltInRegistries.MOD_INFO_REGISTRY.getRegisteredNames()).forEach(id -> {
            ModJsonInfo info = BuiltInRegistries.MOD_INFO_REGISTRY.get(id);
            mixinConfigs.addAll(Arrays.stream(info.mixins()).toList());
        });

        mixinConfigs.forEach(Mixins::addConfiguration);
        MixinBootstrap.inject();

//        try {
//            Class.forName(getLaunchTarget(), false, getClass().getClassLoader()).getMethod("main", String[].class).invoke(null, (Object) new String[]{});
//        } catch (IllegalAccessException | InvocationTargetException | NoSuchMethodException | ClassNotFoundException e) {
//            throw new RuntimeException(e);
//        }
    }

    @Override
    public String getLaunchTarget() {
        return MixinBootstrap.getPlatform().getLaunchTarget();
    }

    @Override
    public String[] getLaunchArguments() {
        return new String[]{};
    }

}
