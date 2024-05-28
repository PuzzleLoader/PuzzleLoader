package net.appel.tweakers;

import dev.crmodders.puzzle.mod.ModContainer;
import dev.crmodders.puzzle.mod.ModLocator;
import dev.crmodders.puzzle.mod.Version;
import dev.crmodders.puzzle.providers.api.GameProviderScaffold;
import net.minecraft.launchwrapper.Launch;
import net.minecraft.launchwrapper.PuzzleClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.util.*;

import static dev.crmodders.puzzle.utils.MethodUtil.getDeclaredMethod;
import static dev.crmodders.puzzle.utils.MethodUtil.runStaticMethod;

public class CosmicReachClientTweaker implements GameProviderScaffold {

    public CosmicReachClientTweaker() {
        MixinBootstrap.start();
    }

    @Override
    public void initArgs(String[] args) {
        MixinBootstrap.doInit(CommandLineOptions.of(List.of(args)));
    }

    @Override
    public void inject(PuzzleClassLoader classLoader) {
        ModLocator.getMods(List.of(Launch.classLoader.getURLs()));

        Set<String> mixinConfigs = new HashSet<>();
        mixinConfigs.add("internal.mixins.json");

        for (ModContainer mod : ModLocator.LocatedMods.values()) {
            mixinConfigs.addAll(Arrays.stream(mod.extraInfo.mixins()).toList());
        }

        mixinConfigs.forEach(Mixins::addConfiguration);
        MixinBootstrap.inject();
    }

    @Override
    public String getId() {
        return "Cosmic Reach";
    }

    @Override
    public String getName() {
        return "";
    }

    @Override
    public Version getGameVersion() {
        return null;
    }

    @Override
    public String getRawVersion() {
        return "";
    }

    @Override
    public String getEntrypoint() {
//        return MixinBootstrap.getPlatform().getLaunchTarget();
        return "finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher";
    }

    @Override
    public List<String> getArgs() {
        runStaticMethod(getDeclaredMethod(MixinEnvironment.class, "gotoPhase", MixinEnvironment.Phase.class), MixinEnvironment.Phase.DEFAULT);
        return List.of();
    }

}
