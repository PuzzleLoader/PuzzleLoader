package dev.crmodders.puzzle.access_manipulator.pairs;

import dev.crmodders.puzzle.access_manipulator.transformers.ClassModifier;

public class FieldModifierPair {

    public String fieldName;
    public String className;
    public ClassModifier modifier;

    public FieldModifierPair(
            String fieldName,
            String className,
            ClassModifier modifier
    ) {
        this.fieldName = fieldName;
        this.className = className;
        this.modifier = modifier;
    }

}
