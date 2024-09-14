package com.github.puzzle.core.loader.provider.lang.impl;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.meta.ModInfo;
import com.github.puzzle.core.loader.provider.ProviderException;
import com.github.puzzle.core.loader.provider.lang.ILangProvider;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandleProxies;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.List;

public class JavaLangProvider implements ILangProvider {

    @Override
    public <T> T create(ModInfo info, String value, Class<T> type) throws ProviderException {
        String[] split = value.split("::");

        if (split.length >= 3) throw new ProviderException("Invalid format for handle: " + value);

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
                    throw new ProviderException(e);
                }
            }
            throw new ProviderException("Class " + clazz.getSimpleName() + " is not able to be cast to " + type.getName() + "!");
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
                throw new ProviderException("Field " + value + " must be static!");
            if (!methodList.isEmpty())
                throw new ProviderException("Ambiguous " + value + " - this refers to a field and method!");
            if (!type.isAssignableFrom(fieldType))
                throw new ProviderException("Field " + value + " is not of type " + type.getName() + "!");

            return (T) field.get(null);
        } catch (IllegalAccessException e) {
            throw new ProviderException("Field " + value + " cannot be accessed!", e);
        } catch (NoSuchFieldException ignore) {
        }

        if (!type.isInterface())
            throw new ProviderException("Cannot proxy method " + value + " to non-interface type " + type.getName() + "!");

        if (methodList.isEmpty()) throw new ProviderException("Cannot find " + value + "!");
        else if (methodList.size() >= 2)
            throw new ProviderException("Found more than one methods named " + value + "!");

        final Method target = methodList.get(0);
        Object o = null;

        if ((target.getModifiers() & Modifier.STATIC) == 0) {
            try {
                o = clazz.getDeclaredConstructor().newInstance();
            } catch (Exception e) {
                throw new ProviderException(e);
            }
        }

        MethodHandle handle;

        try {
            handle = MethodHandles.lookup().unreflect(target);
        } catch (Exception e) {
            throw new ProviderException(e);
        }

        if (o != null) handle = handle.bindTo(o);

        try {
            return MethodHandleProxies.asInterfaceInstance(type, handle);
        } catch (Exception e) {
            throw new ProviderException(e);
        }
    }

}
