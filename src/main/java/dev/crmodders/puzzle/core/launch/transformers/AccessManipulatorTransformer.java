package dev.crmodders.puzzle.core.launch.transformers;

import dev.crmodders.puzzle.access_manipulators.AccessManipulators;
import dev.crmodders.puzzle.core.launch.Piece;
import dev.crmodders.puzzle.core.mod.ModContainer;
import dev.crmodders.puzzle.core.mod.ModLocator;
import net.minecraft.launchwrapper.IClassTransformer;

import java.util.List;

public class AccessManipulatorTransformer implements IClassTransformer {

    public AccessManipulatorTransformer() {
        ModLocator.getMods(List.of(Piece.classLoader.getURLs()));
        ModLocator.AddBuiltinMods(Piece.provider);

        readAccessManipulators();
    }

    public void doPath0(String path) {
        if (path != null && !path.isEmpty()) {
            AccessManipulators.registerModifierFile(path);
        }
    }

    public void readAccessManipulators() {
        for (ModContainer container : ModLocator.LocatedMods.values()) {
                doPath0(container.JSON_INFO.accessWidener());
                doPath0(container.JSON_INFO.accessManipulator());
                doPath0(container.JSON_INFO.accessTransformer());
        }
    }

    @Override
    public byte[] transform(String name, String tname, byte[] classBytes) {
        return AccessManipulators.transformClass(tname, classBytes);
    }

}