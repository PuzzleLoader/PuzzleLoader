package com.github.puzzle.game.engine.blocks.models;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.Array;
import com.badlogic.gdx.utils.Json;
import com.badlogic.gdx.utils.OrderedMap;
import com.github.puzzle.game.block.generators.model.BlockModelGenerator;
import com.github.puzzle.game.engine.ServerGameLoader;
import com.github.puzzle.game.engine.blocks.model.IPuzzleBlockModel;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.RuntimeInfo;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @see finalforeach.cosmicreach.blockentities.BlockEntity
 *
 */
public class ServerPuzzleBlockModel extends BlockModel implements IPuzzleBlockModel {

    public static IPuzzleBlockModel fromJson(String modelJson, String modelName, int rotXZ) {
        Json json = new Json();
        ServerPuzzleBlockModel model = json.fromJson(ServerPuzzleBlockModel.class, modelJson);
        model.modelName = modelName;
        model.rotXZ = rotXZ;
        return model;
    }

    public static final boolean useIndices = !RuntimeInfo.useSharedIndices;
    public transient String modelName;
    public transient int rotXZ;
    public OrderedMap<String, String> textures;
    public transient DummyPuzzleBlockModelCuboid.Face[] allFaces;
    public transient Boolean canGreedyCombine;
    public transient boolean initialized = false;

    public String parent;
    public DummyPuzzleBlockModelCuboid[] cuboids;

    public ServerPuzzleBlockModel() {
    }

    private static boolean endsWithOnce(String string, String endsWith) {
        return string.indexOf(endsWith) == string.length() - endsWith.length();
    }

