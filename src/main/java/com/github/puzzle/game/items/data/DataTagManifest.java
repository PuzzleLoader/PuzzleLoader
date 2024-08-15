package com.github.puzzle.game.items.data;

import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;
import finalforeach.cosmicreach.io.ICRBinSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class DataTagManifest implements ICRBinSerializable {

    Map<String, DataTag<?>> tagMap = new HashMap<>();

    static final Logger LOGGER = LoggerFactory.getLogger("DataTags");

    public DataTagManifest addTag(DataTag<?> tag) {
        if (tag.name.isEmpty() || tag.name.isBlank() || DataTag.pattern.matcher(tag.name).matches()) {
            LOGGER.error("Tag \"{}\" may not be formatted correctly and has been ignored and not added to Manifest", tag.name);
            return this;
        }
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
        Object value = tagMap.get(tagPreset.name);
        return value != null ? (DataTag<T>) value : tagPreset.createTag(tagPreset.defaultAttribute.getValue());
    }

    public <T> DataTag<T> getTag(String name) {
        return (DataTag<T>) tagMap.get(name);
    }


    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        String[] keys = crBinDeserializer.readStringArray("data_manifest_keys");
        for (String key : keys) {
            tagMap.put(key, crBinDeserializer.readObj("data_tag_key_" + key, DataTag.class));
        }
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeStringArray("data_manifest_keys", tagMap.keySet().toArray(new String[0]));
        for (String key : tagMap.keySet()) {
            DataTag<?> value = tagMap.get(key);

            crBinSerializer.writeObj("data_tag_key_" + key, value);
        }
    }

    @Override
    public String toString() {
        StringBuilder builder = new StringBuilder();
        builder.append("{\n");

        String[] keys = tagMap.keySet().toArray(new String[0]);
        for (int i = 0; i < keys.length; i++) {
            String key = keys[i];
            DataTag<?> value = tagMap.get(key);

            builder.append("\t" + key + ": \"" + value.attribute.getFormattedString() + "\"");
            if (i < keys.length - 1) {
                builder.append(", \n ");
            }
        }
        builder.append("\n}");
        return builder.toString();
    }
}
