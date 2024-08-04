package com.github.puzzle.core;

import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.game.Globals;
import com.github.puzzle.game.events.OnPreLoadAssetsEvent;
import com.github.puzzle.loader.entrypoint.interfaces.PreModInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;

public class Puzzle implements PreModInitializer {
    public static final String MOD_ID = "puzzle-loader";

    public static final String VERSION;

    static {
        try {
            InputStream stream = getFile("assets/puzzle-loader/version.txt");
            String bytez = new String(stream.readAllBytes()).strip();
            stream.close();
            if (!bytez.contains(".")) {
                VERSION = "69.69.69";
            } else VERSION = bytez;
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
