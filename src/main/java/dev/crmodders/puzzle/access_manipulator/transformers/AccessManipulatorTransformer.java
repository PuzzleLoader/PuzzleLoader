package dev.crmodders.puzzle.access_manipulator.transformers;

import dev.crmodders.puzzle.access_manipulator.AccessWidenerReader;
import dev.crmodders.puzzle.access_manipulator.ManipulatorReader;
import dev.crmodders.puzzle.access_manipulator.pairs.FieldModifierPair;
import dev.crmodders.puzzle.access_manipulator.pairs.MethodModifierPair;
import dev.crmodders.puzzle.core.launch.Piece;
import dev.crmodders.puzzle.core.mod.ModContainer;
import dev.crmodders.puzzle.core.mod.ModLocator;
import net.minecraft.launchwrapper.IClassTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Consumer;

public class AccessManipulatorTransformer implements IClassTransformer {

    public static Map<String, ClassModifier> classesToModify = new HashMap<>();
    public static Map<String, Map<String, FieldModifierPair>> fieldsToModify = new HashMap<>();
    public static Map<String, List<MethodModifierPair>> methodsToModify = new HashMap<>();

    public AccessManipulatorTransformer() {
        ModLocator.getMods(List.of(Piece.classLoader.getURLs()));
        ModLocator.AddBuiltinMods(Piece.provider);

        readAccessManipulators();
    }

    public void doPath0(ModContainer container, String path, Consumer<String> reader) {
        if (path != null && !path.isEmpty()) {
            try {
                InputStream stream = container.JAR.getInputStream(container.JAR.getEntry(path));
                reader.accept(new String(stream.readAllBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void doPath1(String path, Consumer<String> reader) {
        if (path != null && !path.isEmpty()) {
            try {
                InputStream stream = this.getClass().getClassLoader().getResourceAsStream(path);
                reader.accept(new String(stream.readAllBytes()));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void readAccessManipulators() {
        for (ModContainer container : ModLocator.LocatedMods.values()) {
            if (container.JAR != null) {
                doPath0(container, container.JSON_INFO.accessWidener(), AccessWidenerReader::read);
                doPath0(container, container.JSON_INFO.accessManipulator(), ManipulatorReader::read);
            } else {
                doPath1(container.JSON_INFO.accessWidener(), AccessWidenerReader::read);
                doPath1(container.JSON_INFO.accessManipulator(), ManipulatorReader::read);
            }
        }
    }

    @Override
    public byte[] transform(String name, String tname, byte[] classBytes) {
        String transformedName = name.replaceAll("\\.", "/");
        if (classesToModify.containsKey(transformedName) || fieldsToModify.containsKey(transformedName) || methodsToModify.containsKey(transformedName)) {
            System.out.println("Manipulated Class " + name);
            ManipulatorClassWriter writer = new ManipulatorClassWriter(transformedName, classBytes);
            return writer.applyManipulations();
        }
        return classBytes;
    }

}
