package net.appel.registry;

import net.appel.mod.info.ModJsonInfo;
import net.appel.mod.interfaces.ModInitializer;
import net.appel.mod.interfaces.ModPreInitializer;
import net.appel.registry.api.FreezingRegistry;

public class BuiltInRegistries {

    public static FreezingRegistry<Class<? extends ModPreInitializer>> PRE_MOD_REGISTRY = FreezingRegistry.create();
    public static FreezingRegistry<Class<? extends ModInitializer>> MOD_REGISTRY = FreezingRegistry.create();
    public static FreezingRegistry<ModJsonInfo> MOD_INFO_REGISTRY = FreezingRegistry.create();

}
