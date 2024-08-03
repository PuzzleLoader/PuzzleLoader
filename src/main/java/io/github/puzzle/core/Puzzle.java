package io.github.puzzle.core;

import io.github.puzzle.core.localization.ILanguageFile;
import io.github.puzzle.core.localization.LanguageManager;
import io.github.puzzle.core.localization.files.LanguageFileVersion1;
import io.github.puzzle.game.Globals;
import io.github.puzzle.game.events.OnPreLoadAssetsEvent;
import io.github.puzzle.loader.entrypoint.interfaces.PreModInitializer;
import io.github.puzzle.loader.launch.PuzzleClassLoader;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;

public class Puzzle implements PreModInitializer {
    public static final String MOD_ID = "puzzle-loader";

    public static final String VERSION;

    static {
        try {
            InputStream stream = getFile("assets/puzzle-loader/version.txt");
            VERSION = new String(stream.readAllBytes()).strip();
            stream.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static InputStream getFile(String file) {
        InputStream input = Puzzle.class.getResourceAsStream(file);
        if (input == null) {
            input = PuzzleClassLoader.class.getClassLoader().getResourceAsStream(file);
        }
        return input;
    }

    @Override
    public void onPreInit() {
        PuzzleRegistries.EVENT_BUS.register(this);

//        ILanguageFile lang = LOADER.loadResourceSync(Globals.LanguageEnUs, LanguageFileVersion1.class);
        try {
            ILanguageFile lang = LanguageFileVersion1.loadLanguageFromString(new String(getFile(Globals.LanguageEnUs.toPath()).readAllBytes()));
            LanguageManager.registerLanguageFile(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Subscribe
    public void onEvent(OnPreLoadAssetsEvent event) {

    }
}
