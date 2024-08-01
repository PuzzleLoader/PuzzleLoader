package dev.crmodders.puzzle.loader.lang.impl;

import dev.crmodders.puzzle.loader.lang.LanguageAdapter;
import dev.crmodders.puzzle.loader.lang.LanguageAdapterException;
import dev.crmodders.puzzle.loader.launch.Piece;
import dev.crmodders.puzzle.loader.mod.info.ModInfo;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JavaLanguageAdapter implements LanguageAdapter {

    @Override
    public <T> T create(ModInfo info, String value, Class<T> type) throws LanguageAdapterException {
        String[] split = value.split("::");

        if (split.length >= 3) throw new LanguageAdapterException("Invalid format for handel: " + value);

        Class<?> clazz;
        try {
            clazz = Class.forName(split[0], false, Piece.classLoader);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }

        if (split.length == 1) {
            if (type.isAssignableFrom(clazz)) {
                try {
                    return (T) clazz.getDeclaredConstructor().newInstance();
                } catch (Exception e) {
                    throw new LanguageAdapterException(e);
                }
            }
            throw new LanguageAdapterException("Class " + clazz.getSimpleName() + " is not able to be cast to " + type.getName() + "!");
        }

        String name = split[1];

        List<Method> methodList = new ArrayList<>();

        for (Method m : clazz.getDeclaredMethods()) {
            if (m.getName().equals(name))
                methodList.add(m);
        }

        try {
            Field field = clazz.getDeclaredField(name);
            Class<?> fieldType = field.getType();

            if ((fieldType.getModifiers() & Modifier.STATIC) == 0)
                throw new LanguageAdapterException("Field " + value + " must be static!");
            if (!methodList.isEmpty())
                throw new LanguageAdapterException("Ambiguous " + value + " - this refers to a field and method!");
            if (!type.isAssignableFrom(fieldType))
                throw new LanguageAdapterException("Field " + value + " is not of type " + type.getName() + "!");

            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new LanguageAdapterException("Field " + value + " cannot be accessed!", e);
        } catch (NoSuchFieldException ignore) {
        }

        if (!type.isInterface())
            throw new LanguageAdapterException("Cannot proxy method " + value + " to non-interface type " + type.getName() + "!");

        if (methodList.isEmpty()) throw new LanguageAdapterException("Cannot find " + value + "!");
        else if (methodList.size() >= 2)
            throw new LanguageAdapterException("Found more than one methods named " + value + "!");

        final Method target = methodList.get(0);
        Object o = null;

        if ((target.getModifiers() & Modifier.STATIC) == 0) {
            try {
                o = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new LanguageAdapterException(e);
            }
        }

        MethodHandle handle;

        try {
            handle = MethodHandles.lookup().unreflect(target);
        } catch (Exception e) {
            throw new LanguageAdapterException(e);
        }

        if (o != null) handle = handle.bindTo(o);

        try {
            return MethodHandleProxies.asInterfaceInstance(type, handle);
        } catch (Exception e) {
            throw new LanguageAdapterException(e);
        }
    }

}
