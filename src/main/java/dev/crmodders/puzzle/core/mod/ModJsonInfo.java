package dev.crmodders.puzzle.core.mod;

import java.util.Collection;
import java.util.Map;

public record ModJsonInfo(
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
