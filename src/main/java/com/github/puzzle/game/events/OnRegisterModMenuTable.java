package com.github.puzzle.game.events;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ArrayMap;

public class OnRegisterModMenuTable {

    private final ArrayMap<String, Table> customTables;

    public OnRegisterModMenuTable(ArrayMap<String, Table> customTable) {
        this.customTables = customTable;
    }

    public void registerTable(String id, Table table) {
        customTables.put(id, table);
    }
}
