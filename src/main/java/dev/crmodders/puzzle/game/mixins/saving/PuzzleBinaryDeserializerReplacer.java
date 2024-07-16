package dev.crmodders.puzzle.game.mixins.saving;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinaryDeserializer;
import dev.crmodders.puzzle.game.serialization.impl.PuppetBinaryDeserializer;
import dev.crmodders.puzzle.game.serialization.impl.PuzzleNBTDeserializer;
import dev.crmodders.puzzle.game.serialization.impl.PuzzleNBTSerializer;
import dev.crmodders.puzzle.utils.MethodUtil;
import dev.dewy.nbt.tags.collection.CompoundTag;
import finalforeach.cosmicreach.io.CosmicReachBinaryDeserializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import finalforeach.cosmicreach.savelib.crbin.CosmicReachBinarySchema;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Overwrite;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

import java.nio.ByteBuffer;
import java.util.Base64;

@Mixin(CosmicReachBinaryDeserializer.class)
public abstract class PuzzleBinaryDeserializerReplacer implements IPuzzleBinaryDeserializer {

    @Shadow protected abstract <T> T newInstance(Class<T> type);

    @Unique
    private static Class<? extends IPuzzleBinaryDeserializer> defaultDeserializer;

    static {
        defaultDeserializer = PuzzleNBTDeserializer.class;
    }

    @Unique
    IPuzzleBinaryDeserializer masterDeserializer;

    @Inject(method = "<init>", at = @At("TAIL"))
    private void init(CallbackInfo ci) {
        masterDeserializer = newInstance(defaultDeserializer);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public static CosmicReachBinaryDeserializer fromBase64(String base64) {
        IPuzzleBinaryDeserializer deserializer = MethodUtil.newInstance(defaultDeserializer);
        ByteBuffer byteBuf = ByteBuffer.wrap(Base64.getDecoder().decode(base64));
        deserializer.prepareForRead(byteBuf);
        return new PuppetBinaryDeserializer(deserializer);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public void readDataFromSchema(CosmicReachBinarySchema schema, ByteBuffer byteBuffer) {
        masterDeserializer.readDataFromSchema(schema, byteBuffer);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public void prepareForRead(ByteBuffer byteBuffer) {
        masterDeserializer.prepareForRead(byteBuffer);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public String[] readStringArray(String name) {
        return masterDeserializer.readStringArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public boolean[] readBooleanArray(String name) {
        return masterDeserializer.readBooleanArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public byte[] readByteArray(String name) {
        return masterDeserializer.readByteArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public short[] readShortArray(String name) {
        return masterDeserializer.readShortArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public int[] readIntArray(String name) {
        return masterDeserializer.readIntArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public long[] readLongArray(String name) {
        return masterDeserializer.readLongArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public float[] readFloatArray(String name) {
        return masterDeserializer.readFloatArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public double[] readDoubleArray(String name) {
        return masterDeserializer.readDoubleArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public CosmicReachBinaryDeserializer[] readRawObjArray(String name) {
        return masterDeserializer.readRawObjArray(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public <T extends ICosmicReachBinarySerializable> Array<T> readObjArray(String name, Class<T> baseType) {
        return masterDeserializer.readObjArray(name, baseType);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public String readString(String name) {
        return masterDeserializer.readString(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public boolean readBoolean(String name, boolean defaultValue) {
        return masterDeserializer.readBoolean(name, defaultValue);
    }

    @Override
    public byte readByte(String name, byte defaultValue) {
        return masterDeserializer.readByte(name, defaultValue);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public int readInt(String name, int defaultValue) {
        return masterDeserializer.readInt(name, defaultValue);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public long readLong(String name, long defaultValue) {
        return masterDeserializer.readLong(name, defaultValue);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public float readFloat(String name, float defaultValue) {
        return masterDeserializer.readFloat(name, defaultValue);
    }

    @Override
    public double readDouble(String name, double defaultValue) {
        return masterDeserializer.readDouble(name, defaultValue);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public Vector2 readVector2(String name) {
        return masterDeserializer.readVector2(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public Vector3 readVector3(String name) {
        return masterDeserializer.readVector3(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public BoundingBox readBoundingBox(String name) {
        return masterDeserializer.readBoundingBox(name);
    }

    /**
     * @author Mr_Zombii
     * @reason Allow Custom Deserializers
     */
    @Overwrite
    public <T extends ICosmicReachBinarySerializable> T readObj(String name, Class<T> baseType) {
        return masterDeserializer.readObj(name, baseType);
    }

}
