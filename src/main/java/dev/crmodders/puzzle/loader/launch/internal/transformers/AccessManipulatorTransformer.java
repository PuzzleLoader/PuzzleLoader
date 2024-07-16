package dev.crmodders.puzzle.loader.launch.internal.transformers;

import dev.crmodders.puzzle.access_manipulators.AccessManipulators;
import dev.crmodders.puzzle.loader.launch.Piece;
import dev.crmodders.puzzle.loader.mod.ModContainer;
import dev.crmodders.puzzle.loader.mod.ModLocator;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.List;

public class AccessManipulatorTransformer implements IClassTransformer {

    public AccessManipulatorTransformer() {
        ModLocator.getMods(List.of(Piece.classLoader.getURLs()));
        Piece.provider.addBuiltinMods();

        readAccessManipulators();
    }

    public void doPath0(String path) {
        if (path != null && !path.isEmpty()) {
            AccessManipulators.registerModifierFile(path);
        }
    }

    public void readAccessManipulators() {
        for (ModContainer container : ModLocator.locatedMods.values()) {
                doPath0(container.INFO.AccessWidener);
                doPath0(container.INFO.AccessManipulator);
                doPath0(container.INFO.AccessTransformer);
        }
    }

    @Override
    public byte[] transform(String name, String tname, byte[] classBytes) {
        return AccessManipulators.transformClass(tname, classBytes);
    }

}
