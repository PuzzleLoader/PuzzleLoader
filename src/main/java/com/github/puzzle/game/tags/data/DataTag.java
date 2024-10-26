package com.github.puzzle.game.tags.data;

import com.github.puzzle.core.loader.launch.Piece;
import com.github.puzzle.core.loader.util.Reflection;
import com.github.puzzle.game.tags.TagFormatException;
import finalforeach.cosmicreach.savelib.crbin.CRBinDeserializer;
import finalforeach.cosmicreach.savelib.crbin.CRBinSerializer;
import finalforeach.cosmicreach.savelib.crbin.ICRBinSerializable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.regex.Pattern;

public class DataTag<T> implements ICRBinSerializable {

    public String name;
    public DataTagAttribute<T> attribute;

    public static Pattern pattern = Pattern.compile("[^a-zA-Z_]");

    public DataTag() {}

    public DataTag(String name, DataTagAttribute<T> attribute) {
        if (pattern.matcher(name).matches())
            throw new TagFormatException("DataTag \""+name+"\" is formatted incorrectly and can only contain lowercase letters and underscores.");

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
        Class<? extends DataTagAttribute> attributeClass = null;
        try {
            attributeClass = (Class<? extends DataTagAttribute>) Piece.classLoader.findClass(crBinDeserializer.readString("tag_attribute_class"));
            Reflection.setFieldContents(this, "attribute", crBinDeserializer.readObj("tag_attribute", attributeClass));
        } catch (ClassNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void write(CRBinSerializer crBinSerializer) {
        crBinSerializer.writeString("tag_name", name);
        crBinSerializer.writeString("tag_attribute_class", attribute.getClass().getName());
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