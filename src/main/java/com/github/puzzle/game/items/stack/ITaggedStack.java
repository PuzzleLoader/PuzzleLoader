package com.github.puzzle.game.items.stack;

import com.github.puzzle.game.items.data.DataTagManifest;

public interface ITaggedStack {

    void puzzleLoader$setDataManifest(DataTagManifest tagManifest);
    DataTagManifest puzzleLoader$getDataManifest();

}
