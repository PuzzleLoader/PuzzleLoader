package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.*;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinaryDeserializer;
import dev.crmodders.puzzle.utils.MethodUtil;
import dev.dewy.nbt.Nbt;
import dev.dewy.nbt.api.registry.TagTypeRegistry;
import dev.dewy.nbt.tags.collection.CompoundTag;
import finalforeach.cosmicreach.io.CosmicReachBinaryDeserializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import finalforeach.cosmicreach.savelib.crbin.CosmicReachBinarySchema;

import java.io.*;
import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.zip.GZIPInputStream;

public class PuzzleNBTDeserializer implements IPuzzleBinaryDeserializer {

    public CompoundTag compound;

    public PuzzleNBTDeserializer() {
    }

    public PuzzleNBTDeserializer(CompoundTag tag) {
        this.compound = tag;
    }

    public static CompoundTag compoundFromStream(DataInputStream stream) {
        TagTypeRegistry registry = new TagTypeRegistry();
        CompoundTag result = new CompoundTag();
        try {
            stream.readByte();
            result.setName(stream.readUTF());

            result.read(stream, 0, registry);
        } catch (EOFException ignore) {
        } catch (IOException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
        return result;
    }

    public static CompoundTag compoundFromByteArray(byte[] bytes) {
        DataInputStream input = new DataInputStream(new ByteArrayInputStream(bytes));
        return compoundFromStream(input);
    }

    public static CompoundTag compoundFromBase64(String base64) {
        return compoundFromByteArray(Base64.getDecoder().decode(base64));
    }

    public static CosmicReachBinaryDeserializer fromBase64(String base64) {
        PuzzleNBTDeserializer deserial = new PuzzleNBTDeserializer();
        ByteBuffer byteBuf = ByteBuffer.wrap(Base64.getDecoder().decode(base64));
        deserial.prepareForRead(byteBuf);
        return new PuppetBinaryDeserializer(deserial);
    }

    @Override
    public IPuzzleBinaryDeserializer newInst() {
        return new PuzzleNBTDeserializer();
    }

    public void readDataFromSchema(CosmicReachBinarySchema schema, ByteBuffer bytes) {
        compound = PuzzleNBTDeserializer.compoundFromByteArray(bytes.array());
    }

    public String[] readStringArray(String name) {
        CompoundTag tag = compound.getCompound(name);
        int length = tag.getInt("len").getValue();

        String[] stringArray = new String[length];

        for (int i = 0; i < length; i++) {
            stringArray[i] = tag.getString(String.valueOf(i)).getValue();
        }

        return stringArray;
    }

    public boolean[] readBooleanArray(String name) {
        byte[] ints = compound.getByteArray(name).getValue();
        boolean[] booleans = new boolean[ints.length];
        for (int i = 0; i < ints.length; i++) {
            booleans[i] = ints[i] != 0;
        }
        return booleans;
    }

    public byte[] readByteArray(String name) {
        return compound.getByteArray(name).getValue();
    }

    public short[] readShortArray(String name) {
        int[] ints = compound.getIntArray(name).getValue();
        short[] shorts = new short[ints.length];
        for (int i = 0; i < ints.length; i++) {
            shorts[i] = (short) ints[i];
        }
        return shorts;
    }

    public int[] readIntArray(String name) {
        return compound.getIntArray(name).getValue();
    }

    public long[] readLongArray(String name) {
        return compound.getLongArray(name).getValue();
    }

    public float[] readFloatArray(String name) {
        CompoundTag tag = compound.getCompound(name);
        int length = tag.getInt("len").getValue();

        float[] floatArray = new float[length];

        for (int i = 0; i < length; i++) {
            floatArray[i] = tag.getFloat(String.valueOf(i)).getValue();
        }

        return floatArray;
    }

    public double[] readDoubleArray(String name) {
        CompoundTag tag = compound.getCompound(name);
        int length = tag.getInt("len").getValue();

        double[] doubleArray = new double[length];

        for (int i = 0; i < length; i++) {
            doubleArray[i] = tag.getDouble(String.valueOf(i)).getValue();
        }

        return doubleArray;
    }

    public void prepareForRead(ByteBuffer bytes) {
        if (bytes.array().length != 0) {
            compound = PuzzleNBTDeserializer.compoundFromByteArray(bytes.array());
        }
        else compound = new CompoundTag();
    }

    public int readInt(String name, int defaultValue) {
        return compound.contains(name) ? compound.getInt(name).getValue() : defaultValue;
    }

    public long readLong(String name, long defaultValue) {
        return compound.contains(name) ? compound.getLong(name).getValue() : defaultValue;
    }

    public short readShort(String name, short defaultValue) {
        return compound.contains(name) ? compound.getShort(name).getValue() : defaultValue;
    }

    public float readFloat(String name, float defaultValue) {
        return compound.contains(name) ? compound.getFloat(name).getValue() : defaultValue;
    }

    @Override
    public double readDouble(String name, double defaultValue) {
        return compound.contains(name) ? compound.getDouble(name).getValue() : defaultValue;
    }

    public boolean readBoolean(String name, boolean defaultValue) {
        return compound.contains(name) ? compound.getByte(name).getValue() != 0 : defaultValue;
    }

    @Override
    public byte readByte(String name, byte defaultValue) {
        return compound.contains(name) ? compound.getByte(name).getValue() : defaultValue;
    }

    public String readString(String name) {
        return compound.getString(name).getValue();
    }

    private <T extends ICosmicReachBinarySerializable> T readObj(Class<T> elementType, CosmicReachBinaryDeserializer d) {
        T obj = MethodUtil.newInstance(elementType);
        obj.read(d);
        return obj;
    }

    public <T extends ICosmicReachBinarySerializable> T readObj(String name, Class<T> elementType) {
        if (!compound.contains(name)) return null;

        T obj = MethodUtil.newInstance(elementType);
        obj.read(new PuppetBinaryDeserializer(new PuzzleNBTDeserializer(compound.getCompound(name))));
        return obj;
    }

    private PuzzleNBTDeserializer readObj(CompoundTag tag, int i) {
        PuzzleNBTDeserializer deserializer = new PuzzleNBTDeserializer();
        deserializer.compound = tag.getCompound(String.valueOf(i));
        return deserializer;
    }

    public CosmicReachBinaryDeserializer[] readRawObjArray(String name) {
        CompoundTag tag = compound.getCompound(name);
        int length = tag.getInt("len").getValue();

        CosmicReachBinaryDeserializer[] objArray = new CosmicReachBinaryDeserializer[length];

        for (int i = 0; i < length; i++) {
            objArray[i] = new PuppetBinaryDeserializer(readObj(tag, i));
        }

        return objArray;
    }

    public <T extends ICosmicReachBinarySerializable> Array<T> readObjArray(String name, Class<T> elementType) {
        CompoundTag tag = compound.getCompound(name);
        int length = tag.getInt("len").getValue();

        Array<T> arr = new Array<>(length);

        for (int i = 0; i < length; i++) {
            arr.add(readObj(elementType, new PuppetBinaryDeserializer(readObj(tag, i))));
        }

        return arr;
    }

    public Vector2 readVector2(String name) {
        CompoundTag vec = compound.getCompound(name);
        return new Vector2(
                vec.getFloat("x").getValue(),
                vec.getFloat("y").getValue()
        );
    }

    public Vector3 readVector3(String name) {
        CompoundTag vec = compound.getCompound(name);
        return new Vector3(
                vec.getFloat("x").getValue(),
                vec.getFloat("y").getValue(),
                vec.getFloat("z").getValue()
        );
    }

    public BoundingBox readBoundingBox(String name) {
        float[] f = this.readFloatArray(name);
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
}
