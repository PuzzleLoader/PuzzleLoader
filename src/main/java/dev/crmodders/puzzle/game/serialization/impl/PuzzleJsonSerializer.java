package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinarySerializer;
import dev.crmodders.puzzle.game.serialization.impl.wrappers.PuppetBinarySerializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import org.hjson.JsonArray;
import org.hjson.JsonObject;

import java.util.Base64;

public class PuzzleJsonSerializer implements IPuzzleBinarySerializer {

    JsonObject object = new JsonObject();

    @Override
    public void writeStringArray(String name, String[] array) {
        JsonArray array1 = new JsonArray();
        for (String s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    @Override
    public void writeBooleanArray(String name, boolean[] array) {
        JsonArray array1 = new JsonArray();
        for (boolean s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    @Override
    public void writeByteArray(String name, byte[] array) {
        JsonArray array1 = new JsonArray();
        for (int s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    @Override
    public void writeShortArray(String name, short[] array) {
        JsonArray array1 = new JsonArray();
        for (int s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    @Override
    public void writeIntArray(String name, int[] array) {
        JsonArray array1 = new JsonArray();
        for (int s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    @Override
    public void writeLongArray(String name, long[] array) {
        JsonArray array1 = new JsonArray();
        for (long s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    @Override
    public void writeFloatArray(String name, float[] array) {
        JsonArray array1 = new JsonArray();
        for (float s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    @Override
    public void writeDoubleArray(String name, double[] array) {
        JsonArray array1 = new JsonArray();
        for (double s : array) {
            array1.add(s);
        }
        object.set(name, array1);
    }

    private <T extends ICosmicReachBinarySerializable> PuzzleJsonSerializer writeObj(T item) {
        PuzzleJsonSerializer serializer = new PuzzleJsonSerializer();
        item.write(new PuppetBinarySerializer(serializer));
        return serializer;
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, Array<T> array) {
        JsonObject tag = new JsonObject();
        tag.set("len", array.size);
        for (int i = 0; i < array.size; i++) {
            tag.set(String.valueOf(i), writeObj(array.get(i)).object);
        }
        object.set(name, tag);
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, T[] array) {
        JsonObject tag = new JsonObject();
        tag.set("len", array.length);
        for (int i = 0; i < array.length; i++) {
            tag.set(String.valueOf(i), writeObj(array[i]).object);
        }
        object.set(name, tag);
    }

    @Override
    public void writeString(String name, String value) {
        object.set(name, value);
    }

    @Override
    public void writeBoolean(String name, boolean bool) {
        object.set(name, bool);
    }

    @Override
    public void writeByte(String name, byte i) {
        object.set(name, i);
    }

    @Override
    public void writeInt(String name, int i) {
        object.set(name, i);
    }

    @Override
    public void writeLong(String name, long l) {
        object.set(name, l);
    }

    @Override
    public void writeFloat(String name, float f) {
        object.set(name, f);
    }

    @Override
    public void writeDouble(String name, double d) {
        object.set(name, d);
    }

    @Override
    public void writeVector2(String name, Vector2 vector) {
        JsonObject object1 = new JsonObject();
        object1.set("x", vector.x);
        object1.set("y", vector.y);

        object.set(name, object1);
    }

    @Override
    public void writeVector3(String name, Vector3 vector) {
        JsonObject object1 = new JsonObject();
        object1.set("x", vector.x);
        object1.set("y", vector.y);
        object1.set("z", vector.z);

        object.set(name, object1);
    }

    @Override
    public void writeBoundingBox(String name, BoundingBox bb) {
        writeFloatArray(name, new float[]{bb.min.x, bb.min.y, bb.min.z, bb.max.x, bb.max.y, bb.max.z});
    }

    public void writeSerializer(String name, PuzzleJsonSerializer serializer) {
        object.set(name, serializer.object);
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> void writeObj(String name, T item) {
        PuzzleJsonSerializer serializer = new PuzzleJsonSerializer();
        item.write(new PuppetBinarySerializer(serializer));
        writeSerializer(name, serializer);
    }

    @Override
    public byte[] toBytes() {
        return object.toString().getBytes();
    }

    @Override
    public String toBase64() {
        return Base64.getEncoder().encodeToString(toBytes());
    }

    @Override
    public String getFileExt() {
        return ".json";
    }
}
