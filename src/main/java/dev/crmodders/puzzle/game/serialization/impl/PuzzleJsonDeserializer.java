package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinaryDeserializer;
import dev.crmodders.puzzle.game.serialization.impl.wrappers.PuppetBinaryDeserializer;
import dev.crmodders.puzzle.util.ClassUtil;
import finalforeach.cosmicreach.io.CosmicReachBinaryDeserializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import finalforeach.cosmicreach.savelib.crbin.CosmicReachBinarySchema;
import org.hjson.JsonArray;
import org.hjson.JsonObject;
import org.jetbrains.annotations.NotNull;

import java.nio.ByteBuffer;

public class PuzzleJsonDeserializer implements IPuzzleBinaryDeserializer {
    JsonObject object;

    public PuzzleJsonDeserializer() {}
    public PuzzleJsonDeserializer(JsonObject object) {
        this.object = object;
    }

    @Override
    public IPuzzleBinaryDeserializer newInst() {
        return new PuzzleJsonDeserializer();
    }

    @Override
    public void readDataFromSchema(CosmicReachBinarySchema schema, @NotNull ByteBuffer byteBuffer) {
        object = JsonObject.readHjson(new String(byteBuffer.array())).asObject();
    }

    @Override
    public void prepareForRead(@NotNull ByteBuffer byteBuffer) {
        object = JsonObject.readHjson(new String(byteBuffer.array())).asObject();
    }

    @Override
    public String[] readStringArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        String[] objs = new String[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = jsonArray.get(i).asString();
        }

        return objs;
    }

    @Override
    public boolean[] readBooleanArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        boolean[] objs = new boolean[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = jsonArray.get(i).asBoolean();
        }

        return objs;
    }

    @Override
    public byte[] readByteArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        byte[] objs = new byte[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = (byte) jsonArray.get(i).asInt();
        }

        return objs;
    }

    @Override
    public short[] readShortArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        short[] objs = new short[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = (short) jsonArray.get(i).asInt();
        }

        return objs;
    }

    @Override
    public int[] readIntArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        int[] objs = new int[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = jsonArray.get(i).asInt();
        }

        return objs;
    }

    @Override
    public long[] readLongArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        long[] objs = new long[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = jsonArray.get(i).asLong();
        }

        return objs;
    }

    @Override
    public float[] readFloatArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        float[] objs = new float[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = jsonArray.get(i).asFloat();
        }

        return objs;
    }

    @Override
    public double[] readDoubleArray(String name) {
        JsonArray jsonArray = object.get(name).asArray();
        double[] objs = new double[jsonArray.size()];

        for (int i = 0; i < jsonArray.size(); i++) {
            objs[i] = jsonArray.get(i).asDouble();
        }

        return objs;
    }

    private @NotNull PuzzleJsonDeserializer readObj(@NotNull JsonObject tag, int i) {
        PuzzleJsonDeserializer deserializer = new PuzzleJsonDeserializer();
        deserializer.object = tag.get(String.valueOf(i)).asObject();
        return deserializer;
    }

    private <T extends ICosmicReachBinarySerializable> @NotNull T readObj(Class<T> elementType, CosmicReachBinaryDeserializer d) {
        T obj = ClassUtil.newInstance(elementType);
        obj.read(d);
        return obj;
    }

    @Override
    public CosmicReachBinaryDeserializer[] readRawObjArray(String name) {
        JsonObject tag = object.get(name).asObject();
        int length = tag.get("len").asInt();

        CosmicReachBinaryDeserializer[] objArray = new CosmicReachBinaryDeserializer[length];

        for (int i = 0; i < length; i++) {
            objArray[i] = new PuppetBinaryDeserializer(readObj(tag, i));
        }

        return objArray;
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> Array<T> readObjArray(String name, Class<T> baseType) {
        JsonObject tag = object.get(name).asObject();
        int length = tag.get("len").asInt();

        Array<T> arr = new Array<>(length);

        for (int i = 0; i < length; i++) {
            arr.add(readObj(baseType, new PuppetBinaryDeserializer(readObj(tag, i))));
        }

        return arr;
    }

    @Override
    public String readString(String name) {
        return object.get(name).asString();
    }

    @Override
    public boolean readBoolean(String name, boolean defaultValue) {
        return object.get(name) != null ? object.get(name).asBoolean() : defaultValue;
    }

    @Override
    public byte readByte(String name, byte defaultValue) {
        return object.get(name) != null ? (byte) object.get(name).asInt() : defaultValue;
    }

    @Override
    public int readInt(String name, int defaultValue) {
        return object.get(name) != null ? object.get(name).asInt() : defaultValue;
    }

    @Override
    public long readLong(String name, long defaultValue) {
        return object.get(name) != null ? object.get(name).asLong() : defaultValue;
    }

    @Override
    public float readFloat(String name, float defaultValue) {
        return object.get(name) != null ? object.get(name).asFloat() : defaultValue;
    }

    @Override
    public double readDouble(String name, double defaultValue) {
        return object.get(name) != null ? object.get(name).asDouble() : defaultValue;
    }

    @Override
    public Vector2 readVector2(String name) {
        JsonObject object1 = object.get(name).asObject();
        return new Vector2(
                object1.get("x").asFloat(),
                object1.get("y").asFloat()
        );
    }

    @Override
    public Vector3 readVector3(String name) {
        JsonObject object1 = object.get(name).asObject();
        return new Vector3(
                object1.get("x").asFloat(),
                object1.get("y").asFloat(),
                object1.get("z").asFloat()
        );
    }

    @Override
    public BoundingBox readBoundingBox(String name) {
        float[] f = readFloatArray(name);
        if (f.length != 6) {
            throw new RuntimeException("readBoundingBox: Expected 6 floats, but got " + f.length + " instead!");
        } else {
            BoundingBox bb = new BoundingBox();
            bb.min.set(f[0], f[1], f[2]);
            bb.max.set(f[3], f[4], f[5]);
            bb.update();
            return bb;
        }
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> T readObj(String name, Class<T> baseType) {
        if (object.get(name) == null) return null;

        T obj = ClassUtil.newInstance(baseType);
        obj.read(new PuppetBinaryDeserializer(new PuzzleJsonDeserializer(object.get(name).asObject())));
        return obj;
    }
}
