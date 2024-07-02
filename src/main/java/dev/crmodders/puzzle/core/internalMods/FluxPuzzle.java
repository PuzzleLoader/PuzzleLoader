//package dev.crmodders.puzzle.core.internalMods;
//
//import dev.crmodders.flux.FluxConstants;
//import dev.crmodders.flux.FluxRegistries;
//import dev.crmodders.flux.assets.FluxGameAssetLoader;
//import dev.crmodders.flux.events.OnPreLoadAssetsEvent;
//import dev.crmodders.flux.localization.ILanguageFile;
//import dev.crmodders.flux.localization.LanguageManager;
//import dev.crmodders.flux.localization.files.LanguageFileVersion1;
//import dev.crmodders.puzzle.core.entrypoint.interfaces.PreInitModInitializer;
//import org.greenrobot.eventbus.Subscribe;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//
//public class FluxPuzzle implements PreInitModInitializer {
//    public static final Logger LOGGER = LoggerFactory.getLogger("FluxAPI");
//
//    public void onPreInit() {
//        LOGGER.info("Flux on Puzzle Initialized");
//        FluxRegistries.EVENT_BUS.register(this);
//    }
//
//    @Subscribe
//    public void onEvent(OnPreLoadAssetsEvent event) {
//        ILanguageFile lang = FluxGameAssetLoader.LOADER.loadResourceSync(FluxConstants.LanguageEnUs, LanguageFileVersion1.class);
//        LanguageManager.registerLanguageFile(lang);
//    }
//}
