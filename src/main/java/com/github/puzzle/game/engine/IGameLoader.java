package com.github.puzzle.game.engine;

import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import com.github.puzzle.core.localization.TranslationKey;
import com.github.puzzle.game.engine.blocks.IBlockLoader;

public interface IGameLoader {

    void addStage(LoadStage stage);

    void setupProgressBar(ProgressBar bar, int range);

    void setupProgressBar(ProgressBar bar, int range, TranslationKey key);

    void setupProgressBar(ProgressBar bar, int range, String str);

    void incrementProgress(ProgressBar bar);

    void incrementProgress(ProgressBar bar, TranslationKey key);

    void incrementProgress(ProgressBar bar, String str);

    boolean isServer();

    IBlockLoader getBlockLoader();

    ProgressBar getProgressBar1();
    ProgressBar getProgressBar2();
    ProgressBar getProgressBar3();

    Label getProgressBarText1();
    Label getProgressBarText2();
    Label getProgressBarText3();
}
