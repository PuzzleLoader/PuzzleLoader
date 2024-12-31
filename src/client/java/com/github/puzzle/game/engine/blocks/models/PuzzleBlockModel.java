package com.github.puzzle.game.engine.blocks.models;

import com.badlogic.gdx.graphics.Color;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.*;
import com.github.puzzle.game.block.generators.model.BlockModelGenerator;
import com.github.puzzle.game.engine.ClientGameLoader;
import com.github.puzzle.game.engine.blocks.model.IPuzzleBlockModel;
import finalforeach.cosmicreach.ClientSingletons;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.rendering.IMeshData;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonTexture;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;
import finalforeach.cosmicreach.util.Identifier;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * @see finalforeach.cosmicreach.blockentities.BlockEntity
 *
 */
public class PuzzleBlockModel extends BlockModel implements IPuzzleBlockModel {

    public static PuzzleBlockModel fromJson(String modelJson, String modelName, int rotXZ) {
        Json json = new Json();
        PuzzleBlockModel model = json.fromJson(PuzzleBlockModel.class, modelJson);
        model.modelName = modelName;
        model.rotXZ = rotXZ;
        return model;
    }

    public static boolean useIndices;
    public transient String modelName;
    public transient int rotXZ;
    public transient PuzzleBlockModelCuboid.Face[] allFaces;
    public transient Boolean canGreedyCombine;
    public transient boolean initialized = false;

    public String parent;
    public OrderedMap<String, BlockModelJsonTexture> textures;
    public PuzzleBlockModelCuboid[] cuboids;

    public PuzzleBlockModel() {
    }

    private static boolean endsWithOnce(String string, String endsWith) {
        return string.indexOf(endsWith) == string.length() - endsWith.length();
    }

    public OrderedMap<String, BlockModelJsonTexture> getTextures() {
        return this.textures;
    }

    public BlockModelJsonTexture getTexture(String texName) {
        BlockModelJsonTexture t = textures.get(texName);
        if (t != null) {
            return t;
        }
        switch (texName) {
            case "slab_top" -> {
                return getTexture("top");
            }
            case "slab_bottom" -> {
                return this.getTexture("bottom");
            }
            case "slab_side" -> {
                return getTexture("side");
            }
        }
        t = textures.get("all");
        if(t != null) {
            return t;
        }
        return textures.values().iterator().next();
    }
    