    public void initialize() {

        ServerPuzzleBlockModel parent = this.parent == null ? null : (ServerPuzzleBlockModel) GameSingletons.blockModelInstantiator.getInstance(this.parent, this.rotXZ);

        if (parent != null) {

            if(!parent.initialized) {
                parent.initialize();
                ServerGameLoader.LOGGER.warn("parent '{}' was not initialized, initializing now", this.parent);
            }

            Json json = new Json();
            json.setTypeName(null);

            if (this.cuboids == null && parent.cuboids != null) {
                this.cuboids = json.fromJson(parent.cuboids.getClass(), json.toJson(parent.cuboids));
            }

        }

        if(cuboids != null) {
            List<DummyPuzzleBlockModelCuboid.Face> faces = new ArrayList<>();
            for (DummyPuzzleBlockModelCuboid c : this.cuboids) {

                final float boundsX1 = c.localBounds[0];
                final float boundsZ1 = c.localBounds[2];
                final float boundsX2 = c.localBounds[3];
                final float boundsZ2 = c.localBounds[5];

                DummyPuzzleBlockModelCuboid.Face tmpNegX = c.faces.get("localNegX");
                DummyPuzzleBlockModelCuboid.Face tmpPosX = c.faces.get("localPosX");
                DummyPuzzleBlockModelCuboid.Face tmpNegY = c.faces.get("localNegY");
                DummyPuzzleBlockModelCuboid.Face tmpPosY = c.faces.get("localPosY");
                DummyPuzzleBlockModelCuboid.Face tmpNegZ = c.faces.get("localNegZ");
                DummyPuzzleBlockModelCuboid.Face tmpPosZ = c.faces.get("localPosZ");

                // rotate model
                switch (rotXZ) {
                    case 90:{
                        c.localBounds[0] = boundsZ1;
                        c.localBounds[2] = boundsX1;
                        c.localBounds[3] = boundsZ2;
                        c.localBounds[5] = boundsX2;
                        c.faces.clear();
                        if (tmpPosX != null) {
                            c.faces.put("localPosZ", tmpPosX);
                        }
                        if (tmpNegX != null) {
                            c.faces.put("localNegZ", tmpNegX);
                        }
                        if (tmpNegY != null) {
                            tmpNegY.uvRotation = (tmpNegY.uvRotation - 90 + 360) % 360;
                            c.faces.put("localNegY", tmpNegY);
                        }
                        if (tmpPosY != null) {
                            tmpPosY.uvRotation = (tmpPosY.uvRotation + 90 + 360) % 360;
                            c.faces.put("localPosY", tmpPosY);
                        }
                        if (tmpNegZ != null) {
                            c.faces.put("localPosX", tmpNegZ);
                        }
                        if (tmpPosZ != null) {
                            c.faces.put("localNegX", tmpPosZ);
                            break;
                        }
                        break;
                }
                    case 180: {
                        final float fxa = 16.0f - boundsX1;
                        final float fxb = 16.0f - boundsX2;
                        final float fza = 16.0f - boundsZ1;
                        final float fzb = 16.0f - boundsZ2;
                        c.localBounds[0] = Math.min(fxa, fxb);
                        c.localBounds[2] = Math.min(fza, fzb);
                        c.localBounds[3] = Math.max(fxa, fxb);
                        c.localBounds[5] = Math.max(fza, fzb);
                        c.faces.clear();
                        if (tmpNegX != null) {
                            c.faces.put("localPosX", tmpNegX);
                        }
                        if (tmpPosX != null) {
                            c.faces.put("localNegX", tmpPosX);
                        }
                        if (tmpNegY != null) {
                            c.faces.put("localNegY", tmpNegY);
                        }
                        if (tmpPosY != null) {
                            c.faces.put("localPosY", tmpPosY);
                        }
                        if (tmpPosZ != null) {
                            c.faces.put("localNegZ", tmpPosZ);
                        }
                        if (tmpNegZ != null) {
                            c.faces.put("localPosZ", tmpNegZ);
                            break;
                        }
                        break;
                    }
                    case 270: {
                        final float fxa = 16.0f - boundsX1;
                        final float fxb = 16.0f - boundsX2;
                        final float fza = 16.0f - boundsZ1;
                        final float fzb = 16.0f - boundsZ2;
                        c.localBounds[0] = Math.min(fza, fzb);
                        c.localBounds[2] = Math.min(fxa, fxb);
                        c.localBounds[3] = Math.max(fza, fzb);
                        c.localBounds[5] = Math.max(fxa, fxb);
                        c.faces.clear();
                        if (tmpNegX != null) {
                            c.faces.put("localPosZ", tmpNegX);
                        }
                        if (tmpPosX != null) {
                            c.faces.put("localNegZ", tmpPosX);
                        }
                        if (tmpNegY != null) {
                            tmpNegY.uvRotation = (tmpNegY.uvRotation - 90 + 360) % 360;
                            c.faces.put("localNegY", tmpNegY);
                        }
                        if (tmpPosY != null) {
                            tmpPosY.uvRotation = (tmpPosY.uvRotation + 90 + 360) % 360;
                            c.faces.put("localPosY", tmpPosY);
                        }
                        if (tmpPosZ != null) {
                            c.faces.put("localPosX", tmpPosZ);
                        }
                        if (tmpNegZ != null) {
                            c.faces.put("localNegX", tmpNegZ);
                            break;
                        }
                        break;
                    }
                }

                // init model
                c.initialize(this, faces);
                this.isNegXFaceOccluding |= c.isNegXFaceOccluding;
                this.isPosXFaceOccluding |= c.isPosXFaceOccluding;
                this.isNegYFaceOccluding |= c.isNegYFaceOccluding;
                this.isPosYFaceOccluding |= c.isPosYFaceOccluding;
                this.isNegZFaceOccluding |= c.isNegZFaceOccluding;
                this.isPosZFaceOccluding |= c.isPosZFaceOccluding;
                this.isNegXFacePartOccluding |= c.isNegXFacePartOccluding;
                this.isPosXFacePartOccluding |= c.isPosXFacePartOccluding;
                this.isNegYFacePartOccluding |= c.isNegYFacePartOccluding;
                this.isPosYFacePartOccluding |= c.isPosYFacePartOccluding;
                this.isNegZFacePartOccluding |= c.isNegZFacePartOccluding;
                this.isPosZFacePartOccluding |= c.isPosZFacePartOccluding;
            }

            // cache bounding boxes
            for (final DummyPuzzleBlockModelCuboid c : this.cuboids) {
                if (this.boundingBox.max.epsilonEquals(this.boundingBox.min)) {
                    this.boundingBox = c.getBoundingBox();
                }
                else {
                    this.boundingBox.ext(c.getBoundingBox());
                }
            }

            allFaces = faces.toArray(DummyPuzzleBlockModelCuboid.Face[]::new);
        } else {
            allFaces = new DummyPuzzleBlockModelCuboid.Face[0];
        }

        if (cuboids == null || cuboids.length == 0 || boundingBox.max.epsilonEquals(boundingBox.min)) {
            boundingBox.min.set(0.0F, 0.0F, 0.0F);
            boundingBox.max.set(1.0F, 1.0F, 1.0F);
        }

        boundingBox.update();
        initialized = true;

        if(allFaces == null) {
            throw new IllegalStateException("allFaces == null for " + modelName);
        }

    }

