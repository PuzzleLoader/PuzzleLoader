package com.github.puzzle.game.mixins.bugfixes;

import com.github.puzzle.game.util.Point3DKey;
import finalforeach.cosmicreach.util.IPoint3DMap;
import finalforeach.cosmicreach.util.Point3DConsumer;
import finalforeach.cosmicreach.util.Point3DMap;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Consumer;

@Mixin(Point3DMap.class)
public class Point3DMapRewrite<T> implements IPoint3DMap<T> {

    @Unique
    final Map<Point3DKey, T> puzzleLoader$objects = new HashMap<>();

    @Override
    public T get(int x, int y, int z) {
        return puzzleLoader$objects.get(new Point3DKey(x, y, z));
    }

    @Override
    public void put(T o, int x, int y, int z) {
        synchronized (puzzleLoader$objects) {
            puzzleLoader$objects.put(new Point3DKey(x, y, z), o);
        }
    }

    @Override
    public T remove(int x, int y, int z) {
        synchronized (puzzleLoader$objects) {
            return puzzleLoader$objects.remove(new Point3DKey(x, y, z));
        }
    }

    @Override
    public void forEach(Consumer<T> consumer) {
        synchronized (puzzleLoader$objects) {
            puzzleLoader$objects.values().forEach(consumer);
        }
    }

    @Override
    public int size() {
        return puzzleLoader$objects.size();
    }

    @Override
    public void clear() {
        synchronized (puzzleLoader$objects) {
            puzzleLoader$objects.clear();
            System.gc();
        }
    }

    @Override
    public void forEach(Point3DConsumer point3DConsumer) {
        synchronized (puzzleLoader$objects) {
            puzzleLoader$objects.keySet().forEach((key) -> {
                T t = puzzleLoader$objects.get(key);
                synchronized (puzzleLoader$objects) {
                    point3DConsumer.consume(t, key.x(), key.y(), key.z());
                }
            });
        }
    }
}
