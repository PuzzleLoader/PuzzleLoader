package com.github.puzzle.game.mixins.client.items;

import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.EnvType;
import com.github.puzzle.game.ClientGlobals;
import com.github.puzzle.game.items.IModItem;
import com.llamalad7.mixinextras.injector.ModifyExpressionValue;
import finalforeach.cosmicreach.ClientBlockEvents;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.blockevents.BlockEventArgs;
import finalforeach.cosmicreach.blockevents.BlockEventTrigger;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemSlot;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.networking.GamePacket;
import finalforeach.cosmicreach.networking.NetworkIdentity;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import finalforeach.cosmicreach.networking.packets.blocks.BreakBlockPacket;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import finalforeach.cosmicreach.settings.ControlSettings;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.world.Zone;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBlockEvents.class)
public class ClientBlockEventsMixin {

    private static final Logger logger = LoggerFactory.getLogger("Puzzle | Server");

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    public void breakBlock(Zone zone, BlockPosition blockPos, double timeSinceLastInteract, CallbackInfo ci) {
        ItemStack stack = UI.hotbar.getSelectedSlot().itemStack;
        if (stack != null && stack.getItem() instanceof IModItem modItem) {
            if (!modItem.canBreakBlockWith(blockPos.getBlockState())){
                ci.cancel();
            }
        }
    }
}
