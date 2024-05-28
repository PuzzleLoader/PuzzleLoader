package dev.crmodders.puzzle.providers.impl;

import dev.crmodders.puzzle.mod.ModContainer;
import dev.crmodders.puzzle.mod.Version;
import dev.crmodders.puzzle.providers.api.GameProviderScaffold;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import dev.crmodders.puzzle.mod.ModLocator;
import dev.crmodders.puzzle.launch.PuzzleClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.IOException;
import java.util.*;

import static dev.crmodders.puzzle.utils.MethodUtil.*;

public class CosmicReachProvider implements GameProviderScaffold {

    String MIXIN_START = "start";
    String MIXIN_DO_INIT = "doInit";
    String MIXIN_INJECT = "inject";
    String MIXIN_GOTO_PHASE = "gotoPhase";

    public CosmicReachProvider() {
        runStaticMethod(getDeclaredMethod(MixinBootstrap.class, MIXIN_START));
    }

    @Override
    public String getId() {
        return "cosmic-reach";
    }

    @Override
    public String getName() {
        return "Cosmic Reach";
    }

    @Override
    public Version getGameVersion() {
        try {
            return Version.parseVersion(new String(GameAssetLoader.class.getResourceAsStream("/build_assets/version.txt").readAllBytes()));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getRawVersion() {
        try {
            return new String(GameAssetLoader.class.getResourceAsStream("/build_assets/version.txt").readAllBytes());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public String getEntrypoint() {
        return Lwjgl3Launcher.class.getName();
    }

    @Override
    public Collection<String> getArgs() {
        runStaticMethod(getDeclaredMethod(MixinEnvironment.class, MIXIN_GOTO_PHASE, MixinEnvironment.Phase.class), MixinEnvironment.Phase.DEFAULT);
        return List.of();
    }

    @Override
    public void initArgs(String[] args) {
        runStaticMethod(getDeclaredMethod(MixinBootstrap.class, MIXIN_DO_INIT, CommandLineOptions.class), CommandLineOptions.of(List.of(args)));
    }

    @Override
    public void inject(PuzzleClassLoader classLoader) {
        ModLocator.getMods(List.of(classLoader.getURLs()));

        List<String> mixinConfigs = new ArrayList<>();
        mixinConfigs.add("internal.mixins.json");

        for (ModContainer mod : ModLocator.LocatedMods.values()) {
            mixinConfigs.addAll(Arrays.stream(mod.extraInfo.mixins()).toList());
        }

        Mixins.addConfigurations(mixinConfigs.toArray(new String[0]));
        runStaticMethod(getDeclaredMethod(MixinBootstrap.class, MIXIN_INJECT));
    }


}
