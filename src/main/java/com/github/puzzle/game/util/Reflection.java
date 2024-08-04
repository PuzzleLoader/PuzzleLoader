package com.github.puzzle.game.util;

import java.lang.reflect.Field;

public class Reflection {

    @SuppressWarnings("unchecked")
    public static <T> T getField(Class<?> clazz, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(null);
    }

    public static void setField(Class<?> clazz, String fieldName, Object data) throws NoSuchFieldException, IllegalAccessException {
        Field f = clazz.getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(null, data);
    }

    @SuppressWarnings("unchecked")
    public static <T> T getField(Object instance, String fieldName) throws NoSuchFieldException, IllegalAccessException {
        Field f = instance.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        return (T) f.get(instance);
    }

    public static void setField(Object instance, String fieldName, Object data) throws NoSuchFieldException, IllegalAccessException {
        Field f = instance.getClass().getDeclaredField(fieldName);
        f.setAccessible(true);
        f.set(instance, data);
    }

}
