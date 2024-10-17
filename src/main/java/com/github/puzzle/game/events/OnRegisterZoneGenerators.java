package com.github.puzzle.game.events;

import finalforeach.cosmicreach.worldgen.ZoneGenerator;

import java.util.List;
import java.util.function.Supplier;

public class OnRegisterZoneGenerators {

    private final List<Supplier<ZoneGenerator>> zoneFactories;

    public OnRegisterZoneGenerators(List<Supplier<ZoneGenerator>> zoneFactories) {
        this.zoneFactories = zoneFactories;
    }

    public void registerGenerator(Supplier<ZoneGenerator> zoneGenerator) {
        zoneFactories.add(zoneGenerator);
    }

}
