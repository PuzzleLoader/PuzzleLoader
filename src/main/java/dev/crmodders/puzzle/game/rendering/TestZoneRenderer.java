package dev.crmodders.puzzle.game.rendering;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.glutils.ShapeRenderer;
import com.badlogic.gdx.math.Frustum;
import com.badlogic.gdx.math.Vector3;
import com.badlogic.gdx.math.collision.BoundingBox;
import com.badlogic.gdx.utils.*;
import finalforeach.cosmicreach.GameSingletons;
import finalforeach.cosmicreach.RuntimeInfo;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.rendering.*;
import finalforeach.cosmicreach.world.Chunk;
import finalforeach.cosmicreach.world.Region;
import finalforeach.cosmicreach.world.RegionOctant;
import finalforeach.cosmicreach.world.Zone;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Random;

public class TestZoneRenderer implements PuzzleZoneRenderer {
    private final ObjectMap<Region, Array<Chunk>> regionChunksToRender = new ObjectMap();
    private final Array<Region> regionsToRender = new Array(false, 16, Region.class);
    private final IntMap<Array<TestChunkBatch>> layers = new IntMap();
    private final IntMap<Boolean> layerWritesToDepth = new IntMap();
    public HashMap<BatchMaterial, TestChunkBatch> batchMap = new HashMap();
    private final IntArray layerNums = new IntArray();
    private Vector3 tmpVec = new Vector3();
    private Vector3 lastCameraPosition;
    private Vector3 lastCameraDirection;
    private ObjectIntMap<Region> numChunksPerRegion = new ObjectIntMap();
    private boolean gotNewChunksToRender;
    private ShapeRenderer batchDebuggingShapeRenderer;
    public boolean drawDebugLines = true;
    private Array<Chunk> chunksToNoLongerRender = new Array();
    private static final BatchMaterial tmpMat = new BatchMaterial();
    private static final BoundingBox tmpBounds = new BoundingBox();
    private static final BatchCoords tmpBatchCoords = new BatchCoords();
    private static final ObjectSet<Chunk> tmpLastRendered = new ObjectSet();
    boolean haveChunksToRemove;

    public TestZoneRenderer() {
    }

    public void unload() {
        this.disposeUnusedBatches(true);
        IWorldRenderingMeshGenThread meshGenThread = GameSingletons.meshGenThread;
        meshGenThread.unload();
    }

    public void removeRegion(Region r) {
        this.numChunksPerRegion.remove(r, 0);
        this.regionChunksToRender.remove(r);
        this.regionsToRender.removeValue(r, true);
    }

    private boolean regionInBounds(Camera worldCamera, Region r) {
        return r.boundingBox.contains(worldCamera.position) || worldCamera.frustum.boundsInFrustum(r.boundingBox);
    }

    private boolean octantInBounds(Camera worldCamera, Region r, RegionOctant octant, BoundingBox tmpBounds) {
        octant.getBounds(r, tmpBounds);
        return worldCamera.frustum.boundsInFrustum(tmpBounds);
    }

    private boolean chunkBatchInBounds(Camera worldCamera, Chunk chunk, BoundingBox tmpBounds) {
        Frustum frustum = worldCamera.frustum;
        tmpBatchCoords.setBatchCoordsFromChunk(chunk);
        tmpBounds.min.set((float)tmpBatchCoords.blockX(), (float)tmpBatchCoords.blockY(), (float)tmpBatchCoords.blockZ());
        float w = 64.0F;
        tmpBounds.max.set(tmpBounds.min).add(w, w, w);
        tmpBounds.update();
        return frustum.boundsInFrustum(tmpBounds);
    }

    private boolean boundsContainedInView(Camera worldCamera, BoundingBox box) {
        Frustum frustum = worldCamera.frustum;
        this.tmpVec.set(box.min);
        if (!frustum.pointInFrustum(this.tmpVec)) {
            return false;
        } else {
            this.tmpVec.add(box.max.x - box.min.x, 0.0F, 0.0F);
            if (!frustum.pointInFrustum(this.tmpVec)) {
                return false;
            } else {
                this.tmpVec.set(box.min).add(0.0F, box.max.y - box.min.y, 0.0F);
                if (!frustum.pointInFrustum(this.tmpVec)) {
                    return false;
                } else {
                    this.tmpVec.set(box.min).add(0.0F, 0.0F, box.max.z - box.min.z);
                    if (!frustum.pointInFrustum(this.tmpVec)) {
                        return false;
                    } else {
                        this.tmpVec.set(box.max);
                        if (!frustum.pointInFrustum(this.tmpVec)) {
                            return false;
                        } else {
                            this.tmpVec.add(box.min.x - box.max.x, 0.0F, 0.0F);
                            if (!frustum.pointInFrustum(this.tmpVec)) {
                                return false;
                            } else {
                                this.tmpVec.set(box.max).add(0.0F, box.min.y - box.max.y, 0.0F);
                                if (!frustum.pointInFrustum(this.tmpVec)) {
                                    return false;
                                } else {
                                    this.tmpVec.set(box.max).add(0.0F, 0.0F, box.min.z - box.max.z);
                                    return frustum.pointInFrustum(this.tmpVec);
                                }
                            }
                        }
                    }
                }
            }
        }
    }

