package com.github.puzzle.core.loader.meta;

import com.github.puzzle.core.loader.meta.parser.SideRequire;
import com.github.puzzle.core.loader.meta.parser.mod.ModJsonV2;
import com.llamalad7.mixinextras.lib.apache.commons.tuple.Pair;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class ModInfoV2Builder extends ModInfoV1Builder {

    protected SideRequire require = SideRequire.BOTH_REQUIRED;

    protected ModInfoV2Builder() {
    }

    public ModInfoV2Builder setSideRequirements(SideRequire require) {
        this.require = require;
        return this;
    }

    public ModInfo build() {
        return new ModInfo(new ModJsonV2(
                makeName(),
                makeId(),
                version != null ? version.toString() : "1.0.0",
                description,
                authors.toArray(new String[0]),
                mixins.toArray(new Pair[0]),
                accessTransformers.toArray(new String[0]),
                meta,
                entrypoints,
                depends,
                require
        ));
    }

    @Contract(" -> new")
    public static @NotNull ModInfoV2Builder New() {
        return new ModInfoV2Builder();
    }

}
