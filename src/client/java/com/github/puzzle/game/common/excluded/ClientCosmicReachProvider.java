package com.github.puzzle.game.common.excluded;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.core.loader.meta.*;
import com.github.puzzle.core.loader.provider.IGameProvider;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.CommonTransformerInitializer;
import com.github.puzzle.core.loader.util.MethodUtil;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.game.common.Puzzle;
import com.github.puzzle.game.common.ServerPuzzle;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.lwjgl3.Lwjgl3Launcher;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.spongepowered.asm.launch.MixinBootstrap;
import org.spongepowered.asm.launch.platform.CommandLineOptions;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.*;
import java.util.regex.Pattern;

public class ClientCosmicReachProvider implements IGameProvider {

    final static String MIXIN_START = "start";
    final static String MIXIN_DO_INIT = "doInit";
    final static String MIXIN_INJECT = "inject";
    final static String MIXIN_GOTO_PHASE = "gotoPhase";

    public ClientCosmicReachProvider() {
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
        return Version.parseVersion(getRawVersion());
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
        ModLocator.getMods(EnvType.CLIENT, List.of(classLoader.getURLs()));
        addBuiltinMods();

        CommonTransformerInitializer.invokeTransformers(classLoader);
    }

    @Override
    public void initArgs(String[] args) {
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_DO_INIT, CommandLineOptions.class), CommandLineOptions.of(List.of(args)));
    }

    @Override
    public void inject(PuzzleClassLoader classLoader) {
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
        List<Pair<EnvType, String>> mixinConfigs = new ArrayList<>();

        for (ModContainer mod : ModLocator.locatedMods.values()) {
            if (!mod.INFO.MixinConfigs.isEmpty()) mixinConfigs.addAll(mod.INFO.MixinConfigs);
        }

        mixinConfigs.forEach((e) -> {
            Mixins.addConfiguration(e.getRight());
        });
        MethodUtil.runStaticMethod(Reflection.getMethod(MixinBootstrap.class, MIXIN_INJECT));
    }

    @Override
    public void addBuiltinMods() {
        /* Puzzle Loader as a Mod */
        ModInfo.Builder puzzleLoaderInfo = ModInfo.Builder.New();
        {
            puzzleLoaderInfo.setName("Puzzle Loader");
            puzzleLoaderInfo.setId(Constants.MOD_ID);
            puzzleLoaderInfo.setDesc("A new dedicated modloader for Cosmic Reach");
            puzzleLoaderInfo.addEntrypoint("transformers", PuzzleTransformers.class.getName());
            puzzleLoaderInfo.addDependency("cosmic-reach", getRawVersion());
            puzzleLoaderInfo.addSidedMixinConfigs(
                    EnvType.CLIENT,
                    "mixins/client/accessors.client.mixins.json",
                    "mixins/client/internal.client.mixins.json",
                    "mixins/client/logging.client.mixins.json",

                    "mixins/common/logging.common.mixins.json",
                    "mixins/common/fixes.common.mixins.json",
                    "mixins/common/internal.common.mixins.json"
            );
            HashMap<String, JsonValue> meta = new HashMap<>();
            meta.put("icon", JsonObject.valueOf("puzzle-loader:icons/PuzzleLoaderIconx160.png"));
            puzzleLoaderInfo.setMeta(meta);
            puzzleLoaderInfo.setAuthors(new String[]{
                    "Mr-Zombii", "repletsin5", "SinfullySoul", "tympanicblock61"
            });

            puzzleLoaderInfo.setVersion(Constants.getPuzzleVersion());
            puzzleLoaderInfo.addAccessManipulator("puzzle_loader.manipulator");
            puzzleLoaderInfo.addEntrypoint("client_preInit", Puzzle.class.getName());
            puzzleLoaderInfo.addEntrypoint("client_init", Puzzle.class.getName());
            puzzleLoaderInfo.addEntrypoint("client_postInit", Puzzle.class.getName());
            puzzleLoaderInfo.addEntrypoint("init", ServerPuzzle.class.getName());

            ModLocator.addMod(puzzleLoaderInfo.build().getOrCreateModContainer());
        }

        /* Cosmic Reach as a mod */
        ModInfoV2Builder cosmicReachInfo = ModInfoV2Builder.New();
        {
            cosmicReachInfo.setName(getName());
            puzzleLoaderInfo.setId("cosmic-reach");
            cosmicReachInfo.setDesc("The base Game");
            cosmicReachInfo.addAuthor("FinalForEach");
            cosmicReachInfo.setVersion(getGameVersion());
            HashMap<String, JsonValue> meta = new HashMap<>();
            meta.put("icon", JsonObject.valueOf("icons/logox256.png"));
            cosmicReachInfo.setMeta(meta);
            ModLocator.addMod(cosmicReachInfo.build().getOrCreateModContainer());
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
