package com.github.puzzle.game.mixins.common.items;

import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.stack.ITaggedStack;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ItemStack.class)
public class ItemStackMixin implements ITaggedStack {

    @Shadow private Item item;
    private boolean isModItem;
    private DataTagManifest manifest;

    @Inject(method = "read", at = @At("TAIL"))
    private void read(CRBinDeserializer crbd, CallbackInfo ci) {
        isModItem = crbd.readBoolean("isModItem", false);
        try {
            manifest = crbd.readObj("dataTagManifest", DataTagManifest.class);
        } catch (Exception ignore) {
            ignore.printStackTrace();
            manifest = new DataTagManifest();
        }
    }

    @Inject(method = "write", at = @At("TAIL"))
    private void write(CRBinSerializer crbs, CallbackInfo ci) {
        crbs.writeBoolean("isModItem", isModItem);
        crbs.writeObj("dataTagManifest", manifest == null ? new DataTagManifest() : manifest);
    }

    @Inject(method = "damage(I)V", at = @At("HEAD"), cancellable = true)
    private void canDamage(int damage, CallbackInfo ci){
        if(item instanceof IModItem modItem){
            DataTagManifest manifest = modItem.getTagManifest();
            boolean disableDamage = (boolean) manifest.getTag("disableItemDamage").attribute.getValue();
            if(disableDamage) ci.cancel();
        }
    }

    @Override
    public void puzzleLoader$setDataManifest(DataTagManifest tagManifest) {
        manifest = tagManifest;
    }

    @Override
    public DataTagManifest puzzleLoader$getDataManifest() {
        if (manifest == null) puzzleLoader$setDataManifest(new DataTagManifest());
        return manifest;
    }
}
