package com.github.puzzle.game.mixins.refactors.items;

import com.github.puzzle.game.engine.items.InstanceModelWrapper;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.data.DataTagManifest;
import com.github.puzzle.game.items.puzzle.ItemInstance;
import com.github.puzzle.game.items.stack.ITaggedStack;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.items.ItemStack;
import finalforeach.cosmicreach.items.ItemThing;
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
    public Item puzzleLoader$getItemInstance() {
        return new ItemInstance((ItemStack) (Object) this);
    }

    @Override
    public Item puzzleLoader$getItem() {
        return item;
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

    /**
     * @author Mr_Zombii
     * @reason add item meta data
     */
    @Overwrite
    public Item getItem() {
        // This shitty code should be redone
        if (item instanceof IModItem) {
            if (item instanceof ItemInstance)
                return puzzleLoader$getItem();
            this.item = puzzleLoader$getItemInstance();
            return getItem();
        } else {
            return puzzleLoader$getItem();
        }
    }
}
