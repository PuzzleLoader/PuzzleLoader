package com.github.puzzle.game.server_mixins.bugfixes;

import com.badlogic.gdx.files.FileHandle;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import finalforeach.cosmicreach.blocks.BlockStateGenerator;
import finalforeach.cosmicreach.util.Identifier;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(BlockStateGenerator.class)
public abstract class BlockStateGeneratorMixin {

    @Shadow private static HashMap<String, BlockStateGenerator> generators;

    @Shadow
    private static void loadGeneratorsFromFile(FileHandle asset) {
    }

    /**
     * @author Mr_Zombii
     * @reason add custom block generators
     */
    @Overwrite
    public static BlockStateGenerator getInstance(String genKey) {
        if (generators.containsKey(genKey))
            return generators.get(genKey);
        else {
            Identifier location = Identifier.of(genKey);
            location = Identifier.of(location.getNamespace(), "block_state_generators/" + location.getName() + ".json");
            loadGeneratorsFromFile(PuzzleGameAssetLoader.locateAsset(location));
        }
        return generators.get(genKey);
    }

}
