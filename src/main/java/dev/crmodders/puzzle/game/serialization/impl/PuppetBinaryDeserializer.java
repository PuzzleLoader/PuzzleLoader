package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinaryDeserializer;
import finalforeach.cosmicreach.io.CosmicReachBinaryDeserializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import finalforeach.cosmicreach.savelib.crbin.CosmicReachBinarySchema;

import java.nio.ByteBuffer;

public class PuppetBinaryDeserializer extends CosmicReachBinaryDeserializer implements IPuzzleBinaryDeserializer {

    IPuzzleBinaryDeserializer masterDeserializer;

    public PuppetBinaryDeserializer(IPuzzleBinaryDeserializer deserializer) {
        masterDeserializer = deserializer;
    }

    @Override
    public IPuzzleBinaryDeserializer newInst() {
        return masterDeserializer.newInst();
    }

    @Override
    public void readDataFromSchema(CosmicReachBinarySchema schema, ByteBuffer byteBuffer) {
        masterDeserializer.readDataFromSchema(schema, byteBuffer);
    }

    @Override
    public void prepareForRead(ByteBuffer byteBuffer) {
        masterDeserializer.prepareForRead(byteBuffer);
    }

    @Override
    public String[] readStringArray(String name) {
        return masterDeserializer.readStringArray(name);
    }

    @Override
    public boolean[] readBooleanArray(String name) {
        return masterDeserializer.readBooleanArray(name);
    }

    @Override
    public byte[] readByteArray(String name) {
        return masterDeserializer.readByteArray(name);
    }

    @Override
    public short[] readShortArray(String name) {
        return masterDeserializer.readShortArray(name);
    }

    @Override
    public int[] readIntArray(String name) {
        return masterDeserializer.readIntArray(name);
    }

    @Override
    public long[] readLongArray(String name) {
        return masterDeserializer.readLongArray(name);
    }

    @Override
    public float[] readFloatArray(String name) {
        return masterDeserializer.readFloatArray(name);
    }

    @Override
    public double[] readDoubleArray(String name) {
        return masterDeserializer.readDoubleArray(name);
    }

    @Override
    public CosmicReachBinaryDeserializer[] readRawObjArray(String name) {
        return masterDeserializer.readRawObjArray(name);
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> Array<T> readObjArray(String name, Class<T> baseType) {
        return masterDeserializer.readObjArray(name, baseType);
    }

    @Override
    public String readString(String name) {
        return masterDeserializer.readString(name);
    }

    @Override
    public boolean readBoolean(String name, boolean defaultValue) {
        return masterDeserializer.readBoolean(name, defaultValue);
    }

    @Override
    public byte readByte(String name, byte defaultValue) {
        return masterDeserializer.readByte(name, defaultValue);
    }

    @Override
    public int readInt(String name, int defaultValue) {
        return masterDeserializer.readInt(name, defaultValue);
    }

    @Override
    public long readLong(String name, long defaultValue) {
        return masterDeserializer.readLong(name, defaultValue);
    }

    @Override
    public float readFloat(String name, float defaultValue) {
        return masterDeserializer.readFloat(name, defaultValue);
    }

    @Override
    public double readDouble(String name, double defaultValue) {
        return masterDeserializer.readDouble(name, defaultValue);
    }

    @Override
    public Vector2 readVector2(String name) {
        return masterDeserializer.readVector2(name);
    }

    @Override
    public Vector3 readVector3(String name) {
        return masterDeserializer.readVector3(name);
    }

    @Override
    public BoundingBox readBoundingBox(String name) {
        return masterDeserializer.readBoundingBox(name);
    }

    @Override
    public <T extends ICosmicReachBinarySerializable> T readObj(String name, Class<T> baseType) {
        return masterDeserializer.readObj(name, baseType);
    }
}
