package com.github.puzzle.game.items.stack;

import com.github.puzzle.game.items.data.DataTagManifest;
import finalforeach.cosmicreach.items.Item;

public interface ITaggedStack {

    Item puzzleLoader$getItem();
    void puzzleLoader$setDataManifest(DataTagManifest tagManifest);
    DataTagManifest puzzleLoader$getDataManifest();

}