    public void initialize() {

        PuzzleBlockModel parent = this.parent == null ? null : (PuzzleBlockModel) GameSingletons.blockModelInstantiator.getInstance(this.parent, this.rotXZ);

        if (parent != null) {

            if(!parent.initialized) {
                parent.initialize();
                ClientGameLoader.LOGGER.warn("parent '{}' was not initialized, initializing now", this.parent);
            }

            Json json = new Json();
            json.setTypeName(null);

            if (this.cuboids == null && parent.cuboids != null) {
                this.cuboids = json.fromJson(parent.cuboids.getClass(), json.toJson(parent.cuboids));
            }

            if (this.textures == null && parent.textures != null) {
                this.textures = json.fromJson(parent.textures.getClass(), json.toJson(parent.textures));
            }

        }

        if (this.textures != null) {
            for (final BlockModelJsonTexture t : this.textures.values()) {
                if (t.fileName != null) {
                    t.uv = ChunkShader.addToAllBlocksTexture(t);
                }
            }
        }

        if(cuboids != null && textures != null) {
            List<PuzzleBlockModelCuboid.Face> faces = new ArrayList<>();
            for (PuzzleBlockModelCuboid c : this.cuboids) {

                final float boundsX1 = c.localBounds[0];
                final float boundsZ1 = c.localBounds[2];
                final float boundsX2 = c.localBounds[3];
                final float boundsZ2 = c.localBounds[5];

                PuzzleBlockModelCuboid.Face tmpNegX = c.faces.get("localNegX");
                PuzzleBlockModelCuboid.Face tmpPosX = c.faces.get("localPosX");
                PuzzleBlockModelCuboid.Face tmpNegY = c.faces.get("localNegY");
                PuzzleBlockModelCuboid.Face tmpPosY = c.faces.get("localPosY");
                PuzzleBlockModelCuboid.Face tmpNegZ = c.faces.get("localNegZ");
                PuzzleBlockModelCuboid.Face tmpPosZ = c.faces.get("localPosZ");

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
                            final float tmpU = tmpPosZ.uv[0];
                            tmpNegZ.uv[0] = tmpNegZ.uv[2];
                            tmpNegZ.uv[2] = tmpU;
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
                            final float tmpU2 = tmpPosZ.uv[0];
                            tmpNegZ.uv[0] = tmpNegZ.uv[2];
                            tmpNegZ.uv[2] = tmpU2;
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
            for (final PuzzleBlockModelCuboid c : this.cuboids) {
                if (this.boundingBox.max.epsilonEquals(this.boundingBox.min)) {
                    this.boundingBox = c.getBoundingBox();
                }
                else {
                    this.boundingBox.ext(c.getBoundingBox());
                }
            }

            allFaces = faces.toArray(PuzzleBlockModelCuboid.Face[]::new);
        } else {
            allFaces = new PuzzleBlockModelCuboid.Face[0];
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
    public void addVertices(final IMeshData meshData, int bx, int by, int bz, int opaqueBitmask, final short[] blockLightLevels, final int[] skyLightLevels) {
        meshData.ensureVerticesCapacity(6 * allFaces.length * 7);

        IntArray indices = meshData.getIndices();

        for (final PuzzleBlockModelCuboid.Face f : allFaces) {
            if((opaqueBitmask & f.cullingMask) != 0) continue;

            int aoIdA = 3;
            int aoIdB = 3;
            int aoIdC = 3;
            int aoIdD = 3;

            if (f.ambientocclusion) {
                aoIdA = (((opaqueBitmask & f.aoBitmaskA1) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskA2) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskA3) == 0) ? 1 : 0);
                aoIdB = (((opaqueBitmask & f.aoBitmaskB1) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskB2) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskB3) == 0) ? 1 : 0);
                aoIdC = (((opaqueBitmask & f.aoBitmaskC1) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskC2) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskC3) == 0) ? 1 : 0);
                aoIdD = (((opaqueBitmask & f.aoBitmaskD1) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskD2) == 0) ? 1 : 0) + (((opaqueBitmask & f.aoBitmaskD3) == 0) ? 1 : 0);
            }

