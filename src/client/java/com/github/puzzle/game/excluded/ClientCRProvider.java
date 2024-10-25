package com.github.puzzle.game.excluded;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.annotation.Internal;
import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.core.loader.meta.ModInfo;
import com.github.puzzle.core.loader.meta.Version;
import com.github.puzzle.core.loader.provider.IGameProvider;
import com.github.puzzle.core.loader.provider.mod.ModContainer;
import com.github.puzzle.core.loader.provider.mod.entrypoint.impls.CommonTransformerInitializer;
import com.github.puzzle.core.loader.util.MixinUtil;
import com.github.puzzle.core.loader.util.ModLocator;
import com.github.puzzle.game.mod.ClientPuzzle;
import finalforeach.cosmicreach.GameAssetLoader;
import org.hjson.JsonObject;
import org.hjson.JsonValue;
import org.spongepowered.asm.mixin.MixinEnvironment;
import org.spongepowered.asm.mixin.Mixins;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Internal
@Env(EnvType.CLIENT)
public class ClientCRProvider implements IGameProvider {

    public ClientCRProvider() {
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
        return ModLocator.COSMIC_REACH_CLIENT_ENTRYPOINT;
    }

    @Override
    public Collection<String> getArgs() {
        MixinUtil.goToPhase(MixinEnvironment.Phase.DEFAULT);
        return List.of();
    }

    @Override
    public void registerTransformers(PuzzleClassLoader classLoader) {
        ModLocator.getMods(List.of(classLoader.getURLs()));
        addBuiltinMods();

        CommonTransformerInitializer.invokeTransformers(classLoader);
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
            HashMap<String, JsonValue> meta = new HashMap<>();
            meta.put("icon", JsonObject.valueOf("puzzle-loader:icons/PuzzleLoaderIconx160.png"));
            puzzleLoaderInfo.setMeta(meta);
            puzzleLoaderInfo.setAuthors(new String[]{
                    "Mr-Zombii", "repletsin5", "SinfullySoul", "tympanicblock61"
            });

            puzzleLoaderInfo.setVersion(Constants.getVersion());
            puzzleLoaderInfo.setAccessManipulator("puzzle_loader.manipulator");
//            puzzleLoaderInfo.addEntrypoint("client_preInit", Puzzle.class.getName());
            puzzleLoaderInfo.addEntrypoint("client_init", ClientPuzzle.class.getName());
            puzzleLoaderInfo.addEntrypoint("client_postInit", ClientPuzzle.class.getName());

            ModLocator.locatedMods.put("puzzle-loader", puzzleLoaderInfo.build().getOrCreateModContainer());
        }

        /* Cosmic Reach as a mod */
        ModInfo.Builder cosmicReachInfo = ModInfo.Builder.New();
        {
            cosmicReachInfo.setName(getName());
            cosmicReachInfo.setDesc("The base game");
            cosmicReachInfo.addAuthor("FinalForEach");
            cosmicReachInfo.setVersion(getGameVersion());
            HashMap<String, JsonValue> meta = new HashMap<>();
            meta.put("icon", JsonObject.valueOf("icons/logox256.png"));
            cosmicReachInfo.setMeta(meta);
            ModLocator.locatedMods.put(getId(), cosmicReachInfo.build().getOrCreateModContainer());
        }
    }
}
