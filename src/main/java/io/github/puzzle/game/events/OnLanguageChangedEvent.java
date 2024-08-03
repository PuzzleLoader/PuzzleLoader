package io.github.puzzle.game.events;

import io.github.puzzle.core.localization.Language;

public class OnLanguageChangedEvent {

    private final Language newLanguage;

    public OnLanguageChangedEvent(Language newLanguage) {
        this.newLanguage = newLanguage;
    }

    public Language newLanguage() {
        return newLanguage;
    }
}