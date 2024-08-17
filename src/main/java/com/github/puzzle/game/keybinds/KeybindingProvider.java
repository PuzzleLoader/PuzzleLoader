package com.github.puzzle.game.keybinds;

import com.github.puzzle.loader.mod.ModContainer;
import finalforeach.cosmicreach.settings.Keybind;

import java.util.HashMap;

public class KeybindingProvider {

    final String sectionName;
    final HashMap<String, Keybind> keybindings = new HashMap<>();

    final ModContainer container;

    public KeybindingProvider(ModContainer container) {
        sectionName = container.NAME == null ? container.ID : container.NAME;
        this.container = container;
    }

    public String getSectionName() {
        return sectionName;
    }

    public ModContainer getContainer() {
        return container;
    }

    public HashMap<String, Keybind> getKeybindings() {
        return keybindings;
    }

    public Keybind addKeybind(String name, Keybind keybind) {
        keybindings.put(name, keybind);
        return keybind;
    }

}
