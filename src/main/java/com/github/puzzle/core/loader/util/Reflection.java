package com.github.puzzle.core.loader.util;

import com.github.puzzle.core.loader.launch.Piece;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class Reflection {

    public static String getCallerClassName() {
        StackTraceElement[] stElements = Thread.currentThread().getStackTrace();
        for (int i=1; i<stElements.length; i++) {
            StackTraceElement ste = stElements[i];
            if (!ste.getClassName().equals(Reflection.class.getName()) && ste.getClassName().indexOf("java.lang.Thread")!=0) {
                return ste.getClassName();
            }
        }
        return null;
    }

    public static Object newInstance(String name) {
        try {
            return newInstance(Class.forName(name, false, Piece.classLoader));
        } catch (ClassNotFoundException e) {
            return null;
        }
    }

    public static <T> T newInstance(Class<T> type) {
        try {
            Constructor<T> c = type.getConstructor();
            c.setAccessible(true);
            return c.newInstance();
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e) {
            try {
                Constructor<T> c = type.getDeclaredConstructor();
                c.setAccessible(true);
                return c.newInstance();
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException | NoSuchMethodException e2) {
                throw new RuntimeException(e2);
            }
        }
    }

    @SuppressWarnings("unchecked")
    public static @NotNull Field getField(@NotNull Class<?> clazz, String fieldName){
        try {
            Field f = clazz.getField(fieldName);
            f.setAccessible(true);
            return f;
        } catch (NoSuchFieldException ignore) {
            try {
                Field f = clazz.getDeclaredField(fieldName);
                f.setAccessible(true);
                return f;
            } catch (NoSuchFieldException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static void setFieldContents(Class<?> clazz, String fieldName, Object data) {
        Field f = getField(clazz, fieldName);
        try {
            f.set(null, data);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getFieldContents(Class<?> clazz, String fieldName) {
        try {
            return (T) getField(clazz, fieldName).get(null);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static Field getField(Object instance, String fieldName) {
        return getField(instance.getClass(), fieldName);
    }

    public static void setFieldContents(Object instance, String fieldName, Object data) {
        Field f = getField(instance, fieldName);
        try {
            f.set(instance, data);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static <T> T getFieldContents(Object instance, String fieldName) {
        try {
            return (T) getField(instance, fieldName).get(instance);
        } catch (IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    public static @NotNull Method getMethod(@NotNull Class<?> clazz, String name, Class<?>... args) {
        try {
            Method m = clazz.getMethod(name, args);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException ignore) {
            try {
                Method m = clazz.getDeclaredMethod(name, args);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public static @NotNull Method getMethod(@NotNull Class<?> clazz, String name) {
        try {
            Method m = clazz.getMethod(name);
            m.setAccessible(true);
            return m;
        } catch (NoSuchMethodException ignore) {
            try {
                Method m = clazz.getDeclaredMethod(name);
                m.setAccessible(true);
                return m;
            } catch (NoSuchMethodException e) {
                throw new RuntimeException(e);
            }
        }
    }

}
