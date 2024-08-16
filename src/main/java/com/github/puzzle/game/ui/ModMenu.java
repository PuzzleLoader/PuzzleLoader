package com.github.puzzle.game.ui;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.utils.ScreenUtils;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.MainMenu;
import finalforeach.cosmicreach.gamestates.OptionsMenu;
import finalforeach.cosmicreach.gamestates.PauseMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.ui.UIElement;

public class ModMenu extends GameState {


    public ModMenu(GameState currentGameState) {
        this.previousState = currentGameState;
        UIElement doneButton = new UIElement(0.0F, 200.0F, 250.0F, 50.0F) {
            public void onClick() {
                super.onClick();
                ModMenu.this.returnToPrevious();
            }
        };
        doneButton.setText(Lang.get("doneButton"));
        doneButton.show();
        this.uiObjects.add(doneButton);
    }
    private void returnToPrevious() {
        if (this.previousState instanceof MainMenu) {
            switchToGameState(new MainMenu());
        } else if (this.previousState instanceof PauseMenu) {
            switchToGameState(new PauseMenu(((PauseMenu)this.previousState).cursorCaught));
        } else {
            switchToGameState(this.previousState);
        }

    }
    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(Input.Keys.ESCAPE)) {
            this.returnToPrevious();
        }

        ScreenUtils.clear(0.145F, 0.078F, 0.153F, 1.0F, true);
        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        this.drawUIElements();
    }
}
