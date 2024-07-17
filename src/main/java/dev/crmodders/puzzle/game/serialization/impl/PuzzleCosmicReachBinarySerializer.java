package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.ByteArray;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinarySerializer;
import dev.crmodders.puzzle.game.serialization.impl.wrappers.PuppetBinarySerializer;
import finalforeach.cosmicreach.io.ByteArrayUtils;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import finalforeach.cosmicreach.savelib.crbin.CosmicReachBinarySchema;
import finalforeach.cosmicreach.savelib.crbin.SchemaType;
import org.jetbrains.annotations.NotNull;

import java.util.Base64;
import java.util.function.IntConsumer;

public class PuzzleCosmicReachBinarySerializer implements IPuzzleBinarySerializer {
    CosmicReachBinarySchema schema = new CosmicReachBinarySchema();
    Array<CosmicReachBinarySchema> altSchemas = new Array<>(CosmicReachBinarySchema.class);
    Array<String> strings = new Array<>();
    ByteArray bytes = new ByteArray();

    public PuzzleCosmicReachBinarySerializer() {
    }

    public <T extends ICosmicReachBinarySerializable> void writeObj(String name, T item) {
        CosmicReachBinarySchema oldSchema = this.schema;
        ByteArray oldBytes = this.bytes;
        this.bytes = new ByteArray();
        if (name != null) {
            oldSchema.add(name, SchemaType.OBJ);
        }

        if (item != null) {
            this.schema = new CosmicReachBinarySchema();
            item.write(new PuppetBinarySerializer(this));
            if (!this.altSchemas.contains(this.schema, false)) {
                this.altSchemas.add(this.schema);
            }

            for(int i = 0; i < this.altSchemas.size; ++i) {
                CosmicReachBinarySchema s = (CosmicReachBinarySchema)this.altSchemas.get(i);
                if (s.equals(this.schema)) {
                    ByteArrayUtils.writeInt(oldBytes, i);
                    break;
                }
            }
        } else {
            ByteArrayUtils.writeInt(oldBytes, -1);
        }

        oldBytes.addAll(this.bytes);
        this.schema = oldSchema;
        this.bytes = oldBytes;
    }

    public byte[] toBytes() {
        ByteArray bytesToWrite = new ByteArray();
        ByteArrayUtils.writeInt(bytesToWrite, this.strings.size);
        Array.ArrayIterator var2 = this.strings.iterator();

        while(var2.hasNext()) {
            String s = (String)var2.next();
            ByteArrayUtils.writeString(bytesToWrite, s);
        }

        bytesToWrite.addAll(this.schema.getBytes());
        ByteArrayUtils.writeInt(bytesToWrite, this.altSchemas.size);
        var2 = this.altSchemas.iterator();

        while(var2.hasNext()) {
            CosmicReachBinarySchema s = (CosmicReachBinarySchema)var2.next();
            bytesToWrite.addAll(s.getBytes());
        }

        bytesToWrite.addAll(this.bytes);
        return bytesToWrite.toArray();
    }

    public String toBase64() {
        return Base64.getEncoder().encodeToString(this.toBytes());
    }

    @Override
    public String getFileExt() {
        return ".crbin";
    }

    private void writeArray(String name, SchemaType type, int arrayLen, IntConsumer forEach) {
        this.schema.add(name, type);
        ByteArrayUtils.writeInt(this.bytes, arrayLen);

        for(int i = 0; i < arrayLen; ++i) {
            forEach.accept(i);
        }

    }

    public void writeByteArray(String name, byte @NotNull [] array) {
        this.writeArray(name, SchemaType.BYTE_ARRAY, array.length, (i) -> {
            this.writeByte((String)null, array[i]);
        });
    }

    public void writeBooleanArray(String name, boolean @NotNull [] array) {
        this.writeArray(name, SchemaType.BOOLEAN_ARRAY, array.length, (i) -> {
            this.writeBoolean((String)null, array[i]);
        });
    }

