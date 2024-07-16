package dev.crmodders.puzzle.game.serialization.api;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.impl.PuppetBinaryDeserializer;
import finalforeach.cosmicreach.io.CosmicReachBinaryDeserializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import finalforeach.cosmicreach.savelib.crbin.CosmicReachBinarySchema;

import java.nio.ByteBuffer;

public interface IPuzzleBinaryDeserializer {

    static CosmicReachBinaryDeserializer createPuppetDeserializer(IPuzzleBinaryDeserializer deserializer) {
        return new PuppetBinaryDeserializer(deserializer);
    }

    // Deserializer Initializers
    IPuzzleBinaryDeserializer newInst();

    void readDataFromSchema(CosmicReachBinarySchema schema, ByteBuffer byteBuffer);
    void prepareForRead(ByteBuffer byteBuffer);

    // Array Readers
    String[] readStringArray(String name);
    boolean[] readBooleanArray(String name);
    byte[] readByteArray(String name);
    short[] readShortArray(String name);
    int[] readIntArray(String name);
    long[] readLongArray(String name);
    float[] readFloatArray(String name);
    double[] readDoubleArray(String name);
    CosmicReachBinaryDeserializer[] readRawObjArray(String name);
    <T extends ICosmicReachBinarySerializable> Array<T> readObjArray(String name, Class<T> baseType);

    // Single Element Readers
    String readString(String name);
    boolean readBoolean(String name, boolean defaultValue);
    byte readByte(String name, byte defaultValue);
    int readInt(String name, int defaultValue);
    long readLong(String name, long defaultValue);
    float readFloat(String name, float defaultValue);
    double readDouble(String name, double defaultValue);
    Vector2 readVector2(String name);
    Vector3 readVector3(String name);
    BoundingBox readBoundingBox(String name);
    <T extends ICosmicReachBinarySerializable> T readObj(String name, Class<T> baseType);

}
