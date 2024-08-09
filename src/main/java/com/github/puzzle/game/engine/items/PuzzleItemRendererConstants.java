package com.github.puzzle.game.engine.items;

import com.badlogic.gdx.graphics.Camera;
import com.badlogic.gdx.graphics.OrthographicCamera;

public interface PuzzleItemRendererConstants {

    Camera itemCam2 = new OrthographicCamera(100F, 100F);

    static void initCamera() {
        itemCam2.position.set(0, 0, 2);
        itemCam2.lookAt(0, 0, 0);
        ((OrthographicCamera) itemCam2).zoom = 0.027F;
        itemCam2.update();
    }

}
