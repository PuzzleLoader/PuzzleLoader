package dev.crmodders.puzzle.access_manipulator;

import dev.crmodders.puzzle.access_manipulator.pairs.FieldModifierPair;
import dev.crmodders.puzzle.access_manipulator.pairs.MethodModifierPair;
import dev.crmodders.puzzle.access_manipulator.transformers.ClassModifier;

import java.util.Map;

public record Manipulator(
        Map<String, ClassModifier> classesToModify,
        Map<String, FieldModifierPair> fieldsToModify,
        Map<String, MethodModifierPair> methodsToModify
) {
}
