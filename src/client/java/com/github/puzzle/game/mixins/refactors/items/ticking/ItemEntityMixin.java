package com.github.puzzle.game.mixins.refactors.items.ticking;

import com.github.puzzle.game.items.ITickingItem;
import finalforeach.cosmicreach.entities.ItemEntity;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.world.Zone;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemEntity.class)
public class ItemEntityMixin {

    @Shadow private ItemStack itemStack;

    @Inject(method = "update", at = @At("HEAD"))
    private void update(Zone zone, double deltaTime, CallbackInfo ci) {
        if (itemStack.getItem() instanceof ITickingItem tickingItem) {
            tickingItem.tickEntity(zone, deltaTime, (ItemEntity) (Object) this, itemStack);
        }
    }

}
