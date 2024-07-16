package dev.crmodders.puzzle.game.serialization.impl;

import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.*;
import dev.crmodders.puzzle.game.serialization.api.IPuzzleBinaryDeserializer;
import dev.crmodders.puzzle.utils.ClassUtil;
import finalforeach.cosmicreach.io.ByteArrayUtils;
import finalforeach.cosmicreach.io.CosmicReachBinaryDeserializer;
import finalforeach.cosmicreach.io.ICosmicReachBinarySerializable;
import finalforeach.cosmicreach.savelib.crbin.CosmicReachBinarySchema;
import finalforeach.cosmicreach.savelib.crbin.SchemaType;

import java.nio.ByteBuffer;
import java.util.Base64;
import java.util.Iterator;
import java.util.function.IntFunction;
import java.util.function.ObjIntConsumer;

public class PuzzleCosmicReachBinaryDeserializer implements IPuzzleBinaryDeserializer {

    @Override
    public IPuzzleBinaryDeserializer newInst() {
        return new PuzzleCosmicReachBinaryDeserializer();
    }

    private CosmicReachBinarySchema schema;
    public Array<CosmicReachBinarySchema> altSchemas;
    public Array<String> strings;
    private ObjectIntMap<String> intValues = new ObjectIntMap();
    private ObjectLongMap<String> longValues = new ObjectLongMap();
    private ObjectFloatMap<String> floatValues = new ObjectFloatMap();
    private ObjectMap<String, Double> doubleValues = new ObjectMap();
    private ObjectMap<String, Object> objValues = new ObjectMap();

    public PuzzleCosmicReachBinaryDeserializer() {
    }

    public static CosmicReachBinaryDeserializer fromBase64(String base64) {
        CosmicReachBinaryDeserializer deserial = new CosmicReachBinaryDeserializer();
        ByteBuffer byteBuf = ByteBuffer.wrap(Base64.getDecoder().decode(base64));
        deserial.prepareForRead(byteBuf);
        return deserial;
    }

    private void readSchema(CosmicReachBinarySchema schema, ByteBuffer bytes) {
        boolean validSchema = false;

        while(bytes.hasRemaining()) {
            byte b = bytes.get();
            SchemaType stype = SchemaType.get(b);
            if (stype == SchemaType.SCHEMA_END) {
                validSchema = true;
                break;
            }

            String name = ByteArrayUtils.readString(bytes);
            schema.add(name, stype);
        }

        if (!validSchema) {
            throw new RuntimeException("Invalid schema");
        }
    }

    private CosmicReachBinaryDeserializer readObj(ByteBuffer bytes) {
        int altSchema = ByteArrayUtils.readInt(bytes);
        if (altSchema == -1) {
            return null;
        } else {
            CosmicReachBinarySchema alt = (CosmicReachBinarySchema)this.altSchemas.get(altSchema);
            CosmicReachBinaryDeserializer subDeserial = new CosmicReachBinaryDeserializer();
            subDeserial.altSchemas = this.altSchemas;
            subDeserial.strings = this.strings;
            subDeserial.readDataFromSchema(alt, bytes);
            return subDeserial;
        }
    }

