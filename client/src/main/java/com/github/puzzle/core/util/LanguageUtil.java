package com.github.puzzle.core.util;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.puzzle.core.localization.LanguageManager;
import com.github.puzzle.game.ui.font.TranslationParameters;

public class LanguageUtil {

    public static void updateLabel(Label label, TranslationParameters parameters) {
        if(parameters.attachedProgressBar != null) {
            ProgressBar bar = parameters.attachedProgressBar;
            String text;
            if(bar.getStepSize() == 1.0F) {
                text = LanguageManager.format(parameters.key, (int) bar.getValue(), (int) bar.getMaxValue());
            } else {
                text = LanguageManager.format(parameters.key, bar.getValue(), bar.getMaxValue());
            }
            label.setText(text);
            label.invalidate();
        } else {
            label.setText(LanguageManager.string(parameters.key));
            label.invalidate();
        }
    }

    public static void updateLabels(Stage stage) {
        for(Actor actor : stage.getActors()) {
            if(actor instanceof Label label && label.getUserObject() instanceof TranslationParameters params) {
                updateLabel(label, params);
            }
        }
    }

}
