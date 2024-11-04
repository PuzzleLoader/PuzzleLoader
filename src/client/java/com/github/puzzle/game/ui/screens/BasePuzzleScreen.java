package com.github.puzzle.game.ui.screens;

import finalforeach.cosmicreach.items.ISlotContainerParent;
import finalforeach.cosmicreach.items.screens.BaseItemScreen;

public class BasePuzzleScreen extends BaseItemScreen {

    public BasePuzzleScreen(ISlotContainerParent parent) {
        super(parent);
    }

    @Override
    public void drawItems() {
        onRender();
        super.drawItems();
    }

    public void onRender() {
    }

}