    private boolean getChunksToRenderShouldEarlyExit(Zone zone, Camera worldCamera) {
        this.gotNewChunksToRender = false;
        if (this.lastCameraPosition == null) {
            this.lastCameraPosition = new Vector3(worldCamera.position);
            this.lastCameraDirection = new Vector3(worldCamera.direction);
        } else if (this.lastCameraPosition.epsilonEquals(worldCamera.position) && this.lastCameraDirection.epsilonEquals(worldCamera.direction)) {
            boolean earlyExit = true;
            synchronized(zone.getRegionLock()) {
                Region[] var5 = zone.getRegions();
                int var6 = var5.length;

                for(int var7 = 0; var7 < var6; ++var7) {
                    Region r = var5[var7];
                    if (this.regionInBounds(worldCamera, r) && this.numChunksPerRegion.get(r, 0) != r.getNumberOfChunks()) {
                        earlyExit = false;
                        break;
                    }
                }
            }

            if (earlyExit) {
                return true;
            }
        }

        return false;
    }

    private void getChunksToRender(Zone zone, Camera worldCamera) {
        if (!this.getChunksToRenderShouldEarlyExit(zone, worldCamera)) {
            this.lastCameraPosition.set(worldCamera.position);
            this.lastCameraDirection.set(worldCamera.direction);
            Region[] var3 = zone.getRegions();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                Region r = var3[var5];
                Array<Chunk> chunksToRender = (Array)this.regionChunksToRender.get(r);
                if (!this.regionInBounds(worldCamera, r)) {
                    this.regionChunksToRender.remove(r);
                    this.regionsToRender.removeValue(r, true);
                } else {
                    if (chunksToRender == null) {
                        chunksToRender = new Array(Chunk.class);
                        this.regionChunksToRender.put(r, chunksToRender);
                        if (!this.regionsToRender.contains(r, true)) {
                            this.regionsToRender.add(r);
                        }
                    }

                    int prevChunksToRenderSize = chunksToRender.size;
                    boolean regionContained = this.boundsContainedInView(worldCamera, r.boundingBox);
                    int numChunksInregion = r.getNumberOfChunks();
                    this.chunksToNoLongerRender.clear();
                    this.chunksToNoLongerRender.addAll(chunksToRender);
                    chunksToRender.size = 0;
                    this.numChunksPerRegion.put(r, numChunksInregion);
                    RegionOctant[] var11 = r.octants;
                    int var12 = var11.length;

                    label92:
                    for(int var13 = 0; var13 < var12; ++var13) {
                        RegionOctant octant = var11[var13];
                        if (this.octantInBounds(worldCamera, r, octant, tmpBounds)) {
                            boolean octantContained = regionContained;
                            if (!regionContained) {
                                octant.getBounds(r, tmpBounds);
                                octantContained = this.boundsContainedInView(worldCamera, tmpBounds);
                            }

                            Array.ArrayIterator<Chunk> octantChunkIt = octant.getMeshedChunks().iterator();

                            while(true) {
                                while(true) {
                                    Chunk chunk;
                                    do {
                                        if (!octantChunkIt.hasNext()) {
                                            continue label92;
                                        }

                                        chunk = (Chunk)octantChunkIt.next();
                                    } while(chunk == null);

                                    IChunkMeshGroup<?> meshGroup = chunk.getMeshGroup();
                                    if (meshGroup.hasMesh() && !meshGroup.isFlaggedForRemeshing() && meshGroup.isAllMeshDataEmpty()) {
                                        octantChunkIt.remove();
                                    } else if (octantContained || this.chunkBatchInBounds(worldCamera, chunk, tmpBounds)) {
                                        chunksToRender.add(chunk);
                                    }
                                }
                            }
                        }
                    }

                    if (prevChunksToRenderSize > chunksToRender.size) {
                        this.gotNewChunksToRender = true;
                    }

                    if (!this.gotNewChunksToRender) {
                        tmpLastRendered.clear();
                        this.chunksToNoLongerRender.forEach((c) -> {
                            tmpLastRendered.add(c);
                        });
                        Array.ArrayIterator var19 = chunksToRender.iterator();

                        while(var19.hasNext()) {
                            Chunk chunk = (Chunk)var19.next();
                            this.gotNewChunksToRender = !tmpLastRendered.contains(chunk);
                            if (this.gotNewChunksToRender) {
                                break;
                            }
                        }
                    }
                }
            }

        }
    }

    public void onChunkFlaggedForRemeshing(Chunk chunk) {
        if (chunk.isGenerated()) {
            IChunkMeshGroup<?> meshGroup = chunk.getMeshGroup();
            if (meshGroup != null) {
                IWorldRenderingMeshGenThread meshGenThread = GameSingletons.meshGenThread;
                synchronized(meshGenThread.getAddChunkLock()) {
                    meshGroup.flushRemeshRequests();
                    meshGenThread.addChunk(chunk);
                }

                this.haveChunksToRemove = true;
                Region region = chunk.region;
                if (region != null) {
                    RegionOctant octant = region.getOctant(chunk);
                    Array<Chunk> m = octant.getMeshedChunks();
                    if (!m.contains(chunk, true)) {
                        m.add(chunk);
                    }
                }

            }
        }
    }

    private void requestMeshes() {
        GameSingletons.meshGenThread.meshChunks(this);
    }

    private void addMeshDatasToChunkBatches() {
        if (this.gotNewChunksToRender || ChunkMeshGroup.setMeshGenRecently) {
            ChunkMeshGroup.setMeshGenRecently = false;
            Region[] var1 = (Region[])this.regionsToRender.items;
            int var2 = var1.length;

            for(int var3 = 0; var3 < var2; ++var3) {
                Region region = var1[var3];
                if (region == null) {
                    break;
                }

                Array<Chunk> chunksToRender = (Array)this.regionChunksToRender.get(region);

                for(int ci = 0; ci < chunksToRender.size; ++ci) {
                    Chunk chunk = ((Chunk[])chunksToRender.items)[ci];
                    tmpBatchCoords.setBatchCoordsFromChunk(chunk);
                    ChunkMeshGroup meshGroup = (ChunkMeshGroup)chunk.getMeshGroup();
                    Array<MeshData> chunkAllMeshData = meshGroup.getAllMeshData();

                    for(int mi = 0; mi < chunkAllMeshData.size; ++mi) {
                        MeshData m = ((MeshData[])chunkAllMeshData.items)[mi];
                        if (m != null) {
                            RenderOrder renderOrder = m.getRenderOrder();
                            tmpMat.set(tmpBatchCoords, m.getShader(), renderOrder);
                            TestChunkBatch batch = (TestChunkBatch)this.batchMap.get(tmpMat);
                            if (batch == null) {
                                Array<TestChunkBatch> layer = (Array)this.layers.get(renderOrder.getOrder());
                                if (layer == null) {
                                    layer = new Array(TestChunkBatch.class);
                                    this.layers.put(renderOrder.getOrder(), layer);
                                    this.layerWritesToDepth.put(renderOrder.getOrder(), renderOrder.doesWriteToDepth());
                                    this.layerNums.add(renderOrder.getOrder());
                                    this.layerNums.shrink();
                                    this.layerNums.sort();
                                }

                                batch = new TestChunkBatch(new BatchMaterial(tmpMat), layer);
                                this.batchMap.put(batch.material, batch);
                            }

                            batch.addMeshData(chunk, m);
                        }
                    }
                }
            }

        }
    }

    public void addChunk(Chunk chunk) {
        IChunkMeshGroup<?> meshGroup = chunk.getMeshGroup();
        if (meshGroup == null) {
            meshGroup = new ChunkMeshGroup();
            chunk.setMeshGroup(meshGroup);
            chunk.flagForRemeshing(false);
        }

    }

    public void removeChunk(Chunk chunk) {
        this.haveChunksToRemove = true;
    }

    private void disposeUnusedBatches(boolean unloadAll) {
        boolean haveChunksToRemove = this.haveChunksToRemove;
        this.haveChunksToRemove = false;
        if (haveChunksToRemove || unloadAll) {
            Iterator<TestChunkBatch> batches = this.batchMap.values().iterator();

            while(true) {
                TestChunkBatch batch;
                do {
                    if (!batches.hasNext()) {
                        return;
                    }

                    batch = (TestChunkBatch)batches.next();
                } while(batch.seen && !unloadAll);

                Array.ArrayIterator var5 = batch.chunkMeshGroups.iterator();

                label50: {
                    IChunkMeshGroup meshGroup;
                    Array allMeshData;
                    do {
                        if (!var5.hasNext()) {
                            break label50;
                        }

                        meshGroup = (IChunkMeshGroup)var5.next();
                        allMeshData = ((ChunkMeshGroup)meshGroup).getAllMeshData();
                    } while(!unloadAll && meshGroup.hasMesh() && !allMeshData.isEmpty() && !meshGroup.isFlaggedForRemeshing() && meshGroup.hasExpectedMeshCount());

                    batch.dispose(unloadAll);
                }

                if (batch.disposed) {
                    batch.layer.removeValue(batch, true);
                    batches.remove();
                }
            }
        }
    }

    public void render(Zone zone, Camera worldCamera) {
        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        this.getChunksToRender(zone, worldCamera);
        this.requestMeshes();
        this.disposeUnusedBatches(false);
        this.addMeshDatasToChunkBatches();
        ChunkBatch.lastBoundShader = null;
        if (SharedQuadIndexData.indexData != null && RuntimeInfo.useSharedIndices) {
            SharedQuadIndexData.indexData.bind();
        }

        int[] var3 = this.layerNums.items;
        int var4 = var3.length;

        int var5;
        int layerNum;
        for(var5 = 0; var5 < var4; ++var5) {
            layerNum = var3[var5];
            Array<ChunkBatch> layer = (Array)this.layers.get(layerNum);
            if (layer != null) {
                Gdx.gl.glDepthMask((Boolean)this.layerWritesToDepth.get(layerNum, true));
                Array.ArrayIterator var8 = layer.iterator();

                while(var8.hasNext()) {
                    TestChunkBatch batch = (TestChunkBatch)var8.next();
                    batch.render(zone, worldCamera);
                }
            }
        }

        if (ChunkBatch.lastBoundShader != null) {
            ChunkBatch.lastBoundShader.unbind();
        }

        if (SharedQuadIndexData.indexData != null && RuntimeInfo.useSharedIndices) {
            SharedQuadIndexData.indexData.unbind();
        }

        if (this.drawDebugLines) {
            if (this.batchDebuggingShapeRenderer == null) {
                this.batchDebuggingShapeRenderer = new ShapeRenderer();
            }

            this.batchDebuggingShapeRenderer.setProjectionMatrix(worldCamera.combined);
            this.batchDebuggingShapeRenderer.begin(ShapeRenderer.ShapeType.Line);
            Random rand = new Random();
            for (var cA : regionChunksToRender.values()) {
                for(var chunk : cA) {
                    tmpBatchCoords.setBatchCoordsFromChunk(chunk);
                    this.batchDebuggingShapeRenderer.setColor(0.0F, .2F, .7F, 1.0F);
                    this.batchDebuggingShapeRenderer.box(chunk.chunkX*16, chunk.chunkY*16, chunk.chunkZ*16, 16, 16, -16);

                    /*
                    else {
                        int h = chunk.hashCode();
                        rand.setSeed((long)h);
                        float r = 0.0F;
                        float g = rand.nextFloat();
                        float b = rand.nextFloat();
                        this.batchDebuggingShapeRenderer.setColor(r, g, b, 1.0F);
                        this.batchDebuggingShapeRenderer.box(chunk.chunkX*16, chunk.chunkY*16, chunk.chunkZ*16, 16, 16, -16);
                    }

                     */
                }
            }
    /*
            int[] var17 = this.layerNums.items;
            var5 = var17.length;


            for(layerNum = 0; layerNum < var5; ++layerNum) {
                layerNum = var17[layerNum];
                Array layer = (Array)this.layers.get(layerNum);
                if (layer != null) {
                    Array.ArrayIterator var20 = layer.iterator();

                    while(var20.hasNext()) {


                        TestChunkBatch batch = (TestChunkBatch)var20.next();
                        if (!worldCamera.frustum.boundsInFrustum(batch.boundingBox)) {
                            this.batchDebuggingShapeRenderer.setColor(1.0F, 0.0F, 0.0F, 1.0F);
                            BoundingBox bb = batch.boundingBox;
                            this.batchDebuggingShapeRenderer.box(bb.min.x, bb.min.y, bb.min.z, bb.getWidth(), bb.getHeight(), -bb.getDepth());
                        } else {
                            int h = batch.hashCode();
                            rand.setSeed((long)h);
                            float r = 0.0F;
                            float g = rand.nextFloat();
                            float b = rand.nextFloat();
                            this.batchDebuggingShapeRenderer.setColor(r, g, b, 1.0F);
                            BoundingBox bb = batch.boundingBox;
                            this.batchDebuggingShapeRenderer.box(bb.min.x, bb.min.y, bb.min.z, bb.getWidth(), bb.getHeight(), -bb.getDepth());
                        }
                    }
                }
            }
        */
            this.batchDebuggingShapeRenderer.end();
        }

        Gdx.gl.glActiveTexture(33984);
        Gdx.gl.glBindTexture(3553, 0);
    }

    public void dispose() {
        IWorldRenderingMeshGenThread meshGenThread = GameSingletons.meshGenThread;
        meshGenThread.unload();
        meshGenThread.stopThread();
    }

    public String getName() {
        return Lang.get("Earth_Day");
    }

    public void onChunkMeshed(Chunk chunk) {
        this.haveChunksToRemove = true;
    }
}
