package com.github.puzzle.game.server_mixins.refactors.logging;

import com.github.puzzle.core.loader.util.AnsiColours;
import com.github.puzzle.game.engine.blocks.IBlockLoader;
import finalforeach.cosmicreach.blocks.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Redirect;

import java.io.PrintStream;

@Mixin(BlockState.class)
public class BlockStateLogging {

    @Redirect(require = 0, method = "initialize(Lfinalforeach/cosmicreach/blocks/Block;)V", at = @At(value = "INVOKE", target = "Ljava/io/PrintStream;println(Ljava/lang/String;)V"))
    private void println(PrintStream instance, String x) {
        String nString = x.replaceAll("Applying generator ", "");
        String[] strs = nString.split("for blockstate:");
        for (int i = 0; i < strs.length; i++) strs[i] = strs[i].strip();

        IBlockLoader.LOGGER.info("Transforming block state {}\"{}\"{} with generator {}\"{}\"", AnsiColours.BLUE, strs[1], AnsiColours.RESET, AnsiColours.BLUE, strs[0]);
    }

}
