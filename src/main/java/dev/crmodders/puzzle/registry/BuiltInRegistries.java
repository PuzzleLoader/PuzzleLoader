package dev.crmodders.puzzle.registry;

import dev.crmodders.puzzle.mod.ModInfo;
import dev.crmodders.puzzle.entrypoint.interfaces.ModInitializer;
import dev.crmodders.puzzle.entrypoint.interfaces.ModPreInitializer;
import dev.crmodders.puzzle.registry.api.FreezingRegistry;

public class BuiltInRegistries {

    public static FreezingRegistry<Class<? extends ModPreInitializer>> PRE_MOD_REGISTRY = FreezingRegistry.create();
    public static FreezingRegistry<Class<? extends ModInitializer>> MOD_REGISTRY = FreezingRegistry.create();
    public static FreezingRegistry<ModInfo> MOD_INFO_REGISTRY = FreezingRegistry.create();

}
