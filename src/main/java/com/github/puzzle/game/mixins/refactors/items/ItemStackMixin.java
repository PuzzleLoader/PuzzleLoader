package com.github.puzzle.game.mixins.refactors.items;

import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.stack.ITaggedStack;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
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
