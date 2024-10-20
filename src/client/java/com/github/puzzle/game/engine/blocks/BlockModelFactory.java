package com.github.puzzle.game.engine.blocks;

import com.badlogic.gdx.utils.Json;
import com.github.puzzle.game.engine.blocks.models.PuzzleBlockModel;
import com.github.puzzle.game.resources.PuzzleGameAssetLoader;
import com.github.puzzle.game.resources.VanillaAssetLocations;
import finalforeach.cosmicreach.blocks.BlockState;
import finalforeach.cosmicreach.rendering.blockmodels.BlockModel;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

public class BlockModelFactory implements IBlockModelFactory {

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

        PuzzleBlockModel model = PuzzleBlockModel.fromJson(modelJson, modelName, rotXZ);
        if (model.parent != null) {
            getInstance(model.parent, rotXZ);
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
        PuzzleBlockModel model = PuzzleBlockModel.fromJson(modelJson, modelName, rotXZ);
        if (model.parent != null) {
            getInstance(model.parent, rotXZ);
        }

        models.put(key, model);
        return model;
    }

    @Override
    public void createGeneratedModelInstance(BlockState blockState, BlockModel parentModel, String parentModelName, String modelName, int rotXZ) {
        modelName = getNotShitModelName(modelName);
        final InstanceKey key = new InstanceKey(modelName, rotXZ);
        if (models.containsKey(key)) {
            return;
        }

        if (parentModel instanceof PuzzleBlockModel fluxParent) {
            Json json = new Json();
            json.setTypeName(null);

            String modelJson;
            modelJson = "{\"parent\": \"" + parentModelName + "\", \"textures\":" + json.toJson(fluxParent.getTextures()) + "}";

            PuzzleBlockModel model = PuzzleBlockModel.fromJson(modelJson, modelName, rotXZ);
            if (model.parent != null) {
                getInstance(model.parent, rotXZ);
            }
            models.put(key, model);
        } else {
            LOGGER.error("can't create generated instances for '{}'", parentModel.getClass().getSimpleName());
        }
    }

    private int getNumberOfParents(PuzzleBlockModel model) {
        int n = 0;
        String parent = model.parent;
        while (parent != null) {
            PuzzleBlockModel parentModel = null;

            InstanceKey parentKey;
            if (models.containsKey(parentKey = new InstanceKey(parent, 0)))
                parentModel = (PuzzleBlockModel) models.get(parentKey);
            else if (models.containsKey(parentKey = new InstanceKey(parent, 90)))
                parentModel = (PuzzleBlockModel) models.get(parentKey);
            else if (models.containsKey(parentKey = new InstanceKey(parent, 180)))
                parentModel = (PuzzleBlockModel) models.get(parentKey);
            else if (models.containsKey(parentKey = new InstanceKey(parent, 270)))
                parentModel = (PuzzleBlockModel) models.get(parentKey);

            parent = parentModel == null ? null : parentModel.parent;
            n++;
        }
        return n;
    }

    public int compare(BlockModel o1, BlockModel o2) {
        if (o1 instanceof PuzzleBlockModel f1 && o2 instanceof PuzzleBlockModel f2) {
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