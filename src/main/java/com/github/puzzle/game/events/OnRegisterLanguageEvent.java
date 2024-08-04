package com.github.puzzle.game.events;

import com.github.puzzle.core.localization.ILanguageFile;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.core.localization.files.LanguageFileVersion1;
import com.github.puzzle.core.resources.ResourceLocation;

import java.io.IOException;

public class OnRegisterLanguageEvent {

    public void registerLanguage(ResourceLocation location) {
        ILanguageFile lang = null;
        try {
            lang = LanguageFileVersion1.loadLanguageFromString(location.locate().readString());
            LanguageManager.registerLanguageFile(lang);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
