package com.github.puzzle.util;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassUtil {
    public static <T> T newInstance(Class<T> type) {
        try {
            Constructor c = type.getConstructor();
            c.setAccessible(true);
            return (T) c.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            try {
                Constructor c = type.getDeclaredConstructor();
                c.setAccessible(true);
                return (T) c.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

}
