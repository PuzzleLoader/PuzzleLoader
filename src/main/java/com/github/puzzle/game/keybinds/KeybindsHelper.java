package com.github.puzzle.game.keybinds;

import com.github.puzzle.game.ui.menus.PuzzleKeybinds;
import com.github.puzzle.loader.mod.ModContainer;
import com.github.puzzle.loader.mod.ModLocator;

public class KeybindsHelper {

    public static KeybindingProvider getProviderForModId(String modId) {
        ModContainer container = ModLocator.locatedMods.get(modId);
        if (container == null) return null;

        KeybindingProvider provider = new KeybindingProvider(container);
        PuzzleKeybinds.addNewProvider(provider);
        return provider;
    }


}
