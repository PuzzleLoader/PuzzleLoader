package dev.crmodders.puzzle.game.engine.blocks.models;

import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.ObjectMap;
import com.badlogic.gdx.utils.OrderedMap;
import dev.crmodders.puzzle.game.engine.GameLoader;
import dev.crmodders.puzzle.game.engine.blocks.CustomTextureLoader;
import finalforeach.cosmicreach.constants.AdjacentBitmask;
import finalforeach.cosmicreach.constants.DiagonalBitmask;
import finalforeach.cosmicreach.constants.VertexIndex;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJsonTexture;
import finalforeach.cosmicreach.rendering.shaders.ChunkShader;

import java.util.List;

public class PuzzleBlockModelCuboid
{

    public static class Face
    {
        public float[] uv;
        public boolean ambientocclusion;
        public boolean cullFace;
        public String texture;
        public  int uvRotation;
        public transient int cullingMask;
        public  transient float x1;
        public  transient float y1;
        public  transient float z1;
        public  transient float x2;
        public  transient float y2;
        public  transient float z2;
        public  transient float midX1;
        public  transient float midY1;
        public  transient float midZ1;
        public  transient float midX2;
        public  transient float midY2;
        public  transient float midZ2;
        public  transient float uA;
        public  transient float vA;
        public  transient float uB;
        public  transient float vB;
        public  transient float uC;
        public  transient float vC;
        public  transient float uD;
        public  transient float vD;
        public  transient int modelUvIdxA;
        public  transient int modelUvIdxB;
        public  transient int modelUvIdxC;
        public  transient int modelUvIdxD;
        public  transient int vertexIndexA;
        public  transient int vertexIndexB;
        public  transient int vertexIndexC;
        public  transient int vertexIndexD;
        public  transient int aoBitmaskA1;
        public  transient int aoBitmaskA2;
        public  transient int aoBitmaskA3;
        public  transient int aoBitmaskB1;
        public  transient int aoBitmaskB2;
        public  transient int aoBitmaskB3;
        public  transient int aoBitmaskC1;
        public  transient int aoBitmaskC2;
        public  transient int aoBitmaskC3;
        public  transient int aoBitmaskD1;
        public  transient int aoBitmaskD2;
        public  transient int aoBitmaskD3;
    }

    public transient boolean isPosXFaceOccluding;
    public transient boolean isNegXFaceOccluding;
    public transient boolean isPosYFaceOccluding;
    public transient boolean isNegYFaceOccluding;
    public transient boolean isPosZFaceOccluding;
    public transient boolean isNegZFaceOccluding;
    public transient boolean isPosXFacePartOccluding;
    public transient boolean isNegXFacePartOccluding;
    public transient boolean isPosYFacePartOccluding;
    public transient boolean isNegYFacePartOccluding;
    public transient boolean isPosZFacePartOccluding;
    public transient boolean isNegZFacePartOccluding;

    public float[] localBounds;
    public OrderedMap<String, Face> faces;

    public BoundingBox getBoundingBox() {
        BoundingBox bb = new BoundingBox();
        bb.min.set(this.localBounds[0] / 16.0f, this.localBounds[1] / 16.0f, this.localBounds[2] / 16.0f);
        bb.max.set(this.localBounds[3] / 16.0f, this.localBounds[4] / 16.0f, this.localBounds[5] / 16.0f);
        bb.update();
        return bb;
    }

