package com.github.puzzle.game.provider;

import com.github.puzzle.core.Puzzle;
import com.github.puzzle.game.util.Reflection;
import com.github.puzzle.loader.entrypoint.interfaces.TransformerInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import com.github.puzzle.loader.launch.internal.mods.PuzzleTransformers;
import com.github.puzzle.loader.mod.ModContainer;
import com.github.puzzle.loader.mod.ModLocator;
import com.github.puzzle.loader.mod.Version;
import com.github.puzzle.loader.mod.info.ModInfo;
import com.github.puzzle.loader.providers.api.IGameProvider;
import com.github.puzzle.util.MethodUtil;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

public class CosmicReachProvider implements IGameProvider {
    String MIXIN_START = "start";
    String MIXIN_DO_INIT = "doInit";
    String MIXIN_INJECT = "inject";
    String MIXIN_GOTO_PHASE = "gotoPhase";

    public CosmicReachProvider() {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_START));
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
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinEnvironment.class, MIXIN_GOTO_PHASE, MixinEnvironment.Phase.class), MixinEnvironment.Phase.DEFAULT);
        return List.of();
    }

    @Override
    public void registerTransformers(@NotNull PuzzleClassLoader classLoader) {
        ModLocator.getMods(List.of(classLoader.getURLs()));
        addBuiltinMods();

        TransformerInitializer.invokeTransformers(classLoader);
    }

    @Override
    public void initArgs(String[] args) {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_DO_INIT, CommandLineOptions.class), CommandLineOptions.of(List.of(args)));
    }

    @Override
    public void inject(PuzzleClassLoader classLoader) {
//        ModLocator.getMods(List.of(classLoader.getURLs()));

//        ModLocator.AddBuiltinMods(this);

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

        // Load Mixins
        List<String> mixinConfigs = new ArrayList<>();

        for (ModContainer mod : ModLocator.locatedMods.values()) {
            mixinConfigs.addAll(mod.INFO.MixinConfigs);
        }

        mixinConfigs.forEach(Mixins::addConfiguration);
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_INJECT));
    }

    @Override
    public void addBuiltinMods() {
        /* Puzzle Loader as a Mod */
        ModInfo.Builder puzzleLoaderInfo = ModInfo.Builder.New();
        {
            puzzleLoaderInfo.setName("Puzzle Loader");
            puzzleLoaderInfo.setDesc("A new dedicated modloader for Cosmic Reach");
            puzzleLoaderInfo.addEntrypoint("transformers", PuzzleTransformers.class.getName());
            puzzleLoaderInfo.addDependency("cosmic-reach", getGameVersion());
            puzzleLoaderInfo.addMixinConfigs(
                    "internal.mixins.json",
                    "accessors.mixins.json",
                    "bugfixes.mixins.json",
                    "logging.mixins.json"
            );
            puzzleLoaderInfo.setVersion(Puzzle.VERSION);
            puzzleLoaderInfo.setAccessManipulator("puzzle_loader.manipulator");
            puzzleLoaderInfo.addEntrypoint("preInit", Puzzle.class.getName());
            puzzleLoaderInfo.addEntrypoint("init", Puzzle.class.getName());

            ModLocator.locatedMods.put("puzzle-loader", puzzleLoaderInfo.build().getOrCreateModContainer());
        }

        /* Cosmic Reach as a mod */
        ModInfo.Builder cosmicReachInfo = ModInfo.Builder.New();
        {
            cosmicReachInfo.setName(getName());
            cosmicReachInfo.setDesc("The base Game");
            cosmicReachInfo.addAuthor("FinalForEach");
            cosmicReachInfo.setVersion(getGameVersion());
            ModLocator.locatedMods.put(getId(), cosmicReachInfo.build().getOrCreateModContainer());
        }

    }

    static @Nullable File lookForJarVariations(String offs) {
        Pattern type1 = Pattern.compile("Cosmic Reach-\\d+\\.\\d+.\\d+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type2 = Pattern.compile("Cosmic_Reach-\\d+\\.\\d+.\\d+\\.jar", Pattern.CASE_INSENSITIVE);
        Pattern type3 = Pattern.compile("CosmicReach-\\d+\\.\\d+.\\d+\\.jar", Pattern.CASE_INSENSITIVE);
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

    static @Nullable File toCrJar(@NotNull File f) {
        if (!f.exists()) return null;
        return f;
    }

    public static String DEFAULT_PACKAGE = "finalforeach.cosmicreach.lwjgl3";

    static @Nullable File searchForCosmicReach() {
        if (ClassLoader.getPlatformClassLoader().getDefinedPackage(DEFAULT_PACKAGE) == null) {File jarFile;
            jarFile = lookForJarVariations(".");
            if (jarFile != null) return toCrJar(jarFile);
            jarFile = lookForJarVariations("../");
            if (jarFile != null) return toCrJar(jarFile);
        }
        return null;
    }
}
