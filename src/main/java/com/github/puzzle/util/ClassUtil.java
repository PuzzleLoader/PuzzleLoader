package com.github.puzzle.util;

import com.github.puzzle.game.util.Reflection;

public class ClassUtil {
    public static <T> T newInstance(Class<T> type) {
        return Reflection.newInstance(type);
    }

}
