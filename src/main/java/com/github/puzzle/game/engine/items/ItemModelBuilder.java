package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.*;
import com.badlogic.gdx.graphics.g3d.utils.MeshBuilder;
import com.badlogic.gdx.graphics.g3d.utils.MeshPartBuilder;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.math.Vector3;
import com.github.puzzle.game.engine.shaders.ItemShader;

public class ItemModelBuilder {

        static MeshBuilder builder = new MeshBuilder();

        public static Mesh build2DMesh() {
            return buildItemRects(0, true);
        }

        public static Mesh build2_5DMesh(Texture texture) {
            buildItemRects((1f / texture.getWidth()), false);
            buildMeshSides(texture);
            return builder.end();
        }

        public static MeshPartBuilder.VertexInfo vertex(float x, float y, float z, float u, float v) {
            MeshPartBuilder.VertexInfo vertexInfo = new MeshPartBuilder.VertexInfo();
            vertexInfo.setPos(new Vector3(x, y, z));
            vertexInfo.setUV(new Vector2(u, v));
            return vertexInfo;
        }

        public static Texture flip(Texture texture) {
            TextureData data = texture.getTextureData();
            data.prepare();

            Pixmap donorPixmap = data.consumePixmap();
            Pixmap newPixmap = new Pixmap(donorPixmap.getWidth(), donorPixmap.getHeight(), donorPixmap.getFormat());

            for (int x = 0; x < donorPixmap.getWidth(); x++) {
                for (int y = 0; y < donorPixmap.getHeight(); y++) {
                    newPixmap.drawPixel(x, y, donorPixmap.getPixel((donorPixmap.getWidth() - 1) - x, (donorPixmap.getHeight() - 1) - y));
                }
            }
            return new Texture(newPixmap);
        }

        public static void buildMeshSides(Texture texture) {
            Pixmap pixmap = texture.getTextureData().consumePixmap();

            if (!pixmap.getFormat().equals(Pixmap.Format.RGBA8888)) return;
            float pixelSize = (1f / texture.getWidth()) * 2f;

            for (int y = texture.getHeight() - 1; y >= 0; y--) {
                for (int x = 0; x < texture.getWidth(); x++) {
                    float u0 = (1f / texture.getWidth()) * x;
                    float v0 = (1f / texture.getHeight()) * y;
                    float u1 = ((1f / texture.getWidth()) * x) + (1f / texture.getWidth());
                    float v1 = ((1f / texture.getHeight()) * y) + (1f / texture.getHeight());
                    float startingPos = 1 + (x * -pixelSize);

                    boolean isAboveClear = y + 1 == texture.getHeight() || (pixmap.getPixel(x, y + 1) & 0x000000FF) == 0;
                    boolean isBelowClear = y == 0 || (pixmap.getPixel(x, y - 1) & 0x000000FF) == 0;

                    boolean isRightClear = x + 1 == texture.getWidth() || (pixmap.getPixel(x + 1, y) & 0x000000FF) == 0;
                    boolean isLeftClear = x == 0 || (pixmap.getPixel(x - 1, y) & 0x000000FF) == 0;

                    boolean isSolid = (pixmap.getPixel(x, y) & 0x000000FF) != 0;

                    // Left Faces
                    if (isLeftClear && isSolid) {
                        MeshPartBuilder.VertexInfo topLeft0 = vertex(startingPos, (pixelSize * y) + pixelSize, (pixelSize / 2f), u1, v1);
                        MeshPartBuilder.VertexInfo topRight0 = vertex(startingPos, (pixelSize * y) + pixelSize, ((pixelSize / 2f) - pixelSize), u1, v1);
                        MeshPartBuilder.VertexInfo bottomLeft0 = vertex(startingPos, pixelSize * y, (pixelSize / 2f), u1, v0);
                        MeshPartBuilder.VertexInfo bottomRight0 = vertex(startingPos, pixelSize * y, ((pixelSize / 2f) - pixelSize), u0, v0);

                        builder.rect(topLeft0, bottomLeft0, bottomRight0, topRight0);
                    }

                    // Right Faces
                    if (isRightClear && isSolid) {
                        MeshPartBuilder.VertexInfo topLeft0 = vertex(startingPos - pixelSize, (pixelSize * y) + pixelSize, (pixelSize / 2f), u1, v1);
                        MeshPartBuilder.VertexInfo topRight0 = vertex(startingPos - pixelSize, (pixelSize * y) + pixelSize, ((pixelSize / 2f) - pixelSize), u1, v1);
                        MeshPartBuilder.VertexInfo bottomLeft0 = vertex(startingPos - pixelSize, pixelSize * y, (pixelSize / 2f), u1, v0);
                        MeshPartBuilder.VertexInfo bottomRight0 = vertex(startingPos - pixelSize, pixelSize * y, ((pixelSize / 2f) - pixelSize), u0, v0);

                        builder.rect(topLeft0, topRight0, bottomRight0, bottomLeft0);
                    }

                    float startingPos2 = (y * pixelSize);

                    // Top Faces
                    if (isAboveClear && isSolid) {
                        MeshPartBuilder.VertexInfo topLeft0 = vertex((pixelSize * ((texture.getWidth() / 2f) - x)), startingPos2 + pixelSize, (pixelSize / 2f), u1, v1);
                        MeshPartBuilder.VertexInfo topRight0 = vertex((pixelSize * ((texture.getWidth() / 2f) - x)), startingPos2 + pixelSize, ((pixelSize / 2f) - pixelSize), u1, v1);
                        MeshPartBuilder.VertexInfo bottomLeft0 = vertex(pixelSize * ((texture.getWidth() / 2f) - x) - pixelSize, startingPos2 + pixelSize, (pixelSize / 2f), u1, v0);
                        MeshPartBuilder.VertexInfo bottomRight0 = vertex(pixelSize * ((texture.getWidth() / 2f) - x) - pixelSize, startingPos2 + pixelSize, ((pixelSize / 2f) - pixelSize), u0, v0);

                        builder.rect(topLeft0, topRight0, bottomRight0, bottomLeft0);
                    }

                    // Bottom Faces
                    if (isBelowClear && isSolid) {
                        MeshPartBuilder.VertexInfo topLeft0 = vertex((pixelSize * ((texture.getWidth() / 2f) - x)), startingPos2, (pixelSize / 2f), u1, v1);
                        MeshPartBuilder.VertexInfo topRight0 = vertex((pixelSize * ((texture.getWidth() / 2f) - x)), startingPos2, ((pixelSize / 2f) - pixelSize), u1, v1);
                        MeshPartBuilder.VertexInfo bottomLeft0 = vertex(pixelSize * ((texture.getWidth() / 2f) - x) - pixelSize, startingPos2, (pixelSize / 2f), u1, v0);
                        MeshPartBuilder.VertexInfo bottomRight0 = vertex(pixelSize * ((texture.getWidth() / 2f) - x) - pixelSize, startingPos2, ((pixelSize / 2f) - pixelSize), u0, v0);

                        builder.rect(topLeft0, bottomLeft0, bottomRight0, topRight0);
                    }
                }
            }
        }

