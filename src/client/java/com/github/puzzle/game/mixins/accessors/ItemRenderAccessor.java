package com.github.puzzle.game.mixins.accessors;

import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

import java.lang.ref.WeakReference;
import java.util.WeakHashMap;

@Mixin(ItemRenderer.class)
public interface ItemRenderAccessor {

    @Accessor("referenceMap")
    public static WeakHashMap<Item, WeakReference<Item>> getRefMap(){
        throw new AssertionError();
    }

    @Accessor("referenceMap")
    public static void setRefMap(WeakHashMap<Item, WeakReference<Item>> referenceMap) {
        throw new AssertionError();
    }
}
