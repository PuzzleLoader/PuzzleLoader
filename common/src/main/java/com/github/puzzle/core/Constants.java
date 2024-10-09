package com.github.puzzle.core;

import com.github.puzzle.core.loader.launch.PuzzleClassLoader;
import com.github.puzzle.core.loader.meta.Environment;
import org.spongepowered.asm.mixin.MixinEnvironment;

import java.io.IOException;
import java.io.InputStream;
import java.util.function.Supplier;

public class Constants {
    public static final String MOD_ID = "puzzle-loader";
    public static final Environment SIDE = ((Supplier<Environment>) () -> {
        try {
            Class.forName("finalforeach.cosmicreach.ClientSingletons");
        } catch (ClassNotFoundException e) {
            return Environment.SERVER;
        }
        return Environment.CLIENT;
    }).get();

    public static final MixinEnvironment.CompatibilityLevel MIXIN_COMPAT_LEVEL = MixinEnvironment.CompatibilityLevel.JAVA_17;

    public static InputStream getFile(String file) {
        InputStream input = Constants.class.getResourceAsStream(file);
        if (input == null) {
            input = PuzzleClassLoader.class.getClassLoader().getResourceAsStream(file);
        }
        return input;
    }

    public static String getVersion() {
            try {
                InputStream stream = getFile("/assets/puzzle-loader/version.txt");
                String bytez = new String(stream.readAllBytes()).strip();
                stream.close();
                if (!bytez.contains(".")) {
                    return "69.69.69";
                } else return bytez;
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
    }
}
