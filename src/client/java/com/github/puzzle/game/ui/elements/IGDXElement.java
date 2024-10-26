package com.github.puzzle.game.ui.elements;

import com.badlogic.gdx.scenes.scene2d.InputEvent;

public interface IGDXElement {

    void onClick(InputEvent event, float mouseX, float mouseY);
    void onHover(float deltaTime, float mouseX, float mouseY);

    boolean isHoveringOver();

}
