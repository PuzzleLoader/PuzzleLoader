package net.appel.mod.info;

import java.util.ArrayList;
import java.util.Map;

public record ModJsonInfo(
        String id,
        String version,
        String name,
        String description,
        String[] authors,
        Map<String, ArrayList<String>> entrypoints,
        Map<String, String> meta,
        String[] mixins,
        Map<String, String> dependencies
) {}
