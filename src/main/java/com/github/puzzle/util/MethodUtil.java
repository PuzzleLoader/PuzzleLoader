package com.github.puzzle.util;

import org.jetbrains.annotations.NotNull;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class MethodUtil {
    public static @NotNull Method getMethod(@NotNull Class<?> clazz, String name, Class<?>... args) {
        try {
            Method m = clazz.getMethod(name, args);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    };

    public static @NotNull Method getMethod(@NotNull Class<?> clazz, String name) {
        try {
            Method m = clazz.getMethod(name);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    };

    public static @NotNull Method getDeclaredMethod(@NotNull Class<?> clazz, String name, Class<?>... args) {
        try {
            Method m = clazz.getDeclaredMethod(name, args);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    };

    public static @NotNull Method getDeclaredMethod(@NotNull Class<?> clazz, String name) {
        try {
            Method m = clazz.getDeclaredMethod(name);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException e) {
            throw new RuntimeException(e);
        }
    };


    public static Object runStaticMethod(@NotNull Method method) {
        try {
            return method.invoke(null);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object runStaticMethod(@NotNull Method method, Object... args) {
        try {
            return method.invoke(null, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object runMethod(Object obj, @NotNull Method method) {
        try {
            return method.invoke(method);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }

    public static Object runMethod(Object obj, @NotNull Method method, Object... args) {
        try {
            return method.invoke(method, args);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new RuntimeException(e);
        }
    }
}
