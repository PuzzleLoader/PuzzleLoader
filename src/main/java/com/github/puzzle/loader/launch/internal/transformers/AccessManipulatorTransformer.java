package com.github.puzzle.loader.launch.internal.transformers;

import com.github.puzzle.access_manipulators.AccessManipulators;
import com.github.puzzle.loader.launch.Piece;
import com.github.puzzle.loader.mod.ModContainer;
import com.github.puzzle.loader.mod.ModLocator;
import net.minecraft.launchwrapper.IClassTransformer;

public class AccessManipulatorTransformer implements IClassTransformer {
    public AccessManipulatorTransformer() {
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