//            final float x1 = bx + f.x1;
//            final float y1 = by + f.y1;
//            final float z1 = bz + f.z1;
//            final float x2 = bx + f.x2;
//            final float y2 = by + f.y2;
//            final float z2 = bz + f.z2;
//
//            final float midX1 = bx + f.midX1;
//            final float midY1 = by + f.midY1;
//            final float midZ1 = bz + f.midZ1;
//            final float midX2 = bx + f.midX2;
//            final float midY2 = by + f.midY2;
//            final float midZ2 = bz + f.midZ2;

            final int viA = f.vertexIndexA;
            final int viB = f.vertexIndexB;
            final int viC = f.vertexIndexC;
            final int viD = f.vertexIndexD;

            bx -= 64 * Math.floorDiv(bx, 64);
            by -= 64 * Math.floorDiv(by, 64);
            bz -= 64 * Math.floorDiv(bz, 64);
            bx &= 63;
            by &= 63;
            bz &= 63;

            int packedPos = bx << 12 | by << 6 | bz;
            final int i1 = addVert(meshData, packedPos, f.uA, f.vA, aoIdA, blockLightLevels[viA], skyLightLevels[viA], f.modelUvIdxA);
            final int i2 = addVert(meshData, packedPos, f.uB, f.vB, aoIdB, blockLightLevels[viB], skyLightLevels[viB], f.modelUvIdxB);
            final int i3 = addVert(meshData, packedPos, f.uC, f.vC, aoIdC, blockLightLevels[viC], skyLightLevels[viC], f.modelUvIdxC);
            final int i4 = addVert(meshData, packedPos, f.uD, f.vD, aoIdD, blockLightLevels[viD], skyLightLevels[viD], f.modelUvIdxD);

            if (PuzzleBlockModel.useIndices) {
                indices.add(i1);
                indices.add(i2);
                indices.add(i3);
                indices.add(i3);
                indices.add(i4);
                indices.add(i1);
            }

        }
    }

    public int addVert(final IMeshData meshData, int packedPos, final float u, final float v, final int aoId, final short blockLight, int skyLight, final int uvIdx) {
        FloatArray vertices = meshData.getVertices();
        final int currentVertexIndex = vertices.size / 3;

        final float subAO = aoId / 4.0f + 0.25f;
        int r = (int) (17f * ((blockLight & 0xF00) >> 8) * subAO);
        int g = (int) (17f * ((blockLight & 0xF0) >> 4) * subAO);
        int b = (int) (17f * (blockLight & 0xF) * subAO);
        skyLight *= (int)(subAO * 17f);

        vertices.items[vertices.size + 1] = Color.toFloatBits(r, g, b, skyLight);
        if (ClientSingletons.shaderNUM32_IS_FLOAT) {
            vertices.items[vertices.size] = (float)packedPos;
            vertices.items[vertices.size + 2] = (float)uvIdx;
        } else {
            vertices.items[vertices.size] = Float.intBitsToFloat(packedPos);
            vertices.items[vertices.size + 2] = Float.intBitsToFloat(uvIdx);
        }
        vertices.size += 3;

        return currentVertexIndex;
    }
    
    @Override
    public boolean isGreedyCube() {
        if (!isEmpty() && this.cuboids.length == 1) {
            final PuzzleBlockModelCuboid c = this.cuboids[0];
            if (c.faces.size == 6 && c.localBounds[0] == 0.0f && c.localBounds[1] == 0.0f && c.localBounds[2] == 0.0f && c.localBounds[3] == 16.0f && c.localBounds[4] == 16.0f && c.localBounds[5] == 16.0f) {
                for (final PuzzleBlockModelCuboid.Face f : c.faces.values()) {
                    boolean expectedUVs = true;
                    expectedUVs &= (f.uv[0] == 0.0f);
                    expectedUVs &= (f.uv[1] == 0.0f);
                    expectedUVs &= (f.uv[2] == 16.0f);
                    expectedUVs &= (f.uv[3] == 16.0f);
                    if (!expectedUVs) {
                        return false;
                    }
                }
                return true;
            }
        }
        return false;
    }
    
    @Override
    public boolean canGreedyCombine() {
        return Objects.requireNonNullElseGet(this.canGreedyCombine, this::isGreedyCube);
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
        for (final PuzzleBlockModelCuboid c : this.cuboids) {
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
        for(String vanillaTextureName : vanillaTextures.keySet()) {
            BlockModelJsonTexture texture = new BlockModelJsonTexture();
            texture.fileName = vanillaTextures.get(vanillaTextureName).toString();
            this.textures.put(vanillaTextureName, texture);
        }
    }

    @Override
    public void fromModelGenerator(BlockModelGenerator gen) {
        this.cuboids = new PuzzleBlockModelCuboid[gen.cuboids.size()];
        for(int i = 0; i < gen.cuboids.size(); i++) {
            BlockModelGenerator.Cuboid cuboid = gen.cuboids.get(i);
            PuzzleBlockModelCuboid cuboid1 = new PuzzleBlockModelCuboid();
            cuboid1.localBounds = new float[] { cuboid.x1, cuboid.y1, cuboid.z1, cuboid.x2, cuboid.y2, cuboid.z2 };
            cuboid1.faces = new OrderedMap<>();
            for(BlockModelGenerator.Cuboid.Face face : cuboid.faces) {
                PuzzleBlockModelCuboid.Face face1 = new PuzzleBlockModelCuboid.Face();
                face1.texture = face.texture;
                face1.uv = new float[] { face.u1, face.v1, face.u2, face.v2 };
                face1.uvRotation = face.uvRotation;
                face1.cullFace = face.cullFace;
                face1.ambientocclusion = face.ambientOcclusion;
                cuboid1.faces.put(face.id, face1);
            }
            this.cuboids[i] = cuboid1;
        }
    }

    @Override
    public void initTextures() {
        this.textures = new OrderedMap<>();
    }

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