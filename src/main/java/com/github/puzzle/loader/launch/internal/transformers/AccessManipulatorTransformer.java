package com.github.puzzle.loader.launch.internal.transformers;

import com.github.puzzle.access_manipulators.AccessManipulators;
import com.github.puzzle.access_manipulators.readers.api.IAccessModifierReader;
import com.github.puzzle.loader.launch.Piece;
import com.github.puzzle.loader.mod.ModContainer;
import com.github.puzzle.loader.mod.ModLocator;
import net.minecraft.launchwrapper.IClassTransformer;

import java.io.IOException;
import java.io.InputStream;
import java.util.zip.ZipFile;

import static com.github.puzzle.access_manipulators.AccessManipulators.readerMap;

public class AccessManipulatorTransformer implements IClassTransformer {
    public AccessManipulatorTransformer() {
        Piece.provider.addBuiltinMods();

        readAccessManipulators();
    }

    public void registerFile(String path, String ext, InputStream stream) {
        IAccessModifierReader reader = readerMap.get(ext);
        if (reader == null) {
            throw new RuntimeException("Unsupported Access Modifier Extension \"." + ext + "\"");
        } else {
            if (stream != null) {
                try {
                    reader.read(new String(stream.readAllBytes()));
                } catch (IOException var6) {
                    throw new RuntimeException(var6);
                }
            } else throw new RuntimeException("Access Modifier \"" + path + "\" does not exist.");
        }
    }

    public void doPath0(ZipFile file, String path) {
        if (path != null && !path.isEmpty()) {
            String fileExt = path.split("\\.")[path.split("\\.").length - 1].toLowerCase();
            try {
                registerFile(path, fileExt, file.getInputStream(file.getEntry(path)));
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public void doPath1(String path) {
        if (path != null && !path.isEmpty()) {
            AccessManipulators.registerModifierFile(path);
        }
    }

    public void readAccessManipulators() {
        for (ModContainer container : ModLocator.locatedMods.values()) {
            if (container.JAR != null) {
                doPath0(container.JAR, container.INFO.AccessWidener);
                doPath0(container.JAR, container.INFO.AccessManipulator);
                doPath0(container.JAR, container.INFO.AccessTransformer);
            } else {
                doPath1(container.INFO.AccessWidener);
                doPath1(container.INFO.AccessManipulator);
                doPath1(container.INFO.AccessTransformer);
            }

        }
    }

    @Override
    public byte[] transform(String name, String tname, byte[] classBytes) {
        return AccessManipulators.transformClass(tname, classBytes);
    }
}
