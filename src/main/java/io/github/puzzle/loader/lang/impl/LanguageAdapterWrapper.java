package io.github.puzzle.loader.lang.impl;

import io.github.puzzle.loader.lang.LanguageAdapter;
import io.github.puzzle.loader.lang.LanguageAdapterException;
import io.github.puzzle.loader.mod.info.ModInfo;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

public class LanguageAdapterWrapper implements LanguageAdapter {

    Object clazz;

    public LanguageAdapterWrapper(Object clazz) {
        LOGGER.info("Wrapping Adapter Class \"" + clazz.getClass().getName() + "\"");
        this.clazz = clazz;
    }

    @Override
    public <T> T create(ModInfo info, String value, Class<T> type) throws LanguageAdapterException {
        Method create = null;
        try {
            create = clazz.getClass().getDeclaredMethod("create", ModInfo.class, String.class, Class.class);
        } catch (NoSuchMethodException e) {
            throw new LanguageAdapterException(e);
        }
        try {
            return (T) create.invoke(clazz, info, value, type);
        } catch (IllegalAccessException | InvocationTargetException e) {
            throw new LanguageAdapterException(e);
        }
    }
}
