package com.github.puzzle.game.excluded;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.core.loader.meta.ModInfo;
import com.github.puzzle.core.loader.meta.Version;
import com.github.puzzle.core.loader.provider.IGameProvider;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.util.MixinUtil;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.game.ServerGlobals;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.server.ServerLauncher;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class ServerCRProvider implements IGameProvider {

    public ServerCRProvider() {
        MixinUtil.start();
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
        return ServerGlobals.isRunningOnParadox ? ModLocator.PARADOX_SERVER_ENTRYPOINT : ServerLauncher.class.getName();
    }

    @Override
    public Collection<String> getArgs() {
        MixinUtil.goToPhase(MixinEnvironment.Phase.DEFAULT);
        return List.of();
    }

    @Override
    public void registerTransformers(PuzzleClassLoader classLoader) {

    }

    @Override
    public void initArgs(String[] args) {
        MixinUtil.doInit(args);
    }

    @Override
    public void inject(PuzzleClassLoader classLoader) {
        ModLocator.verifyDependencies();

        File cosmicReach = ModLocator.searchForCosmicReach();
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
        MixinUtil.inject();
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
                    "puzzle.client.mixins.json",
                    "puzzle.common.mixins.json"
            );
            puzzleLoaderInfo.setAuthors(new String[]{
                    "Mr-Zombii", "repletsin5", "SinfullySoul", "tympanicblock61"
            });

            puzzleLoaderInfo.setVersion(Constants.getVersion());
            puzzleLoaderInfo.setAccessManipulator("puzzle_loader.manipulator");

            ModLocator.locatedMods.put("puzzle-loader", puzzleLoaderInfo.build().getOrCreateModContainer());
        }

        /* Cosmic Reach as a mod */
        ModInfo.Builder cosmicReachInfo = ModInfo.Builder.New();
        {
            cosmicReachInfo.setName(getName());
            cosmicReachInfo.setDesc("The base game");
            cosmicReachInfo.addAuthor("FinalForEach");
            cosmicReachInfo.setVersion(getGameVersion());
            ModLocator.locatedMods.put(getId(), cosmicReachInfo.build().getOrCreateModContainer());
        }
    }
}
