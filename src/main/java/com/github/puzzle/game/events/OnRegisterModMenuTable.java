package com.github.puzzle.game.events;

import com.badlogic.gdx.scenes.scene2d.ui.Table;
import com.badlogic.gdx.utils.ArrayMap;

public class OnRegisterModMenuTable {

    private final ArrayMap<String, Table> customTable;

    public OnRegisterModMenuTable(ArrayMap<String, Table> customTable) {
        this.customTable = customTable;
    }

    public void registerTable(String id, Table table) {

        customTable.put(id, table);
    }
}
