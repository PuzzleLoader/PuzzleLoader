package dev.crmodders.puzzle.game.mixins.refactors.saving;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinarySerializer;
import dev.crmodders.puzzle.game.serialization.impl.PuzzleNBTSerializer;
import dev.crmodders.puzzle.utils.ClassUtil;
import finalforeach.cosmicreach.io.CosmicReachBinarySerializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(CosmicReachBinarySerializer.class)
public abstract class PuzzleBinarySerializerReplacer implements IPuzzleBinarySerializer {

    @Unique
    private static Class<? extends IPuzzleBinarySerializer> defaultSerializer;

    static {
        defaultSerializer = PuzzleNBTSerializer.class;
    }

    @Unique
    IPuzzleBinarySerializer masterSerializer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        masterSerializer = ClassUtil.newInstance(defaultSerializer);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeStringArray(String name, String[] array) {
        masterSerializer.writeStringArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeBooleanArray(String name, boolean[] array) {
        masterSerializer.writeBooleanArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeByteArray(String name, byte[] array) {
        masterSerializer.writeByteArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeShortArray(String name, short[] array) {
        masterSerializer.writeShortArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeIntArray(String name, int[] array) {
        masterSerializer.writeIntArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeLongArray(String name, long[] array) {
        masterSerializer.writeLongArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeFloatArray(String name, float[] array) {
        masterSerializer.writeFloatArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeDoubleArray(String name, double[] array) {
        masterSerializer.writeDoubleArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, Array<T> array) {
        masterSerializer.writeObjArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public <T extends ICosmicReachBinarySerializable> void writeObjArray(String name, T[] array) {
        masterSerializer.writeObjArray(name, array);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeString(String name, String value) {
        masterSerializer.writeString(name, value);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeBoolean(String name, boolean bool) {
        masterSerializer.writeBoolean(name, bool);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeByte(String name, byte i) {
        masterSerializer.writeByte(name, i);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeInt(String name, int i) {
        masterSerializer.writeInt(name, i);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeLong(String name, long l) {
        masterSerializer.writeLong(name, l);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeFloat(String name, float f) {
        masterSerializer.writeFloat(name, f);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeDouble(String name, double d) {
        masterSerializer.writeDouble(name, d);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeVector2(String name, Vector2 vector) {
        masterSerializer.writeVector2(name, vector);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeVector3(String name, Vector3 vector) {
        masterSerializer.writeVector3(name, vector);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public void writeBoundingBox(String name, BoundingBox bb) {
        masterSerializer.writeBoundingBox(name, bb);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public <T extends ICosmicReachBinarySerializable> void writeObj(String name, T item) {
        masterSerializer.writeObj(name, item);
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public byte[] toBytes() {
        return masterSerializer.toBytes();
    }

    /**
     * @author Mr_Zombii
     * @reason Use NBT
     */
    @Overwrite
    public String toBase64() {
        return masterSerializer.toBase64();
    }

}
