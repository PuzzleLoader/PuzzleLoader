package com.github.puzzle.game;

import com.github.puzzle.core.Constants;
import com.github.puzzle.core.registries.GenericRegistry;
import com.github.puzzle.core.registries.IRegistry;
import com.github.puzzle.game.tags.Tag;
import finalforeach.cosmicreach.util.Identifier;

public class CommonRegistries {

    public static IRegistry<Tag> TAGS = new GenericRegistry<>(Identifier.of(Constants.MOD_ID, "tags"));

}
