package dev.crmodders.puzzle.loader.mod;

import java.util.*;

public record ModJsonInfo(
        String id,
        String version,
        String name,
        String description,
        String[] authors,
        Map<String, Collection<String>> entrypoints,
        Map<String, Object> meta,
        String[] mixins,
        Map<String, String> dependencies,
        Map<String, String> optional,
        String accessManipulator,
        String accessTransformer,
        String accessWidener
) {}
