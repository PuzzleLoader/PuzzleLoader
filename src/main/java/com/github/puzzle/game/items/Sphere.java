package com.github.puzzle.game.items;

import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.Mesh;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.VertexAttributes;
import com.badlogic.gdx.graphics.g3d.Material;
import com.badlogic.gdx.graphics.g3d.attributes.TextureAttribute;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.graphics.g3d.utils.ModelBuilder;
import com.badlogic.gdx.utils.Array;
import com.github.puzzle.core.Identifier;
import com.github.puzzle.core.Puzzle;
import com.github.puzzle.core.resources.PuzzleGameAssetLoader;
import com.github.puzzle.core.resources.ResourceLocation;

public class Sphere implements IModItem {

    Identifier id = new Identifier(Puzzle.MOD_ID, "sphere");

    @Override
    public String toString() {
        return "Item: " + getID();
    }

    @Override
    public Identifier getIdentifier() {
        return id;
    }

    @Override
    public Array<Mesh> getMesh() {
        ModelBuilder modelBuilder = new ModelBuilder();
        modelBuilder.begin();

        Texture texture = PuzzleGameAssetLoader.LOADER.getResource(getTexturePath(), Texture.class);

        MeshPartBuilder meshBuilder = modelBuilder.part(
                "MOD_ITEM",
                GL20.GL_TRIANGLES,
                VertexAttributes.Usage.Position | VertexAttributes.Usage.Normal | VertexAttributes.Usage.TextureCoordinates,
                new Material(TextureAttribute.createDiffuse(texture))
        );
        meshBuilder.box(1, 0, 0, 1, 1, 1);
        meshBuilder.box(0, 0, 1, 1, 1, 1);
        meshBuilder.box(0, 3.5f, 0, 1, 8, 1);
        meshBuilder.setUVRange(0, 0, 16, 16);
        return modelBuilder.end().meshes;
    }

    @Override
    public ResourceLocation getTexturePath() {
        return new ResourceLocation(Puzzle.MOD_ID, "textures/items/sphere.png");
    }
}
