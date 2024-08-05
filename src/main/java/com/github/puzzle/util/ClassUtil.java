package com.github.puzzle.util;

import com.github.puzzle.game.util.Reflection;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

public class ClassUtil {
    public static <T> T newInstance(Class<T> type) {
        return Reflection.newInstance(type);
    }

}
