package dev.crmodders.puzzle.core.providers.impl;

import dev.crmodders.flux.FluxPuzzle;
import dev.crmodders.puzzle.core.entrypoint.interfaces.PreMixinModInitializer;
import dev.crmodders.puzzle.core.mod.ModContainer;
import dev.crmodders.puzzle.core.mod.ModJsonInfo;
import dev.crmodders.puzzle.core.mod.Version;
import dev.crmodders.puzzle.core.providers.api.GameProvider;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import dev.crmodders.puzzle.core.mod.ModLocator;
import dev.crmodders.puzzle.core.launch.PuzzleClassLoader;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Pattern;

import static dev.crmodders.puzzle.utils.MethodUtil.*;

public class CosmicReachProvider implements GameProvider {

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

        ModLocator.AddBuiltinMods(this);

        // TODO: VERIFY MOD DEPENDENCIES
        ModLocator.verifyDependencies();

        File cosmicReach = searchForCosmicReach();
        if (cosmicReach != null) {
            try {
                classLoader.addURL(cosmicReach.toURI().toURL());
            } catch (MalformedURLException e) {
                throw new RuntimeException(e);
            }
        }

//        PreMixinModInitializer.invokeEntrypoint();

        // Load Mixins
        List<String> mixinConfigs = new ArrayList<>();
        mixinConfigs.add("internal.mixins.json");

        for (ModContainer mod : ModLocator.LocatedMods.values()) {
            mixinConfigs.addAll(Arrays.stream(mod.JSON_INFO.mixins()).toList());
        }

        mixinConfigs.forEach(Mixins::addConfiguration);
        runStaticMethod(getDeclaredMethod(MixinBootstrap.class, MIXIN_INJECT));
    }

    static File lookForJarVariations(String offs) {
        Pattern type1 = Pattern.compile("Cosmic Reach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type2 = Pattern.compile("Cosmic_Reach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type3 = Pattern.compile("CosmicReach-[\\d]+\\.[\\d]+.[\\d]+\\.jar", Pattern.CASE_INSENSITIVE);
        for (File f : Objects.requireNonNull(new File(offs).listFiles())) {
            if (type1.matcher(f.getName()).find()) return f;
            if (type2.matcher(f.getName()).find()) return f;
            if (type3.matcher(f.getName()).find()) return f;
            if (f.getName().equals("cosmic_reach.jar")) return f;
            if (f.getName().equals("cosmicreach.jar")) return f;
            if (f.getName().equals("cosmicReach.jar")) return f;
        }
        return null;
    }

    static File toCrJar(File f) {
        if (!f.exists()) return null;
        return f;
    }

    public static String DEFAULT_PACKAGE = "finalforeach.cosmicreach.lwjgl3";

    static File searchForCosmicReach() {
        if (ClassLoader.getPlatformClassLoader().getDefinedPackage(DEFAULT_PACKAGE) == null) {File jarFile;
            jarFile = lookForJarVariations(".");
            if (jarFile != null) return toCrJar(jarFile);
            jarFile = lookForJarVariations("../");
            if (jarFile != null) return toCrJar(jarFile);
        }
        return null;
    }


}
