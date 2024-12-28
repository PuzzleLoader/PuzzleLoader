package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.utils.ObjectMap;
import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.mixins.client.accessors.ItemRenderAccessor;
import finalforeach.cosmicreach.items.Item;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;

import java.lang.ref.WeakReference;
import java.util.function.Function;

import static finalforeach.cosmicreach.rendering.items.ItemRenderer.registerItemModelCreator;

public class ClientItemRegistrar {

    public static <T extends IModItem> T registerModel(T item) {
        ItemRenderAccessor.getRefMap().put(item, new WeakReference<>(item));
        ObjectMap<Class<? extends Item>, Function<?, Item>> modelCreators = Reflection.getFieldContents(ItemRenderer.class, "modelCreators");

        if (!modelCreators.containsKey(item.getClass())) {
            registerItemModelCreator(item.getClass(), (modItem) -> new ExperimentalItemModel(modItem.get()).wrap());
        }
        return item;
    }

}