    @Override
    public void addVertices(final IMeshData meshData, int bx, int by, int bz,  int opaqueBitmask, final short[] blockLightLevels, final int[] skyLightLevels) {}

    @Override
    public boolean isGreedyCube() {
        return false;
    }

    @Override
    public boolean canGreedyCombine() {
        return false;
    }
    
    @Override
    public boolean isEmpty() {
        return this.cuboids == null || this.cuboids.length == 0;
    }
    
    @Override
    public void getAllBoundingBoxes(final Array<BoundingBox> boundingBoxes, final int bx, final int by, final int bz) {
        boundingBoxes.clear();
        if (this.cuboids == null) {
            return;
        }
        for (final DummyPuzzleBlockModelCuboid c : this.cuboids) {
            final BoundingBox bb = new BoundingBox();
            bb.min.set(c.localBounds[0] / 16.0f, c.localBounds[1] / 16.0f, c.localBounds[2] / 16.0f);
            bb.max.set(c.localBounds[3] / 16.0f, c.localBounds[4] / 16.0f, c.localBounds[5] / 16.0f);
            bb.min.add((float)bx, (float)by, (float)bz);
            bb.max.add((float)bx, (float)by, (float)bz);
            bb.update();
            boundingBoxes.add(bb);
        }
    }

    public void registerVanillaTextures(Map<String, Identifier> vanillaTextures) {
    }

    @Override
    public void fromModelGenerator(BlockModelGenerator gen) {
        this.cuboids = new DummyPuzzleBlockModelCuboid[gen.cuboids.size()];
        for(int i = 0; i < gen.cuboids.size(); i++) {
            BlockModelGenerator.Cuboid cuboid = gen.cuboids.get(i);
            DummyPuzzleBlockModelCuboid cuboid1 = new DummyPuzzleBlockModelCuboid();
            cuboid1.localBounds = new float[] { cuboid.x1, cuboid.y1, cuboid.z1, cuboid.x2, cuboid.y2, cuboid.z2 };
            cuboid1.faces = new OrderedMap<>();
            for(BlockModelGenerator.Cuboid.Face face : cuboid.faces) {
                DummyPuzzleBlockModelCuboid.Face face1 = new DummyPuzzleBlockModelCuboid.Face();
                face1.texture = face.texture;
                face1.uvRotation = face.uvRotation;
                face1.cullFace = face.cullFace;
                face1.ambientocclusion = face.ambientOcclusion;
                cuboid1.faces.put(face.id, face1);
            }
            this.cuboids[i] = cuboid1;
        }
    }

    @Override
    public void initTextures() {}

    @Override
    public String getParent() {
        return parent;
    }

    @Override
    public String getModelName() {
        return modelName;
    }

    @Override
    public int getXZRotation() {
        return rotXZ;
    }
}