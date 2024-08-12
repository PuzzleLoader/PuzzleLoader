package com.github.puzzle.game.items.data;

import com.github.puzzle.game.oredict.tags.TagFormatException;

public class DataTag<T> {

    public final String name;
    public final DataTagAttribute<T> attribute;

    public DataTag(String name, DataTagAttribute<T> attribute) {
        if (!name.replaceAll("[a-z_]", "").isEmpty())
            throw new TagFormatException("Tag \""+name+"\" is formatted incorrectly and can only contain lowercase letters and underscores.");

        this.name = name;
        this.attribute = attribute;
    }

    public T getValue() {
        return attribute.getValue();
    }

    public <A extends T> DataTag<A> getTagAsType(Class<A> type) {
        if (attribute.getValue().getClass().equals(type)) {
            return (DataTag<A>) this;
        }
        return null;
    }

    public interface DataTagAttribute<T> {

        void setValue(T value);
        T getValue();
        DataTagAttribute<T> copyAndSetValue(T value);
    }

}
