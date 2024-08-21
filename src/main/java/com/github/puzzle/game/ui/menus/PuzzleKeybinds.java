package com.github.puzzle.game.ui.menus;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import com.badlogic.gdx.InputProcessor;
import com.badlogic.gdx.math.Vector2;
import com.badlogic.gdx.utils.ScreenUtils;
import com.github.puzzle.game.keybinds.KeybindingProvider;
import com.github.puzzle.game.util.Reflection;
import finalforeach.cosmicreach.Threads;
import finalforeach.cosmicreach.gamestates.GameState;
import finalforeach.cosmicreach.gamestates.KeybindsMenu;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.Keybind;
import finalforeach.cosmicreach.ui.UIElement;
import finalforeach.cosmicreach.ui.UIObject;
import finalforeach.cosmicreach.ui.VerticalAnchor;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicReference;

public class PuzzleKeybinds extends GameState {

    private static KeybindButton activeKeybindButton;
    private static Set<KeybindingProvider> providers = new HashSet<>();
    private static List<UIObject> staticUIObjects = new ArrayList<>();
    public static PuzzleKeybinds inst = new PuzzleKeybinds(null);
    private static boolean keybindJustSet;
    public static int ix = 0;
    public static int iy = 0;
    GameState previousState;

    public static void addNewProvider(KeybindingProvider provider) {
        providers.add(provider);
        for (String key : provider.getKeybindings().keySet()) {
            Keybind value = provider.getKeybindings().get(key);
//            Constructor<KeybindButton> button;
            Threads.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    staticUIObjects.add(KeybindButton.create(inst, key, value, ix, ix, 25, 60));
                }
            });
//            try {
//                button = KeybindButton.class.getConstructor(String.class, Keybind.class, float.class, float.class, float.class, float.class);
//            } catch (NoSuchMethodException e) {
//                throw new RuntimeException(e);
//            }
//            try {
//                staticUIObjects.add(button.newInstance(key, value, ix, ix, 25, 60));
//            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
//                throw new RuntimeException(e);
//            }
            iy += 27;
        }
    }

    public PuzzleKeybinds(final GameState previousState) {
        if (previousState != null) {
            this.previousState = previousState;
            UIElement doneButton = new UIElement(0.0F, -50.0F, 250.0F, 50.0F) {
                public void onClick() {
                    super.onClick();
                    GameState.switchToGameState(previousState);
                }
            };
            this.uiObjects.addAll(staticUIObjects.toArray(new UIObject[0]));
            doneButton.vAnchor = VerticalAnchor.BOTTOM_ALIGNED;
            doneButton.setText(Lang.get("doneButton"));
            doneButton.show();
            this.uiObjects.add(doneButton);
        }
    }

    public void render() {
        super.render();
        if (Gdx.input.isKeyJustPressed(111) && !this.keybindJustSet) {
            switchToGameState(previousState);
        }

        ScreenUtils.clear(0.145F, 0.078F, 0.153F, 1.0F, true);
        Gdx.gl.glEnable(2929);
        Gdx.gl.glDepthFunc(513);
        Gdx.gl.glEnable(2884);
        Gdx.gl.glCullFace(1029);
        Gdx.gl.glEnable(3042);
        Gdx.gl.glBlendFunc(770, 771);
        this.drawUIElements();
        if (this.keybindJustSet) {
            this.keybindJustSet = false;
        }
    }

    public class KeybindButton extends UIElement {
        InputProcessor inputProcessor;
        final Keybind keybind;
        String label;

        public static KeybindButton create(PuzzleKeybinds keybinds, String label, Keybind keybind, float x, float y, float w, float h) {
            AtomicReference<KeybindButton> buttonAtomicReference = new AtomicReference<>();
            Threads.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    try {
                        for (Constructor c : KeybindButton.class.getConstructors()) {
                            System.out.println(c);
                        }
                        buttonAtomicReference.set(KeybindButton.class
                                .getConstructor(PuzzleKeybinds.class, String.class, Keybind.class, float.class, float.class, float.class, float.class)
                                .newInstance(keybinds, label, keybind, x, y, w, h));
                    } catch (NoSuchMethodException | InvocationTargetException | InstantiationException |
                             IllegalAccessException e) {
                        throw new RuntimeException(e);
                    }
                }
            });

            return buttonAtomicReference.get();
//            return new KeybindButton(label, keybind, x, y, w, h);
        }

        public KeybindButton(String label, Keybind keybind, float x, float y, float w, float h) {
            super(x, y, w, h);
            this.keybind = keybind;
            this.label = label;
        }

        public void onCreate() {
            super.onCreate();
        }

        public void deactivate() {
            super.deactivate();
            if (this == PuzzleKeybinds.activeKeybindButton) {
                PuzzleKeybinds.activeKeybindButton = null;
                keybindJustSet = true;
                this.updateText();
                Gdx.input.setInputProcessor(this.inputProcessor);
                this.inputProcessor = null;
            }
        }

        public boolean touchUp(int screenX, int screenY, int pointer, int button) {
            Vector2 m = new Vector2((float)screenX, (float)screenY);
            uiViewport.unproject(m);
            if (PuzzleKeybinds.activeKeybindButton == this) {
                keybindJustSet = true;
                int savedButtonCode = -2 - button;
                this.keybind.setValue(savedButtonCode);
                this.keybind.setDisplayString('\u0000');
                this.deactivate();
                this.updateText();
                return true;
            } else {
                return super.touchUp(screenX, screenY, pointer, button);
            }
        }

        public void onClick() {
            super.onClick();
            if (PuzzleKeybinds.activeKeybindButton == null && !keybindJustSet) {
                PuzzleKeybinds.activeKeybindButton = this;
                keybindJustSet = true;
                this.inputProcessor = Gdx.input.getInputProcessor();
                Gdx.input.setInputProcessor(this);
                this.updateText();
            }
        }

        public boolean keyDown(int keycode) {
            if (this != PuzzleKeybinds.activeKeybindButton) {
                return false;
            } else {
                this.deactivate();
                if (keycode != 111) {
                    this.keybind.setValue(keycode);
                    String qwertyKeyName = Input.Keys.toString(keycode);
                    if (qwertyKeyName.length() > 1 || !Keybind.isPrintableChar(qwertyKeyName.charAt(0))) {
                        this.keybind.setDisplayString('\u0000');
                    }
                }

                this.updateText();
                return true;
            }
        }

        public boolean keyTyped(char character) {
            this.keybind.setDisplayString(character);
            this.updateText();
            return true;
        }

        public void updateText() {
            int key = this.keybind.getValue();
            String keyStr = "[" + this.keybind.getKeyName(key) + "]";
            if (PuzzleKeybinds.activeKeybindButton == this) {
                keyStr = "[???]";
            }

            this.setText(this.label + ": " + keyStr);
        }
    }
    
}
