package com.github.puzzle.core;

import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.game.Globals;
import com.github.puzzle.game.engine.items.PuzzleItemModel;
import com.github.puzzle.game.events.OnLoadAssetsEvent;
import com.github.puzzle.game.events.OnPreLoadAssetsEvent;
import com.github.puzzle.game.items.IModItem;
import com.github.puzzle.game.items.NullStick;
import com.github.puzzle.game.mixins.accessors.ItemRenderAccessor;
import com.github.puzzle.loader.entrypoint.interfaces.ModInitializer;
import com.github.puzzle.loader.entrypoint.interfaces.PreModInitializer;
import com.github.puzzle.loader.launch.PuzzleClassLoader;
import finalforeach.cosmicreach.items.ItemBlock;
import finalforeach.cosmicreach.items.ItemModelBlock;
import finalforeach.cosmicreach.rendering.items.ItemRenderer;
import org.greenrobot.eventbus.Subscribe;

import java.io.IOException;
import java.io.InputStream;
import java.lang.ref.WeakReference;

import static finalforeach.cosmicreach.rendering.items.ItemRenderer.registerItemModelCreator;

public class Puzzle implements PreModInitializer, ModInitializer {
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
        try {
            ILanguageFile lang = LanguageFileVersion1.loadLanguageFromString(new String(getFile(Globals.LanguageEnUs.toPath()).readAllBytes()));
            LanguageManager.registerLanguageFile(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onInit() {
        IModItem.registerItem(new NullStick());
        ItemBlock.allItems.values().forEach((v)-> {
            if(v instanceof IModItem item)
                ItemRenderAccessor.getRefMap().put(v,new WeakReference<>(v));

        });
        registerItemModelCreator(IModItem.class, (modItem) -> {
            return new PuzzleItemModel(modItem.get());
        });
    }
}