    public void readDataFromSchema(CosmicReachBinarySchema schema, ByteBuffer bytes) {
        Iterator var3 = schema.getSchema().iterator();

        while(true) {
            while(var3.hasNext()) {
                CosmicReachBinarySchema.SchemaItem item = (CosmicReachBinarySchema.SchemaItem)var3.next();
                String name = item.name();
                int i;
                switch (item.type()) {
                    case OBJ:
                        this.objValues.put(name, this.readObj(bytes));
                        break;
                    case OBJ_ARRAY:
                        int length = ByteArrayUtils.readInt(bytes);
                        CosmicReachBinaryDeserializer[] subDeserial = new CosmicReachBinaryDeserializer[length];

                        for(i = 0; i < length; ++i) {
                            subDeserial[i] = this.readObj(bytes);
                        }

                        this.objValues.put(name, subDeserial);
                        break;
                    case BOOLEAN:
                    case BYTE:
                        this.intValues.put(name, ByteArrayUtils.readByte(bytes));
                        break;
                    case DOUBLE:
                        this.doubleValues.put(name, ByteArrayUtils.readDouble(bytes));
                        break;
                    case FLOAT:
                        this.floatValues.put(name, ByteArrayUtils.readFloat(bytes));
                        break;
                    case INT:
                        this.intValues.put(name, ByteArrayUtils.readInt(bytes));
                        break;
                    case LONG:
                        this.longValues.put(name, ByteArrayUtils.readLong(bytes));
                        break;
                    case SHORT:
                        this.intValues.put(name, ByteArrayUtils.readShort(bytes));
                        break;
                    case STRING:
                        i = ByteArrayUtils.readInt(bytes);
                        this.intValues.put(name, i);
                        break;
                    case STRING_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new String[len];
                        }, (arr, ix) -> {
                            arr[ix] = (String)this.strings.get(ByteArrayUtils.readInt(bytes));
                        });
                        break;
                    case BOOLEAN_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new boolean[len];
                        }, (arr, ix) -> {
                            arr[ix] = ByteArrayUtils.readByte(bytes) == 1;
                        });
                        break;
                    case BYTE_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new byte[len];
                        }, (arr, ix) -> {
                            arr[ix] = ByteArrayUtils.readByte(bytes);
                        });
                        break;
                    case DOUBLE_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new double[len];
                        }, (arr, ix) -> {
                            arr[ix] = ByteArrayUtils.readDouble(bytes);
                        });
                        break;
                    case FLOAT_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new float[len];
                        }, (arr, ix) -> {
                            arr[ix] = ByteArrayUtils.readFloat(bytes);
                        });
                        break;
                    case INT_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new int[len];
                        }, (arr, ix) -> {
                            arr[ix] = ByteArrayUtils.readInt(bytes);
                        });
                        break;
                    case LONG_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new long[len];
                        }, (arr, ix) -> {
                            arr[ix] = ByteArrayUtils.readLong(bytes);
                        });
                        break;
                    case SHORT_ARRAY:
                        this.readArray(bytes, name, (len) -> {
                            return new short[len];
                        }, (arr, ix) -> {
                            arr[ix] = ByteArrayUtils.readShort(bytes);
                        });
                }
            }

            return;
        }
    }

    private <T> void readArray(ByteBuffer bytes, String name, IntFunction<T> arrCreator, ObjIntConsumer<T> perElement) {
        int length = ByteArrayUtils.readInt(bytes);
        T arr = arrCreator.apply(length);

        for(int i = 0; i < length; ++i) {
            perElement.accept(arr, i);
        }

        this.objValues.put(name, arr);
    }

    public String[] readStringArray(String name) {
        return (String[])this.objValues.get(name);
    }

    public boolean[] readBooleanArray(String name) {
        return (boolean[])this.objValues.get(name);
    }

    public byte[] readByteArray(String name) {
        return (byte[])this.objValues.get(name);
    }

    public short[] readShortArray(String name) {
        return (short[])this.objValues.get(name);
    }

    public int[] readIntArray(String name) {
        return (int[])this.objValues.get(name);
    }

    public long[] readLongArray(String name) {
        return (long[])this.objValues.get(name);
    }

    public float[] readFloatArray(String name) {
        return (float[])this.objValues.get(name);
    }

    public double[] readDoubleArray(String name) {
        return (double[])this.objValues.get(name);
    }

    public void prepareForRead(ByteBuffer bytes) {
        this.schema = new CosmicReachBinarySchema();
        this.altSchemas = new Array<>(CosmicReachBinarySchema.class);
        int numStrings = ByteArrayUtils.readInt(bytes);
        this.strings = new Array<>(numStrings);

        int numAlt;
        for(numAlt = 0; numAlt < numStrings; ++numAlt) {
            this.strings.add(ByteArrayUtils.readString(bytes));
        }

        this.readSchema(this.schema, bytes);
        numAlt = ByteArrayUtils.readInt(bytes);

        for(int i = 0; i < numAlt; ++i) {
            CosmicReachBinarySchema alt = new CosmicReachBinarySchema();
            this.readSchema(alt, bytes);
            this.altSchemas.add(alt);
        }

        this.readDataFromSchema(this.schema, bytes);
    }

    public int readInt(String name, int defaultValue) {
        return this.intValues.get(name, defaultValue);
    }

    public long readLong(String name, long defaultValue) {
        return this.longValues.get(name, defaultValue);
    }

    public short readShort(String name, short defaultValue) {
        return (short)this.intValues.get(name, defaultValue);
    }

    public float readFloat(String name, float defaultValue) {
        return this.floatValues.get(name, defaultValue);
    }

    @Override
    public double readDouble(String name, double defaultValue) {
        return 0;
    }

    public boolean readBoolean(String name, boolean defaultValue) {
        return this.intValues.get(name, defaultValue ? 1 : 0) == 1;
    }

    @Override
    public byte readByte(String name, byte defaultValue) {
        return 0;
    }

    public String readString(String name) {
        int stringId = this.intValues.get(name, -1);
        if (stringId == -1) {
            return null;
        } else {
            return this.strings.get(stringId);
        }
    }

    private <T extends ICosmicReachBinarySerializable> T readObj(Class<T> elementType, CosmicReachBinaryDeserializer d) {
        T t = ClassUtil.newInstance(elementType);
        t.read(d);
        return t;
    }

    public <T extends ICosmicReachBinarySerializable> T readObj(String name, Class<T> elementType) {
        CosmicReachBinaryDeserializer d = (CosmicReachBinaryDeserializer)this.objValues.get(name);
        return d == null ? null : this.readObj(elementType, d);
    }

    public CosmicReachBinaryDeserializer[] readRawObjArray(String name) {
        CosmicReachBinaryDeserializer[] backing = (CosmicReachBinaryDeserializer[])this.objValues.get(name);
        return backing;
    }

    public <T extends ICosmicReachBinarySerializable> Array<T> readObjArray(String name, Class<T> elementType) {
        CosmicReachBinaryDeserializer[] backing = (CosmicReachBinaryDeserializer[])this.objValues.get(name);
        Array<T> arr = new Array(backing.length);
        CosmicReachBinaryDeserializer[] var5 = backing;
        int var6 = backing.length;

        for(int var7 = 0; var7 < var6; ++var7) {
            CosmicReachBinaryDeserializer d = var5[var7];
            arr.add(d != null ? this.readObj(elementType, d) : null);
        }

        return arr;
    }

    public Vector2 readVector2(String name) {
        float[] f = this.readFloatArray(name);
        if (f.length != 2) {
            throw new RuntimeException("readVector2: Expected 2 floats, but got " + f.length + " instead!");
        } else {
            return new Vector2(f[0], f[1]);
        }
    }

    public Vector3 readVector3(String name) {
        float[] f = this.readFloatArray(name);
        if (f.length != 3) {
            throw new RuntimeException("readVector3: Expected 3 floats, but got " + f.length + " instead!");
        } else {
            return new Vector3(f);
        }
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
