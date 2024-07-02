package dev.crmodders.puzzle.access_manipulator.pairs;

import dev.crmodders.puzzle.access_manipulator.transformers.ClassModifier;

public class MethodModifierPair {

    public String methodName;
    public String methodDesc;
    public String className;
    public ClassModifier modifier;

    public MethodModifierPair(
            String methodName,
            String methodDesc,
            String className,
            ClassModifier modifier
    ) {
        this.methodName = methodName;
        this.methodDesc = methodDesc;
        this.className = className;
        this.modifier = modifier;
    }

}
