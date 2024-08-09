package com.github.puzzle.game.items.data;

import java.lang.reflect.InvocationTargetException;

public class DataTagPreset<T> {

    String name;
    DataTag.DataTagAttribute<T> defaultAttribute;

    public DataTagPreset(String name, DataTag.DataTagAttribute<T> sacrifice) {
        this.name = name;
        this.defaultAttribute = sacrifice;
    }

    public DataTag<T> createTagFromDefault() {
        return new DataTag<>(
                name,
                defaultAttribute.copyAndSetValue(defaultAttribute.getValue())
        );
    }

    public DataTag<T> createTag(T value) {
        return new DataTag<>(
                name,
                defaultAttribute.copyAndSetValue(value)
        );
    }

}
