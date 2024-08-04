package com.github.puzzle.game.events;

import com.github.puzzle.core.localization.Language;

public class OnLanguageChangedEvent {

    private final Language newLanguage;

    public OnLanguageChangedEvent(Language newLanguage) {
        this.newLanguage = newLanguage;
    }

    public Language newLanguage() {
        return newLanguage;
    }
}