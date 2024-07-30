package dev.crmodders.puzzle.game.events;

import dev.crmodders.puzzle.core.localization.Language;

public class OnLanguageChangedEvent {

    private final Language newLanguage;

    public OnLanguageChangedEvent(Language newLanguage) {
        this.newLanguage = newLanguage;
    }

    public Language newLanguage() {
        return newLanguage;
    }
}