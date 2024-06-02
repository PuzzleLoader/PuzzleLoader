package dev.crmodders.puzzle.accessors;

import com.badlogic.gdx.utils.IntMap;
import com.badlogic.gdx.utils.LongMap;
import finalforeach.cosmicreach.util.Point3DMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;

@Mixin(Point3DMap.class)
public interface Point3DMapAccessor<T> {

    @Accessor
    LongMap<IntMap<T>> getMap();

}