    public void writeShortArray(String name, short @NotNull [] array) {
        this.writeArray(name, SchemaType.SHORT_ARRAY, array.length, (i) -> {
            this.writeShort((String)null, array[i]);
        });
    }

    public void writeIntArray(String name, int @NotNull [] array) {
        this.writeArray(name, SchemaType.INT_ARRAY, array.length, (i) -> {
            this.writeInt((String)null, array[i]);
        });
    }

    public void writeLongArray(String name, long @NotNull [] array) {
        this.writeArray(name, SchemaType.LONG_ARRAY, array.length, (i) -> {
            this.writeLong((String)null, array[i]);
        });
    }

    public void writeFloatArray(String name, float @NotNull [] array) {
        this.writeArray(name, SchemaType.FLOAT_ARRAY, array.length, (i) -> {
            this.writeFloat((String)null, array[i]);
        });
    }

    public void writeDoubleArray(String name, double @NotNull [] array) {
        this.writeArray(name, SchemaType.DOUBLE_ARRAY, array.length, (i) -> {
            this.writeDouble((String)null, array[i]);
        });
    }

    public void writeStringArray(String name, String @NotNull [] array) {
        this.writeArray(name, SchemaType.STRING_ARRAY, array.length, (i) -> {
            this.writeString((String)null, array[i]);
        });
    }

    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, @NotNull Array<T> array) {
        this.writeArray(name, SchemaType.OBJ_ARRAY, array.size, (i) -> {
            this.writeObj((String)null, (ICosmicReachBinarySerializable)array.get(i));
        });
    }

    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, T @NotNull [] array) {
        this.writeArray(name, SchemaType.OBJ_ARRAY, array.length, (i) -> {
            this.writeObj((String)null, array[i]);
        });
    }

    public void writeBoolean(String name, boolean bool) {
        this.schema.add(name, SchemaType.BOOLEAN);
        ByteArrayUtils.writeByte(this.bytes, bool ? 1 : 0);
    }

    public void writeByte(String name, byte i) {
        this.schema.add(name, SchemaType.BYTE);
        ByteArrayUtils.writeByte(this.bytes, i);
    }

    public void writeInt(String name, int i) {
        this.schema.add(name, SchemaType.INT);
        ByteArrayUtils.writeInt(this.bytes, i);
    }

    public void writeShort(String name, short s) {
        this.schema.add(name, SchemaType.SHORT);
        ByteArrayUtils.writeShort(this.bytes, s);
    }

    public void writeLong(String name, long l) {
        this.schema.add(name, SchemaType.LONG);
        ByteArrayUtils.writeLong(this.bytes, l);
    }

    public void writeFloat(String name, float f) {
        this.schema.add(name, SchemaType.FLOAT);
        ByteArrayUtils.writeFloat(this.bytes, f);
    }

    public void writeDouble(String name, double d) {
        this.schema.add(name, SchemaType.DOUBLE);
        ByteArrayUtils.writeDouble(this.bytes, d);
    }

    public void writeString(String name, String value) {
        this.schema.add(name, SchemaType.STRING);
        int stringId = this.strings.indexOf(value, false);
        if (stringId == -1) {
            stringId = this.strings.size;
            this.strings.add(value);
        }

        ByteArrayUtils.writeInt(this.bytes, stringId);
    }

    public void writeVector2(String name, Vector2 vector) {
        this.writeFloatArray(name, new float[]{vector.x, vector.y});
    }

    public void writeVector3(String name, @NotNull Vector3 vector) {
        this.writeFloatArray(name, new float[]{vector.x, vector.y, vector.z});
    }

    public void writeBoundingBox(String name, @NotNull BoundingBox bb) {
        this.writeFloatArray(name, new float[]{bb.min.x, bb.min.y, bb.min.z, bb.max.x, bb.max.y, bb.max.z});
    }
}
