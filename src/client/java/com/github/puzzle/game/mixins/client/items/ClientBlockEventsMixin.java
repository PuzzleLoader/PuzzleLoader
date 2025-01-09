package com.github.puzzle.game.mixins.client.items;

import com.github.puzzle.game.items.IModItem;
import finalforeach.cosmicreach.ClientBlockEvents;
import finalforeach.cosmicreach.blockentities.BlockEntity;
import finalforeach.cosmicreach.blocks.BlockPosition;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.entities.player.Player;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.networking.client.ClientNetworkManager;
import finalforeach.cosmicreach.networking.packets.blocks.InteractBlockPacket;
import finalforeach.cosmicreach.ui.UI;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(ClientBlockEvents.class)
public class ClientBlockEventsMixin {

    @Inject(method = "breakBlock", at = @At("HEAD"), cancellable = true)
    public void breakBlock(Zone zone, BlockPosition blockPos, double timeSinceLastInteract, CallbackInfo ci) {
        ItemStack stack = UI.hotbar.getSelectedSlot().itemStack;
        if (stack != null && stack.getItem() instanceof IModItem modItem) {
            if (!modItem.canBreakBlockWith(blockPos.getBlockState())){
                ci.cancel();
            }
        }
    }

    @Inject(method = "interactWithBlock", at = @At("HEAD"), cancellable = true)
    public void interactWithBlock(Player player, Zone zone, BlockPosition blockPos, ItemStack heldItemStack, CallbackInfo ci) {
        if (heldItemStack != null && heldItemStack.getItem() instanceof IModItem modItem) {
            if (!modItem.canInteractWithBlock(blockPos.getBlockState())){
                ci.cancel();
            }
        }
    }

    @Inject(method = "interactWithBlockIfBlockEntity", at = @At("HEAD"), cancellable = true)
    public void interactWithBlockIfBlockEntity(Player player, Zone zone, BlockPosition blockPos, CallbackInfoReturnable<Boolean> cir) {
        ItemStack stack = UI.hotbar.getSelectedSlot().itemStack;
        if (stack != null && stack.getItem() instanceof IModItem modItem) {
            if (blockPos.getBlockEntity() != null){
                if (!modItem.canInteractWithBlockEntity(blockPos.getBlockEntity())){
                    cir.cancel();
                }
            }
        }
    }
}
