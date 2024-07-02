package dev.crmodders.puzzle.access_manipulator.transformers;

import dev.crmodders.puzzle.access_manipulator.pairs.FieldModifierPair;
import dev.crmodders.puzzle.access_manipulator.pairs.MethodModifierPair;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.List;
import java.util.Map;

public class AccessManipulatorTransformer implements IClassTransformer {

    public static Map<String, ClassModifier> classesToModify;
    public static Map<String, Map<String, FieldModifierPair>> fieldsToModify;
    public static Map<String, List<MethodModifierPair>> methodsToModify;

    @Override
    public byte[] transform(String name, String transformedName, byte[] classBytes) {
        System.out.println("Manipulated Class " + name);
        ManipulatorClassWriter writer = new ManipulatorClassWriter(transformedName, classBytes);
        return writer.applyManipulations();
    }

}
