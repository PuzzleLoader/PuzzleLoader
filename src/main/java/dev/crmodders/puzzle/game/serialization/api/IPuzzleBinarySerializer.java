package dev.crmodders.puzzle.game.serialization.api;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.impl.wrappers.PuppetBinarySerializer;
import finalforeach.cosmicreach.io.CosmicReachBinarySerializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;

public interface IPuzzleBinarySerializer {

    static CosmicReachBinarySerializer createPuppetSerializer(IPuzzleBinarySerializer serializer) {
        return new PuppetBinarySerializer(serializer);
    }

    // Array Writers
    void writeStringArray(String name, String[] array);
    void writeBooleanArray(String name, boolean[] array);
    void writeByteArray(String name, byte[] array);
    void writeShortArray(String name, short[] array);
    void writeIntArray(String name, int[] array);
    void writeLongArray(String name, long[] array);
    void writeFloatArray(String name, float[] array);
    void writeDoubleArray(String name, double[] array);
    <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, Array<T> array);
    <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, T[] array);

    // Single Element Writers
    void writeString(String name, String value);
    void writeBoolean(String name, boolean bool);
    void writeByte(String name, byte i);
    void writeInt(String name, int i);
    void writeLong(String name, long l);
    void writeFloat(String name, float f);
    void writeDouble(String name, double d);
    void writeVector2(String name, Vector2 vector);
    void writeVector3(String name, Vector3 vector);
    void writeBoundingBox(String name, BoundingBox bb);
    <T extends ICosmicReachBinarySerializable> void writeObj(String name, T item);

    // Serializer Outputs
    byte[] toBytes();
    String toBase64();

    String getFileExt();

}
