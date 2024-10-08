package com.github.puzzle.game.keybindings;

import finalforeach.cosmicreach.settings.BooleanSetting;
import finalforeach.cosmicreach.settings.FloatSetting;

public class PuzzleControlSettings {
    public static final BooleanSetting invertedMouse = new BooleanSetting("invertMouse", false);
    public static final FloatSetting mouseSensitivity = new FloatSetting("mouseSensitivity", 1.0F);
    public static final PuzzleKeybind keyForward = PuzzleKeybind.fromDefaultKey("forward", 51);
    public static final PuzzleKeybind keyBackward = PuzzleKeybind.fromDefaultKey("backward", 47);
    public static final PuzzleKeybind keyLeft = PuzzleKeybind.fromDefaultKey("left", 29);
    public static final PuzzleKeybind keyRight = PuzzleKeybind.fromDefaultKey("right", 32);
    public static final PuzzleKeybind keyJump = PuzzleKeybind.fromDefaultKey("jump", 62);
    public static final PuzzleKeybind keyCrouch = PuzzleKeybind.fromDefaultKey("crouch", 59);
    public static final PuzzleKeybind keySprint = PuzzleKeybind.fromDefaultKey("sprint", 129);
    public static final PuzzleKeybind keyProne = PuzzleKeybind.fromDefaultKey("prone", 54);
    public static final PuzzleKeybind keyInventory = PuzzleKeybind.fromDefaultKey("openInventory", 33);
    public static final PuzzleKeybind keyDropItem = PuzzleKeybind.fromDefaultKey("dropItem", 45);
    public static final PuzzleKeybind keySwapGroupItem = PuzzleKeybind.fromDefaultKey("swapGroupItem", 68);
    public static int[] keyHotbarArray = new int[]{8, 9, 10, 11, 12, 13, 14, 15, 16, 7};
    public static final PuzzleKeybind keyHideUI = PuzzleKeybind.fromDefaultKey("hideUI", 131);
    public static final PuzzleKeybind keyScreenshot = PuzzleKeybind.fromDefaultKey("screenshot", 132);
    public static final PuzzleKeybind keyDebugInfo = PuzzleKeybind.fromDefaultKey("debugInfo", 133);
    public static final PuzzleKeybind keyDebugNoClip = PuzzleKeybind.fromDefaultKey("debugNoClip", 134);
    public static final PuzzleKeybind keyDebugReloadShaders = PuzzleKeybind.fromDefaultKey("reloadShaders", 136);
    public static final PuzzleKeybind keyFullscreen = PuzzleKeybind.fromDefaultKeyNeverMouse("fullscreen", 141);
    public static final PuzzleKeybind keyAttackBreak = PuzzleKeybind.fromDefaultMouse("attackBreak", 0);
    public static final PuzzleKeybind keyPickBlock = PuzzleKeybind.fromDefaultMouse("pickBlock", 2);
    public static final PuzzleKeybind keyUsePlace = PuzzleKeybind.fromDefaultMouse("usePlace", 1);
    public static final PuzzleKeybind keyChat = PuzzleKeybind.fromDefaultKey("chat", 48);

    public PuzzleControlSettings() {
    }
}