        public static Mesh buildItemRects(float sideOffsets, boolean endMesh) {
            builder.begin(ItemShader.DEFAULT_ITEM_SHADER.allVertexAttributesObj, GL20.GL_TRIANGLES);

            MeshPartBuilder.VertexInfo topLeft0 = new MeshPartBuilder.VertexInfo();
            topLeft0.setPos(new Vector3(-1, 2, sideOffsets));
            topLeft0.setUV(new Vector2(1, 1));

            MeshPartBuilder.VertexInfo topRight0 = new MeshPartBuilder.VertexInfo();
            topRight0.setPos(new Vector3(1, 2, sideOffsets));
            topRight0.setUV(new Vector2(0, 1));

            MeshPartBuilder.VertexInfo bottomLeft0 = new MeshPartBuilder.VertexInfo();
            bottomLeft0.setPos(new Vector3(-1, 0, sideOffsets));
            bottomLeft0.setUV(new Vector2(1, 0));

            MeshPartBuilder.VertexInfo bottomRight0 = new MeshPartBuilder.VertexInfo();
            bottomRight0.setPos(new Vector3(1, 0, sideOffsets));
            bottomRight0.setUV(new Vector2(0, 0));

            builder.rect(topLeft0, bottomLeft0, bottomRight0, topRight0);

            MeshPartBuilder.VertexInfo topLeft1 = new MeshPartBuilder.VertexInfo();
            topLeft1.setPos(new Vector3(-1, 2, -sideOffsets));
            topLeft1.setUV(topLeft0.uv.cpy());

            MeshPartBuilder.VertexInfo topRight1 = new MeshPartBuilder.VertexInfo();
            topRight1.setPos(new Vector3(1, 2, -sideOffsets));
            topRight1.setUV(topRight0.uv.cpy());

            MeshPartBuilder.VertexInfo bottomLeft1 = new MeshPartBuilder.VertexInfo();
            bottomLeft1.setPos(new Vector3(-1, 0, -sideOffsets));
            bottomLeft1.setUV(bottomLeft0.uv.cpy());

            MeshPartBuilder.VertexInfo bottomRight1 = new MeshPartBuilder.VertexInfo();
            bottomRight1.setPos(new Vector3(1, 0, -sideOffsets));
            bottomRight1.setUV(bottomRight0.uv.cpy());

            builder.rect(topLeft1, topRight1, bottomRight1, bottomLeft1);
            if (endMesh) return builder.end();
            return null;
        }

    }