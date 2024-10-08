package com.github.puzzle.game.ui.font;

import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.puzzle.core.localization.TranslationKey;

public class TranslationParameters {

    public static class Builder {
        private final TranslationParameters params;

        public Builder(TranslationKey key) {
            params = new TranslationParameters(key);
        }

        public Builder withProgressBar(ProgressBar bar) {
            params.attachedProgressBar = bar;
            return this;
        }

        public TranslationParameters build() {
            return params;
        }

    }

    public final TranslationKey key;
    public ProgressBar attachedProgressBar;

    private TranslationParameters(TranslationKey key) {
        this.key = key;
    }

}
