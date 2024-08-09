package com.github.puzzle.game.items.data;

import java.util.HashMap;
import java.util.Map;

public class DataTagManifest {

    Map<String, DataTag<?>> tagMap = new HashMap<>();

    public DataTagManifest addTag(DataTag<?> tag) {
        tagMap.put(tag.name, tag);
        return this;
    }

    public boolean hasTag(DataTagPreset<?> tagPreset) {
        return tagMap.containsKey(tagPreset.name);
    }

    public boolean hasTag(String name) {
        return tagMap.containsKey(name);
    }

    public <T> DataTag<T> getTag(DataTagPreset<T> tagPreset) {
        return (DataTag<T>) tagMap.get(tagPreset.name);
    }

    public <T> DataTag<T> getTag(String name) {
        return (DataTag<T>) tagMap.get(name);
    }

}
