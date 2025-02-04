package com.github.puzzle.game.engine.blocks.models;

import com.github.puzzle.game.block.generators.model.BlockModelGenerator;
import finalforeach.cosmicreach.util.Identifier;
import org.jetbrains.annotations.ApiStatus;

@ApiStatus.Internal
public class ServerModelGenerator implements IModelGenerator {

    @Override
    public String fromGeneratorAsString(BlockModelGenerator generator) {
        StringBuilder b = new StringBuilder();
        b.append("{");

        if (!generator.vanillaTextures.isEmpty()) {
            b.append("\"textures\": {");

            String[] strings = generator.vanillaTextures.keySet().toArray(new String[0]);
            for (int i = 0; i < strings.length; i++) {
                String name = strings[i];
                Identifier tex = generator.vanillaTextures.get(name);
                b.append("\"" + name + "\":");
                b.append("\"fileName\": {");
                b.append("\"" + tex.toString() + "\"");
                b.append("}");

                if (i < strings.length - 1) b.append(",");
            }

            b.append("}");
            if (!generator.cuboids.isEmpty()) b.append(",");
        }

        if (!generator.cuboids.isEmpty()) {
            b.append("\"cuboids\": [");
            for (int i = 0; i < generator.cuboids.size(); i++) {
                b.append("{");
                BlockModelGenerator.Cuboid c = generator.cuboids.get(i);
                b.append("\"localBounds\": [" + c.x1 + ", " + c.y1 + ", " + c.z1 + ", " + c.x2 + ", " + c.y2 + ", " + c.z2 + "],");
                b.append("\"faces\": {");
                for (int j = 0; j < c.faces.length; j++) {
                    BlockModelGenerator.Cuboid.Face f = c.faces[j];
                    b.append("\"" + f.id + "\": {");
                    b.append("\"uv\": [" + f.u1 + ", " + f.v1 + ", " + f.u2 + ", " + f.v2 + "],");
                    b.append("\"ambientocclusion\": " + f.ambientOcclusion + ",");
                    b.append("\"cullFace\": " + f.cullFace + ",");
                    b.append("\"texture\": \"" + f.texture + "\"");
                    b.append("}");
                }

                if (i < generator.cuboids.size() - 1) b.append(",");
            }
            b.append("]");
        }

        b.append("}");
        return b.toString();
    }

    public static StringBuilder key(StringBuilder b, String k) {
        return b.append("\"").append(k).append("\":");
    }

    public static StringBuilder str(StringBuilder b, String k) {
        return b.append("\"").append(k).append("\"");
    }

    public static StringBuilder list(StringBuilder b, int[] l) {
        for (int i = 0; i < l.length; i++) {
            b.append(l[i]);
            if (i < l.length - 1) b.append(",");
        }

        return b;
    }

    public static StringBuilder list(StringBuilder b, String[] l) {
        for (int i = 0; i < l.length; i++) {
            str(b, l[i]);
            if (i < l.length - 1) b.append(",");
        }

        return b;
    }
}
