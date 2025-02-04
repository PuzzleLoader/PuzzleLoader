package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.utils.Json;
import com.github.puzzle.game.PuzzleRegistries;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.resources.VanillaAssetLocations;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModelJson;
import finalforeach.cosmicreach.util.Identifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlockModelFactory extends DummyBlockModelFactory implements IBlockModelFactory {

    public record InstanceKey(String modelName, int rotXZ) {}

    public final Map<InstanceKey, BlockModel> models = new LinkedHashMap<>();

    private static final Logger LOGGER = LoggerFactory.getLogger("Puzzle | BlockModelFactory");

    private static String getNotShitModelName(String modelName){
        if(modelName.startsWith("gen_model::")) {
            modelName = modelName.substring("gen_model::".length()) + "_gen_model";
        }
        return modelName;
    }

    public void registerBlockModel(String modelName, int rotXZ, BlockModel model) {
        modelName = getNotShitModelName(modelName);
        final InstanceKey key = new InstanceKey(modelName, rotXZ);
        if (models.containsKey(key)) {
            return;
        }
        models.put(key, model);
    }

    public BlockModel createFromJson(String modelName, int rotXZ, String modelJson) {
        modelName = getNotShitModelName(modelName);
        final InstanceKey key = new InstanceKey(modelName, rotXZ);
        if (models.containsKey(key)) {
            return models.get(key);
        }

        BlockModelJson model = BlockModelJson.getInstanceFromJsonStr(modelName, modelJson, rotXZ);
        addInit(model);
        String parent = getModelParent(model);
        if (parent != null) {
            getInstance(parent, rotXZ);
        }

        models.put(key, model);
        return model;
    }

    @Override
    public BlockModel getInstance(String modelName, int rotXZ) {
        modelName = getNotShitModelName(modelName);
        final InstanceKey key = new InstanceKey(modelName, rotXZ);
        if (models.containsKey(key)) {
            return models.get(key);
        }

        String modelJson = PuzzleGameAssetLoader.locateAsset(VanillaAssetLocations.getBlockModel(modelName)).readString();
        BlockModelJson model = BlockModelJson.getInstanceFromJsonStr(modelName, modelJson, rotXZ);
        addInit(model);

        String parent = getModelParent(model);
        if (parent != null) {
            getInstance(parent, rotXZ);
        }

        models.put(key, model);
        return model;
    }

    public static String getModelParent(BlockModelJson model) {
        try {
            Field f = BlockModelJson.class.getDeclaredField("parent");
            f.setAccessible(true);
            return (String) f.get(model);
        } catch (NoSuchFieldException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void createGeneratedModelInstance(BlockState blockState, BlockModel parentModel, String parentModelName, String modelName, int rotXZ) {
        modelName = getNotShitModelName(modelName);
        final InstanceKey key = new InstanceKey(modelName, rotXZ);
        if (models.containsKey(key)) {
            return;
        }

        Json json = new Json();
        json.setTypeName(null);

        String modelJson;
        modelJson = "{\"parent\": \"" + parentModelName + "\", \"textures\":" + json.toJson(((BlockModelJson) parentModel).getTextures()) + "}";

        BlockModelJson model = BlockModelJson.getInstanceFromJsonStr(modelName, modelJson, rotXZ);
        addInit(model);
        String parent = getModelParent(model);
        if (parent != null) {
            model.cullsSelf = parentModel.cullsSelf;
            model.isTransparent = parentModel.isTransparent;
            getInstance(parent, rotXZ);
        }

        models.put(key, model);
    }

    private void addInit(BlockModelJson model) {
        Threads.runOnMainThread(() -> {
            BlockModelJsonInitializer.init(model);
        });
    }

    private int getNumberOfParents(BlockModelJson model) {
        int n = 0;
        String parent = getModelParent(model);
        while (parent != null) {
            BlockModelJson parentModel = null;

            InstanceKey parentKey;
            if (models.containsKey(parentKey = new InstanceKey(parent, 0)))
                parentModel = (BlockModelJson) models.get(parentKey);
            else if (models.containsKey(parentKey = new InstanceKey(parent, 90)))
                parentModel = (BlockModelJson) models.get(parentKey);
            else if (models.containsKey(parentKey = new InstanceKey(parent, 180)))
                parentModel = (BlockModelJson) models.get(parentKey);
            else if (models.containsKey(parentKey = new InstanceKey(parent, 270))) {
                parentModel = (BlockModelJson) models.get(parentKey);
            }

            parent = parentModel == null ? null : getModelParent(parentModel);
            n++;
        }
        return n;
    }

    public int compare(BlockModel o1, BlockModel o2) {
        if (o1 instanceof BlockModelJson f1 && o2 instanceof BlockModelJson f2) {
            return Integer.compare(getNumberOfParents(f1), getNumberOfParents(f2));
        }
        return 0;
    }

    public List<BlockModel> sort() {
        List<BlockModel> models = new ArrayList<>(this.models.values());
        models.sort(this::compare);
        return models;
    }

}