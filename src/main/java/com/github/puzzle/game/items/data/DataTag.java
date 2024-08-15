package com.github.puzzle.game.items.data;

import com.github.puzzle.game.oredict.tags.TagFormatException;
import com.github.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.io.CRBinDeserializer;
import finalforeach.cosmicreach.io.CRBinSerializer;
import finalforeach.cosmicreach.io.ICRBinSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class DataTag<T> implements ICRBinSerializable {

    public final String name;
    public final DataTagAttribute<T> attribute;

    public static Pattern pattern = Pattern.compile("[^a-zA-Z_]");

    static final Logger LOGGER = LoggerFactory.getLogger("DataTags");

    public DataTag(String name, DataTagAttribute<T> attribute) {
        if (pattern.matcher(name).matches())
            LOGGER.error("Tag \"{}\" is formatted incorrectly and can only contain letters and underscores.", name);

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

    @Override
    public void read(CRBinDeserializer crBinDeserializer) {
        Reflection.setFieldContents(this, "name", crBinDeserializer.readString("tag_name"));
        Reflection.setFieldContents(this, "attribute", crBinDeserializer.readObj("tag_attribute", DataTagAttribute.class));
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeString("tag_name", name);
        crBinSerializer.writeObj("tag_attribute", attribute);
    }

    @Override
    public String toString() {
        return "{"  + name + ": " + attribute.getFormattedString() + "}";
    }

    public interface DataTagAttribute<T> extends ICRBinSerializable {

        void setValue(T value);
        T getValue();
        DataTagAttribute<T> copyAndSetValue(T value);
        String getFormattedString();
    }

}
