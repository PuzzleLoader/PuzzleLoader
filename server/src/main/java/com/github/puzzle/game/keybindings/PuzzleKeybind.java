package com.github.puzzle.game.keybindings;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.CharSetting;
import finalforeach.cosmicreach.settings.IntSetting;

import java.util.HashMap;

public class PuzzleKeybind extends IntSetting {
    public static HashMap<String, PuzzleKeybind> allKeybinds = new HashMap();
    public static final PuzzleKeybind MISSINGKEYBIND = new PuzzleKeybind("MISSINGKEYBIND", -1, true) {
        public String getKeyName() {
            return "MISSINGKEYBIND";
        }
    };
    String key;
    CharSetting charSetting;
    boolean allowMouse;

    private PuzzleKeybind(String key, int defaultValue, boolean allowMouse) {
        super("keybind_" + key, defaultValue);
        allKeybinds.put(key, this);
        this.key = key;
        this.allowMouse = allowMouse;
        this.charSetting = new CharSetting("keybindDisplay_" + key, '\u0000');
    }

    public static PuzzleKeybind fromDefaultKey(String key, int defaultKeyValue) {
        return new PuzzleKeybind(key, defaultKeyValue, true);
    }

    public static PuzzleKeybind fromDefaultKeyNeverMouse(String key, int defaultKeyValue) {
        return new PuzzleKeybind(key, defaultKeyValue, false);
    }

    public static PuzzleKeybind fromDefaultMouse(String key, int defaultButtonValue) {
        return new PuzzleKeybind(key, -2 - defaultButtonValue, true);
    }

    public boolean isPressed() {
        return this.isMouseButton() ? Gdx.input.isButtonPressed(this.getMouseButtonCode()) : Gdx.input.isKeyPressed(this.getValue());
    }

    public boolean isJustPressed() {
        return this.isMouseButton() ? Gdx.input.isButtonJustPressed(this.getMouseButtonCode()) : Gdx.input.isKeyJustPressed(this.getValue());
    }

    public boolean isMouseButton() {
        if (!this.allowMouse) {
            return false;
        } else {
            int val = this.getValue();
            return val < -1;
        }
    }

    public int getMouseButtonCode() {
        return this.isMouseButton() ? -(this.getValue() + 2) : -1;
    }

    public String getKeyName() {
        if (this.isMouseButton()) {
            switch (this.getMouseButtonCode()) {
                case 0 -> {
                    return Lang.get("mouseLeftButton");
                }
                case 1 -> {
                    return Lang.get("mouseRightButton");
                }
                case 2 -> {
                    return Lang.get("mouseMiddleButton");
                }
                case 3 -> {
                    return Lang.get("mouseBackButton");
                }
                case 4 -> {
                    return Lang.get("mouseForwardButton");
                }
            }
        }

        return Input.Keys.toString(this.getValue());
    }

    public String getKeyName(int key) {
        char c = this.charSetting.getValue();
        if (c != 0) {
            return ("" + c).toUpperCase();
        } else {
            try {
                if (this.isMouseButton()) {
                    switch (this.getMouseButtonCode()) {
                        case 0 -> {
                            return Lang.get("mouseLeftButton");
                        }
                        case 1 -> {
                            return Lang.get("mouseRightButton");
                        }
                        case 2 -> {
                            return Lang.get("mouseMiddleButton");
                        }
                        case 3 -> {
                            return Lang.get("mouseBackButton");
                        }
                        case 4 -> {
                            return Lang.get("mouseForwardButton");
                        }
                    }
                }

                return Input.Keys.toString(key);
            } catch (Exception var4) {
                return "???";
            }
        }
    }

    public void setDisplayString(char character) {
        if (isPrintableChar(character) && character != ' ') {
            this.charSetting.setValue(character);
        } else {
            this.charSetting.setValue('\u0000');
        }

    }

    public static boolean isPrintableChar(char c) {
        Character.UnicodeBlock unicodeBlock = Character.UnicodeBlock.of(c);
        return !Character.isISOControl(c) && c != '\uffff' && unicodeBlock != null && unicodeBlock != Character.UnicodeBlock.SPECIALS;
    }

    public char getDisplayChar() {
        return this.charSetting.getValue();
    }
}
