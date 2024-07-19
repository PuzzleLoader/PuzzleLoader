package dev.crmodders.puzzle.util;

import java.lang.reflect.InvocationTargetException;

public class ClassUtil {
    public static <T> T newInstance(Class<T> type) {
        try {
            return type.getConstructor().newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    }

}