    public void initialize(PuzzleBlockModel model, List<Face> allFaces) {
        for (final ObjectMap.Entry<String, Face> kf : this.faces) {

            boolean isValidFace = true;
            Face f = kf.value;
            if(f == null) {
                GameLoader.LOGGER.warn("face '{}' of '{}@{}' is null, skipping", kf.key, model.modelName, model.rotXZ);
                continue;
            }

            // normalize bounds
            float x1 = this.localBounds[0] / 16.0f;
            float y1 = this.localBounds[1] / 16.0f;
            float z1 = this.localBounds[2] / 16.0f;
            float x2 = this.localBounds[3] / 16.0f;
            float y2 = this.localBounds[4] / 16.0f;
            float z2 = this.localBounds[5] / 16.0f;

            // find min,max of bounds
            float minX = Math.min(x1, x2);
            float minY = Math.min(y1, y2);
            float minZ = Math.min(z1, z2);
            float maxX = Math.max(x1, x2);
            float maxY = Math.max(y1, y2);
            float maxZ = Math.max(z1, z2);

            float uvScale = (float)(ChunkShader.allBlocksTexSize / 16);

            // determine culling flags
            if (f.cullFace) {
                f.cullingMask = switch (kf.key) {
                    case "localNegX" -> AdjacentBitmask.NEG_X;
                    case "localPosX" -> AdjacentBitmask.POS_X;
                    case "localNegY" -> AdjacentBitmask.NEG_Y;
                    case "localPosY" -> AdjacentBitmask.POS_Y;
                    case "localNegZ" -> AdjacentBitmask.NEG_Z;
                    case "localPosZ" -> AdjacentBitmask.POS_Z;
                    default -> throw new IllegalArgumentException("faceDirection is not valid: " + kf.key);
                };
            }

            // keep this
            BlockModelJsonTexture t = model.getTexture(f.texture);
            switch (kf.key) {
                case "localNegX":
                    isNegXFaceOccluding |= minX == 0.0F && minY <= 0.0F && maxY >= 1.0F && minZ <= 0.0F && maxZ >= 1.0F;
                    isNegXFacePartOccluding |= minX == 0.0F;
                    f.vertexIndexA = VertexIndex.NX_NY_NZ;
                    f.aoBitmaskA1 = DiagonalBitmask.NEG_X_NEG_Y_NEG_Z;
                    f.aoBitmaskA2 = DiagonalBitmask.NEG_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskA3 = DiagonalBitmask.NEG_X_ZRO_Y_NEG_Z;
                    f.vertexIndexB = VertexIndex.NX_NY_PZ;
                    f.aoBitmaskB1 = DiagonalBitmask.NEG_X_NEG_Y_POS_Z;
                    f.aoBitmaskB2 = DiagonalBitmask.NEG_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskB3 = DiagonalBitmask.NEG_X_ZRO_Y_POS_Z;
                    f.vertexIndexC = VertexIndex.NX_PY_PZ;
                    f.aoBitmaskC1 = DiagonalBitmask.NEG_X_POS_Y_POS_Z;
                    f.aoBitmaskC2 = DiagonalBitmask.NEG_X_POS_Y_ZRO_Z;
                    f.aoBitmaskC3 = DiagonalBitmask.NEG_X_ZRO_Y_POS_Z;
                    f.vertexIndexD = VertexIndex.NX_PY_NZ;
                    f.aoBitmaskD1 = DiagonalBitmask.NEG_X_POS_Y_NEG_Z;
                    f.aoBitmaskD2 = DiagonalBitmask.NEG_X_POS_Y_ZRO_Z;
                    f.aoBitmaskD3 = DiagonalBitmask.NEG_X_ZRO_Y_NEG_Z;
                    f.x1 = x1;
                    f.y1 = y1;
                    f.z1 = z1;
                    f.x2 = x1;
                    f.y2 = y2;
                    f.z2 = z2;
                    f.midX1 = f.x1;
                    f.midY1 = f.y1;
                    f.midZ1 = f.z2;
                    f.midX2 = f.x1;
                    f.midY2 = f.y2;
                    f.midZ2 = f.z1;
                    f.uA = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vA = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uB = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vB = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uC = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vC = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uD = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vD = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    break;
                case "localPosX":
                    isPosXFaceOccluding |= maxX == 1.0F && minY <= 0.0F && maxY >= 1.0F && minZ <= 0.0F && maxZ >= 1.0F;
                    isPosXFacePartOccluding |= maxX == 1.0F;
                    f.vertexIndexA = VertexIndex.PX_NY_NZ;
                    f.aoBitmaskA1 = DiagonalBitmask.POS_X_NEG_Y_NEG_Z;
                    f.aoBitmaskA2 = DiagonalBitmask.POS_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskA3 = DiagonalBitmask.POS_X_ZRO_Y_NEG_Z;
                    f.vertexIndexB = VertexIndex.PX_PY_NZ;
                    f.aoBitmaskB1 = DiagonalBitmask.POS_X_POS_Y_NEG_Z;
                    f.aoBitmaskB2 = DiagonalBitmask.POS_X_POS_Y_ZRO_Z;
                    f.aoBitmaskB3 = DiagonalBitmask.POS_X_ZRO_Y_NEG_Z;
                    f.vertexIndexC = VertexIndex.PX_PY_PZ;
                    f.aoBitmaskC1 = DiagonalBitmask.POS_X_POS_Y_POS_Z;
                    f.aoBitmaskC2 = DiagonalBitmask.POS_X_POS_Y_ZRO_Z;
                    f.aoBitmaskC3 = DiagonalBitmask.POS_X_ZRO_Y_POS_Z;
                    f.vertexIndexD = VertexIndex.PX_NY_PZ;
                    f.aoBitmaskD1 = DiagonalBitmask.POS_X_NEG_Y_POS_Z;
                    f.aoBitmaskD2 = DiagonalBitmask.POS_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskD3 = DiagonalBitmask.POS_X_ZRO_Y_POS_Z;
                    f.x1 = x2;
                    f.y1 = y1;
                    f.z1 = z1;
                    f.x2 = x2;
                    f.y2 = y2;
                    f.z2 = z2;
                    f.midX1 = f.x1;
                    f.midY1 = f.y2;
                    f.midZ1 = f.z1;
                    f.midX2 = f.x1;
                    f.midY2 = f.y1;
                    f.midZ2 = f.z2;
                    f.uA = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vA = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uB = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vB = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uC = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vC = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uD = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vD = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    break;
                case "localNegY":
                    isNegYFaceOccluding |= minX <= 0.0F && maxX >= 1.0F && minY == 0.0F && minZ <= 0.0F && maxZ >= 1.0F;
                    isNegYFacePartOccluding |= minY == 0.0F;
                    f.vertexIndexA = VertexIndex.NX_NY_NZ;
                    f.aoBitmaskA1 = DiagonalBitmask.NEG_X_NEG_Y_NEG_Z;
                    f.aoBitmaskA2 = DiagonalBitmask.NEG_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskA3 = DiagonalBitmask.ZRO_X_NEG_Y_NEG_Z;
                    f.vertexIndexB = VertexIndex.PX_NY_NZ;
                    f.aoBitmaskB1 = DiagonalBitmask.POS_X_NEG_Y_NEG_Z;
                    f.aoBitmaskB2 = DiagonalBitmask.POS_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskB3 = DiagonalBitmask.ZRO_X_NEG_Y_NEG_Z;
                    f.vertexIndexC = VertexIndex.PX_NY_PZ;
                    f.aoBitmaskC1 = DiagonalBitmask.POS_X_NEG_Y_POS_Z;
                    f.aoBitmaskC2 = DiagonalBitmask.POS_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskC3 = DiagonalBitmask.ZRO_X_NEG_Y_POS_Z;
                    f.vertexIndexD = VertexIndex.NX_NY_PZ;
                    f.aoBitmaskD1 = DiagonalBitmask.NEG_X_NEG_Y_POS_Z;
                    f.aoBitmaskD2 = DiagonalBitmask.NEG_X_NEG_Y_ZRO_Z;
                    f.aoBitmaskD3 = DiagonalBitmask.ZRO_X_NEG_Y_POS_Z;
                    f.x1 = x1;
                    f.y1 = y1;
                    f.z1 = z1;
                    f.x2 = x2;
                    f.y2 = y1;
                    f.z2 = z2;
                    f.midX1 = f.x2;
                    f.midY1 = f.y1;
                    f.midZ1 = f.z1;
                    f.midX2 = f.x1;
                    f.midY2 = f.y1;
                    f.midZ2 = f.z2;
                    f.uA = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vA = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uB = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vB = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uC = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vC = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uD = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vD = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    break;
                case "localPosY":
                    isPosYFaceOccluding |= minX <= 0.0F && maxX >= 1.0F && maxY == 1.0F && minZ <= 0.0F && maxZ >= 1.0F;
                    isPosYFacePartOccluding |= maxY == 1.0F;
                    f.vertexIndexA = VertexIndex.NX_PY_NZ;
                    f.aoBitmaskA1 = DiagonalBitmask.NEG_X_POS_Y_NEG_Z;
                    f.aoBitmaskA2 = DiagonalBitmask.NEG_X_POS_Y_ZRO_Z;
                    f.aoBitmaskA3 = DiagonalBitmask.ZRO_X_POS_Y_NEG_Z;
                    f.vertexIndexB = VertexIndex.NX_PY_PZ;
                    f.aoBitmaskB1 = DiagonalBitmask.NEG_X_POS_Y_POS_Z;
                    f.aoBitmaskB2 = DiagonalBitmask.NEG_X_POS_Y_ZRO_Z;
                    f.aoBitmaskB3 = DiagonalBitmask.ZRO_X_POS_Y_POS_Z;
                    f.vertexIndexC = VertexIndex.PX_PY_PZ;
                    f.aoBitmaskC1 = DiagonalBitmask.POS_X_POS_Y_POS_Z;
                    f.aoBitmaskC2 = DiagonalBitmask.POS_X_POS_Y_ZRO_Z;
                    f.aoBitmaskC3 = DiagonalBitmask.ZRO_X_POS_Y_POS_Z;
                    f.vertexIndexD = VertexIndex.PX_PY_NZ;
                    f.aoBitmaskD1 = DiagonalBitmask.POS_X_POS_Y_NEG_Z;
                    f.aoBitmaskD2 = DiagonalBitmask.POS_X_POS_Y_ZRO_Z;
                    f.aoBitmaskD3 = DiagonalBitmask.ZRO_X_POS_Y_NEG_Z;
                    f.x1 = x1;
                    f.y1 = y2;
                    f.z1 = z1;
                    f.x2 = x2;
                    f.y2 = y2;
                    f.z2 = z2;
                    f.midX1 = f.x1;
                    f.midY1 = f.y1;
                    f.midZ1 = f.z2;
                    f.midX2 = f.x2;
                    f.midY2 = f.y1;
                    f.midZ2 = f.z1;
                    f.uA = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vA = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uB = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vB = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uC = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vC = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uD = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vD = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    break;
                case "localNegZ":
                    isNegZFaceOccluding |= minX <= 0.0F && maxX >= 1.0F && minY <= 0.0F && maxY >= 1.0F && minZ == 0.0F;
                    isNegZFacePartOccluding |= minZ == 0.0F;
                    f.vertexIndexA = VertexIndex.NX_NY_NZ;
                    f.aoBitmaskA1 = DiagonalBitmask.NEG_X_NEG_Y_NEG_Z;
                    f.aoBitmaskA2 = DiagonalBitmask.ZRO_X_NEG_Y_NEG_Z;
                    f.aoBitmaskA3 = DiagonalBitmask.NEG_X_ZRO_Y_NEG_Z;
                    f.vertexIndexB = VertexIndex.NX_PY_NZ;
                    f.aoBitmaskB1 = DiagonalBitmask.NEG_X_POS_Y_NEG_Z;
                    f.aoBitmaskB2 = DiagonalBitmask.ZRO_X_POS_Y_NEG_Z;
                    f.aoBitmaskB3 = DiagonalBitmask.NEG_X_ZRO_Y_NEG_Z;
                    f.vertexIndexC = VertexIndex.PX_PY_NZ;
                    f.aoBitmaskC1 = DiagonalBitmask.POS_X_POS_Y_NEG_Z;
                    f.aoBitmaskC2 = DiagonalBitmask.ZRO_X_POS_Y_NEG_Z;
                    f.aoBitmaskC3 = DiagonalBitmask.POS_X_ZRO_Y_NEG_Z;
                    f.vertexIndexD = VertexIndex.PX_NY_NZ;
                    f.aoBitmaskD1 = DiagonalBitmask.POS_X_NEG_Y_NEG_Z;
                    f.aoBitmaskD2 = DiagonalBitmask.ZRO_X_NEG_Y_NEG_Z;
                    f.aoBitmaskD3 = DiagonalBitmask.POS_X_ZRO_Y_NEG_Z;
                    f.x1 = x1;
                    f.y1 = y1;
                    f.z1 = z1;
                    f.x2 = x2;
                    f.y2 = y2;
                    f.z2 = z1;
                    f.midX1 = f.x1;
                    f.midY1 = f.y2;
                    f.midZ1 = f.z1;
                    f.midX2 = f.x2;
                    f.midY2 = f.y1;
                    f.midZ2 = f.z1;
                    f.uA = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vA = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uB = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vB = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uC = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vC = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uD = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vD = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    break;
                case "localPosZ":
                    isPosZFaceOccluding |= minX <= 0.0F && maxX >= 1.0F && minY <= 0.0F && maxY >= 1.0F && maxZ == 1.0F;
                    isPosZFacePartOccluding |= maxZ == 1.0F;
                    f.vertexIndexA = VertexIndex.NX_NY_PZ;
                    f.aoBitmaskA1 = DiagonalBitmask.NEG_X_NEG_Y_POS_Z;
                    f.aoBitmaskA2 = DiagonalBitmask.ZRO_X_NEG_Y_POS_Z;
                    f.aoBitmaskA3 = DiagonalBitmask.NEG_X_ZRO_Y_POS_Z;
                    f.vertexIndexB = VertexIndex.PX_NY_PZ;
                    f.aoBitmaskB1 = DiagonalBitmask.POS_X_NEG_Y_POS_Z;
                    f.aoBitmaskB2 = DiagonalBitmask.ZRO_X_NEG_Y_POS_Z;
                    f.aoBitmaskB3 = DiagonalBitmask.POS_X_ZRO_Y_POS_Z;
                    f.vertexIndexC = VertexIndex.PX_PY_PZ;
                    f.aoBitmaskC1 = DiagonalBitmask.POS_X_POS_Y_POS_Z;
                    f.aoBitmaskC2 = DiagonalBitmask.ZRO_X_POS_Y_POS_Z;
                    f.aoBitmaskC3 = DiagonalBitmask.POS_X_ZRO_Y_POS_Z;
                    f.vertexIndexD = VertexIndex.NX_PY_PZ;
                    f.aoBitmaskD1 = DiagonalBitmask.NEG_X_POS_Y_POS_Z;
                    f.aoBitmaskD2 = DiagonalBitmask.ZRO_X_POS_Y_POS_Z;
                    f.aoBitmaskD3 = DiagonalBitmask.NEG_X_ZRO_Y_POS_Z;
                    f.x1 = x1;
                    f.y1 = y1;
                    f.z1 = z2;
                    f.x2 = x2;
                    f.y2 = y2;
                    f.z2 = z2;
                    f.midX1 = f.x2;
                    f.midY1 = f.y1;
                    f.midZ1 = f.z1;
                    f.midX2 = f.x1;
                    f.midY2 = f.y2;
                    f.midZ2 = f.z1;
                    f.uA = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vA = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uB = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vB = (t.uv[1] + f.uv[3] / 16.0F) / uvScale;
                    f.uC = (t.uv[0] + f.uv[2] / 16.0F) / uvScale;
                    f.vC = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    f.uD = (t.uv[0] + f.uv[0] / 16.0F) / uvScale;
                    f.vD = (t.uv[1] + f.uv[1] / 16.0F) / uvScale;
                    break;
                default:
                    isValidFace = false;
            }

            int tmpRotation = f.uvRotation / 90;
            while(tmpRotation > 0) {
                float tmpU = f.uA;
                float tmpV = f.vA;
                f.uA = f.uB;
                f.vA = f.vB;
                f.uB = f.uC;
                f.vB = f.vC;
                f.uC = f.uD;
                f.vC = f.vD;
                f.uD = tmpU;
                f.vD = tmpV;
                tmpRotation--;
            }

            f.modelUvIdxA = CustomTextureLoader.createUBOFloatsIdx(f.uA, f.vA);
            f.modelUvIdxB = CustomTextureLoader.createUBOFloatsIdx(f.uB, f.vB);
            f.modelUvIdxC = CustomTextureLoader.createUBOFloatsIdx(f.uC, f.vC);
            f.modelUvIdxD = CustomTextureLoader.createUBOFloatsIdx(f.uD, f.vD);

            if (isValidFace) allFaces.add(f);

        }
    }



}