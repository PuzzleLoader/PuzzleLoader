package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinarySerializer;
import dev.crmodders.puzzle.game.serialization.impl.wrappers.PuppetBinarySerializer;
import dev.dewy.nbt.api.registry.TagTypeRegistry;
import dev.dewy.nbt.api.snbt.SnbtConfig;
import dev.dewy.nbt.tags.TagType;
import dev.dewy.nbt.tags.collection.CompoundTag;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import org.jetbrains.annotations.NotNull;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.function.Supplier;

public class PuzzleNBTSerializer implements IPuzzleBinarySerializer {
    public CompoundTag compound = new CompoundTag();

    public PuzzleNBTSerializer() {
    }

    public void writeSerializer(String name, @NotNull PuzzleNBTSerializer serializer) {
        compound.put(name, serializer.compound);
    }

    public static byte @NotNull [] compoundToByteArray(@NotNull CompoundTag tag) {
        TagTypeRegistry registry = new TagTypeRegistry();

        ByteArrayOutputStream stream = new ByteArrayOutputStream();

        try {
            DataOutputStream dos = new DataOutputStream(stream);

            dos.writeByte(TagType.COMPOUND.getId());
            dos.writeUTF(tag.getName() == null ? "serializationMaster" : tag.getName());
            tag.write(dos, 0, registry);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return stream.toByteArray();
    }

    public static @NotNull String compoundToBase64(CompoundTag tag) {
        byte[] bytes = compoundToByteArray(tag);
        return new String(Base64.getEncoder().encode(bytes), StandardCharsets.UTF_8);
    }

    public static void print(CompoundTag tag) {
        if (tag == null) return;
        TagTypeRegistry registry = new TagTypeRegistry();
        SnbtConfig config = new SnbtConfig();
        config.setPrettyPrint(true);
        System.out.println("BASE64 -> " + compoundToBase64(tag));
        System.out.println(tag.toSnbt(2, registry, config));
    }

    public <T extends ICosmicReachBinarySerializable> void writeObj(String name, @NotNull T item) {
        PuzzleNBTSerializer serializer = new PuzzleNBTSerializer();
        item.write(new PuppetBinarySerializer(serializer));
        writeSerializer(name, serializer);
    }

    private <T extends ICosmicReachBinarySerializable> @NotNull PuzzleNBTSerializer writeObj(@NotNull T item) {
        PuzzleNBTSerializer serializer = new PuzzleNBTSerializer();
        item.write(new PuppetBinarySerializer(serializer));
        return serializer;
    }

    public byte[] toBytes() {
        compound.setName("serializationMaster");
        return compoundToByteArray(compound);
    }

    public String toBase64() {
        compound.setName("serializationMaster");
        return compoundToBase64(compound);
    }

    @Override
    public String getFileExt() {
        return ".nbt";
    }

    public void writeByteArray(String name, byte[] array) {
        compound.putByteArray(name, array);
    }

    public void writeBooleanArray(String name, boolean[] array) {
        compound.putByteArray(name, ((Supplier<byte[]>) () -> {
            byte[] bytes = new byte[array.length];
            for (int i = 0; i < array.length; i++) {
                bytes[i] = (byte) (array[i] ? 1 : 0);
            }
            return bytes;
        }).get());
    }

    public void writeShortArray(String name, short[] array) {
        compound.putIntArray(name, ((Supplier<int[]>) () -> {
            int[] bytes = new int[array.length];
            for (int i = 0; i < array.length; i++) {
                bytes[i] = array[i];
            }
            return bytes;
        }).get());
    }

    public void writeIntArray(String name, int[] array) {
        compound.putIntArray(name, array);
    }

    public void writeLongArray(String name, long[] array) {
        compound.putLongArray(name, array);
    }

    public void writeFloatArray(String name, float @NotNull [] array) {
        CompoundTag tag = new CompoundTag(name);
        tag.putInt("len", array.length);
        for (int i = 0; i < array.length; i++) {
            tag.putFloat(String.valueOf(i), array[i]);
        }
        compound.put(tag);
    }

    public void writeDoubleArray(String name, double @NotNull [] array) {
        CompoundTag tag = new CompoundTag(name);
        tag.putInt("len", array.length);
        for (int i = 0; i < array.length; i++) {
            tag.putDouble(String.valueOf(i), array[i]);
        }
        compound.put(tag);
    }

    public void writeStringArray(String name, String @NotNull [] array) {
        CompoundTag tag = new CompoundTag(name);
        tag.putInt("len", array.length);
        for (int i = 0; i < array.length; i++) {
            tag.putString(String.valueOf(i), array[i]);
        }
        compound.put(tag);
    }

    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, @NotNull Array<T> array) {
        CompoundTag tag = new CompoundTag(name);
        tag.putInt("len", array.size);
        for (int i = 0; i < array.size; i++) {
            tag.put(String.valueOf(i), writeObj(array.get(i)).compound);
        }
        compound.put(tag);
    }

    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, T @NotNull [] array) {
        CompoundTag tag = new CompoundTag(name);
        tag.putInt("len", array.length);
        for (int i = 0; i < array.length; i++) {
            tag.put(String.valueOf(i), writeObj(array[i]).compound);
        }
        compound.put(tag);
    }

    public void writeBoolean(String name, boolean bool) {
        compound.putByte(name, (byte) (bool ? 1 : 0));
    }

    public void writeByte(String name, byte i) {
        compound.putByte(name, i);
    }

    public void writeInt(String name, int i) {
        compound.putInt(name, i);
    }

    public void writeShort(String name, short s) {
        compound.putShort(name, s);
    }

    public void writeLong(String name, long l) {
        compound.putLong(name, l);
    }

    public void writeFloat(String name, float f) {
        compound.putFloat(name, f);
    }

    public void writeDouble(String name, double d) {
        compound.putDouble(name, d);
    }

    public void writeString(String name, String value) {
        compound.putString(name, value);
    }

    public void writeVector2(String name, @NotNull Vector2 vector) {
        CompoundTag tag = new CompoundTag(name);
        tag.putFloat("x", vector.x);
        tag.putFloat("y", vector.y);
        compound.put(tag);
    }

    public void writeVector3(String name, @NotNull Vector3 vector) {
        CompoundTag tag = new CompoundTag(name);
        tag.putFloat("x", vector.x);
        tag.putFloat("y", vector.y);
        tag.putFloat("z", vector.z);
        compound.put(tag);
    }

    public void writeBoundingBox(String name, @NotNull BoundingBox bb) {
        writeFloatArray(name, new float[]{bb.min.x, bb.min.y, bb.min.z, bb.max.x, bb.max.y, bb.max.z});
    }
}
