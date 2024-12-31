package com.github.puzzle.game.mixins.common.fixes;

import com.badlogic.gdx.utils.*;
import com.badlogic.gdx.utils.reflect.ArrayReflection;
import com.badlogic.gdx.utils.reflect.ClassReflection;
import org.spongepowered.asm.mixin.Final;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

@Mixin(Json.class)
public abstract class LibgdxJSONFixer {

    @Shadow private JsonWriter writer;

    @Shadow public abstract void writeObjectStart(String name);

    @Shadow public abstract void writeObjectStart(Class actualType, Class knownType);

    @Shadow public abstract void writeObjectStart(String name, Class actualType, Class knownType);

    @Shadow public abstract void writeObjectEnd();

    @Shadow @Final private ObjectMap<Class, Json.Serializer> classToSerializer;

    @Shadow public abstract void writeArrayStart();

    @Shadow public abstract void writeArrayStart(String name);

    @Shadow public abstract void writeArrayEnd();

    @Shadow private String typeName;

    @Shadow protected abstract String convertToString(Enum e);

    @Shadow protected abstract String convertToString(Object object);

    @Shadow public abstract void writeFields(Object object);

    @Shadow public abstract void writeValue(Object value, Class knownType);

    /**
     * @author
     * @reason
     */
    @Overwrite
    public void writeValue (@Null Object value, @Null Class knownType, @Null Class elementType) {
        try {
            if (value == null) {
                writer.value(null);
                return;
            }

            if ((knownType != null && knownType.isPrimitive()) || knownType == String.class || knownType == Integer.class
                    || knownType == Boolean.class || knownType == Float.class || knownType == Long.class || knownType == Double.class
                    || knownType == Short.class || knownType == Byte.class || knownType == Character.class) {
                writer.value(value);
                return;
            }

            Class actualType = value.getClass();

            if (actualType.isPrimitive() || actualType == String.class || actualType == Integer.class || actualType == Boolean.class
                    || actualType == Float.class || actualType == Long.class || actualType == Double.class || actualType == Short.class
                    || actualType == Byte.class || actualType == Character.class) {
                writer.value(value);
                return;
            }

            if (value instanceof Json.Serializable) {
                writeObjectStart(actualType, knownType);
                ((Json.Serializable)value).write((Json) (Object) this);
                writeObjectEnd();
                return;
            }

            Json.Serializer serializer = classToSerializer.get(actualType);
            if (serializer != null) {
                serializer.write((Json) (Object) this, value, knownType);
                return;
            }

            // JSON array special cases.
            if (value instanceof Array) {
                if (knownType != null && actualType != knownType && actualType != Array.class)
                    throw new SerializationException("Serialization of an Array other than the known type is not supported.\n"
                            + "Known type: " + knownType + "\nActual type: " + actualType);
                writeArrayStart();
                Array array = (Array)value;
                for (int i = 0, n = array.size; i < n; i++)
                    writeValue(array.get(i), elementType, null);
                writeArrayEnd();
                return;
            }
            if (value instanceof Queue) {
                if (knownType != null && actualType != knownType && actualType != Queue.class)
                    throw new SerializationException("Serialization of a Queue other than the known type is not supported.\n"
                            + "Known type: " + knownType + "\nActual type: " + actualType);
                writeArrayStart();
                Queue queue = (Queue)value;
                for (int i = 0, n = queue.size; i < n; i++)
                    writeValue(queue.get(i), elementType, null);
                writeArrayEnd();
                return;
            }
            if (value instanceof Collection) {
                if (typeName != null && actualType != ArrayList.class && (knownType == null || knownType != actualType)) {
                    writeObjectStart(actualType, knownType);
                    writeArrayStart("items");
                    for (Object item : (Collection)value)
                        writeValue(item, elementType, null);
                    writeArrayEnd();
                    writeObjectEnd();
                } else {
                    writeArrayStart();
                    for (Object item : (Collection)value)
                        writeValue(item, elementType, null);
                    writeArrayEnd();
                }
                return;
            }
            if (actualType.isArray()) {
                if (elementType == null) elementType = actualType.getComponentType();
                int length = ArrayReflection.getLength(value);
                writeArrayStart();
                for (int i = 0; i < length; i++)
                    writeValue(ArrayReflection.get(value, i), elementType, null);
                writeArrayEnd();
                return;
            }

            // JSON object special cases.
            if (value instanceof ObjectMap) {
                if (knownType == null) knownType = ObjectMap.class;
                writeObjectStart(actualType, knownType);
                for (ObjectMap.Entry entry : ((ObjectMap<?, ?>)value).entries()) {
                    writer.name(convertToString(entry.key));
                    writeValue(entry.value, elementType, null);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof ObjectIntMap) {
                if (knownType == null) knownType = ObjectIntMap.class;
                writeObjectStart(actualType, knownType);
                for (ObjectIntMap.Entry entry : ((ObjectIntMap<?>)value).entries()) {
                    writer.name(convertToString(entry.key));
                    writeValue(entry.value, Integer.class);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof ObjectFloatMap) {
                if (knownType == null) knownType = ObjectFloatMap.class;
                writeObjectStart(actualType, knownType);
                for (ObjectFloatMap.Entry entry : ((ObjectFloatMap<?>)value).entries()) {
                    writer.name(convertToString(entry.key));
                    writeValue(entry.value, Float.class);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof ObjectSet) {
                if (knownType == null) knownType = ObjectSet.class;
                writeObjectStart(actualType, knownType);
                writer.name("values");
                writeArrayStart();
                for (Object entry : (ObjectSet)value)
                    writeValue(entry, elementType, null);
                writeArrayEnd();
                writeObjectEnd();
                return;
            }
            if (value instanceof IntMap) {
                if (knownType == null) knownType = IntMap.class;
                writeObjectStart(actualType, knownType);
                for (IntMap.Entry entry : ((IntMap<?>)value).entries()) {
                    writer.name(String.valueOf(entry.key));
                    writeValue(entry.value, elementType, null);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof LongMap) {
                if (knownType == null) knownType = LongMap.class;
                writeObjectStart(actualType, knownType);
                for (LongMap.Entry entry : ((LongMap<?>)value).entries()) {
                    writer.name(String.valueOf(entry.key));
                    writeValue(entry.value, elementType, null);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof IntSet) {
                if (knownType == null) knownType = IntSet.class;
                writeObjectStart(actualType, knownType);
                writer.name("values");
                writeArrayStart();
                for (IntSet.IntSetIterator iter = ((IntSet)value).iterator(); iter.hasNext;)
                    writeValue(iter.next(), Integer.class, null);
                writeArrayEnd();
                writeObjectEnd();
                return;
            }
            if (value instanceof ArrayMap) {
                if (knownType == null) knownType = ArrayMap.class;
                writeObjectStart(actualType, knownType);
                ArrayMap map = (ArrayMap)value;
                for (int i = 0, n = map.size; i < n; i++) {
                    writer.name(convertToString(map.keys[i]));
                    writeValue(map.values[i], elementType, null);
                }
                writeObjectEnd();
                return;
            }
            if (value instanceof Map) {
                if (knownType == null) knownType = HashMap.class;
                writeObjectStart(actualType, knownType);
                for (Map.Entry entry : ((Map<?, ?>)value).entrySet()) {
                    writer.name(convertToString(entry.getKey()));
                    writeValue(entry.getValue(), elementType, null);
                }
                writeObjectEnd();
                return;
            }

            // Enum special case.
            if (ClassReflection.isAssignableFrom(Enum.class, actualType)) {
                if (actualType.getEnumConstants() == null) // Get the enum type when an enum value is an inner class (enum A {b{}}).
                    actualType = actualType.getSuperclass();
                if (typeName != null && (knownType == null || knownType != actualType)) {
                    writeObjectStart(actualType, null);
                    writer.name("value");
                    writer.value(convertToString((Enum)value));
                    writeObjectEnd();
                } else {
                    writer.value(convertToString((Enum)value));
                }
                return;
            }

            writeObjectStart(actualType, knownType);
            writeFields(value);
            writeObjectEnd();
        } catch (IOException ex) {
            throw new SerializationException(ex);
        }
    }

}
