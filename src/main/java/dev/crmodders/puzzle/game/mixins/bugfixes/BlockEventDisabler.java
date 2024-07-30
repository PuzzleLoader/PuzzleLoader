package dev.crmodders.puzzle.game.mixins.bugfixes;

import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.JsonValue;
import dev.crmodders.puzzle.core.Identifier;
import dev.crmodders.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.GameAssetLoader;
import finalforeach.cosmicreach.blockevents.BlockEvents;
import finalforeach.cosmicreach.blockevents.actions.IBlockAction;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.util.HashMap;

@Mixin(BlockEvents.class)
public class BlockEventDisabler {

    @Shadow @Final public static HashMap<String, BlockEvents> INSTANCES;

    @Inject(method = "registerBlockEventAction", at = @At("HEAD"))
    private static void reg(Class<? extends IBlockAction> actionClass, CallbackInfo ci) {
        System.out.println(actionClass.getSimpleName());
    }

    @Inject(method = "initBlockEvents", at = @At(value = "INVOKE", target = "Lfinalforeach/cosmicreach/blockevents/BlockEvents;getInstance(Ljava/lang/String;)Lfinalforeach/cosmicreach/blockevents/BlockEvents;", shift = At.Shift.AFTER), cancellable = true)
    private static void initBlockEvents(CallbackInfo ci) {
        ci.cancel();
    }

}
