package com.github.puzzle.game;

import com.github.puzzle.core.loader.meta.Env;
import com.github.puzzle.core.loader.meta.EnvType;
import finalforeach.cosmicreach.settings.BooleanSetting;

@Env(EnvType.CLIENT)
public class ClientGlobals {

    public static final BooleanSetting shouldUseRedesign = new BooleanSetting("shouldUseRedesign", true);

}