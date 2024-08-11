package com.github.puzzle.game.mixins.bugfixes;

import com.github.puzzle.core.resources.ResourceLocation;
import finalforeach.cosmicreach.blocks.BlockStateGenerator;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.util.HashMap;

@Mixin(BlockStateGenerator.class)
public abstract class BlockStateGeneratorMixin {

    @Shadow private static HashMap<String, BlockStateGenerator> generators;

    @Shadow
    private static void loadGeneratorsFromFile(String fileName) {
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
            ResourceLocation location = ResourceLocation.fromString(genKey);
            location.name = "block_state_generators/" + location.name + ".json";
            System.out.println(location);
            loadGeneratorsFromFile(location.toString());
        }
        return generators.get(genKey);
    }

}
