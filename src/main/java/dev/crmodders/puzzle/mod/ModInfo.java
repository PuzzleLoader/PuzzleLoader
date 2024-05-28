package dev.crmodders.puzzle.mod;

import java.util.Collection;
import java.util.Map;

public record ModInfo(
        String id,
        String version,
        String name,
        String description,
        String[] authors,
        Map<String, Collection<String>> entrypoints,
        Map<String, String> meta,
        String[] mixins,
        Map<String, String> dependencies
) {}
