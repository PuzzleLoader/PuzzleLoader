package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinarySerializer;
import finalforeach.cosmicreach.io.CosmicReachBinarySerializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;

public class PuppetBinarySerializer extends CosmicReachBinarySerializer implements IPuzzleBinarySerializer {

    IPuzzleBinarySerializer masterSerializer;

    public PuppetBinarySerializer(IPuzzleBinarySerializer serializer) {
        masterSerializer = serializer;
    }

    @Override
    public void writeStringArray(String name, String[] array) {
        masterSerializer.writeStringArray(name, array);
    }

    @Override
    public void writeBooleanArray(String name, boolean[] array) {
        masterSerializer.writeBooleanArray(name, array);
    }

    @Override
    public void writeByteArray(String name, byte[] array) {
        masterSerializer.writeByteArray(name, array);
    }

    @Override
    public void writeShortArray(String name, short[] array) {
        masterSerializer.writeShortArray(name, array);
    }

    @Override
    public void writeIntArray(String name, int[] array) {
        masterSerializer.writeIntArray(name, array);
    }

    @Override
    public void writeLongArray(String name, long[] array) {
        masterSerializer.writeLongArray(name, array);
    }

    @Override
    public void writeFloatArray(String name, float[] array) {
        masterSerializer.writeFloatArray(name, array);
    }

    @Override
    public void writeDoubleArray(String name, double[] array) {
        masterSerializer.writeDoubleArray(name, array);
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, Array<T> array) {
        masterSerializer.writeObjArray(name, array);
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, T[] array) {
        masterSerializer.writeObjArray(name, array);
    }

    @Override
    public void writeString(String name, String value) {
        masterSerializer.writeString(name, value);
    }

    @Override
    public void writeBoolean(String name, boolean bool) {
        masterSerializer.writeBoolean(name, bool);
    }

    @Override
    public void writeByte(String name, byte i) {
        masterSerializer.writeByte(name, i);
    }

    @Override
    public void writeInt(String name, int i) {
        masterSerializer.writeInt(name, i);
    }

    @Override
    public void writeLong(String name, long l) {
        masterSerializer.writeLong(name, l);
    }

    @Override
    public void writeFloat(String name, float f) {
        masterSerializer.writeFloat(name, f);
    }

    @Override
    public void writeDouble(String name, double d) {
        masterSerializer.writeDouble(name, d);
    }

    @Override
    public void writeVector2(String name, Vector2 vector) {
        masterSerializer.writeVector2(name, vector);
    }

    @Override
    public void writeVector3(String name, Vector3 vector) {
        masterSerializer.writeVector3(name, vector);
    }

    @Override
    public void writeBoundingBox(String name, BoundingBox bb) {
        masterSerializer.writeBoundingBox(name, bb);
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> void writeObj(String name, T item) {
        masterSerializer.writeObj(name, item);
    }

    @Override
    public byte[] toBytes() {
        return masterSerializer.toBytes();
    }

    @Override
    public String toBase64() {
        return masterSerializer.toBase64();
    }
}
