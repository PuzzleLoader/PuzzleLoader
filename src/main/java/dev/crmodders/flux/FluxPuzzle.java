package dev.crmodders.flux;

import dev.crmodders.flux.events.OnRegisterLanguageEvent;
import dev.crmodders.flux.logging.LoggingAgent;
import dev.crmodders.flux.logging.impl.CustomizableLogger;
import dev.crmodders.flux.logging.impl.SimpleColouredLogger;
import dev.crmodders.puzzle.core.entrypoint.interfaces.PreInitModInitializer;
import org.greenrobot.eventbus.Subscribe;

public class FluxPuzzle implements PreInitModInitializer {
    public static final CustomizableLogger LOGGER = LoggingAgent.getLogger("FluxPuzzle", CustomizableLogger.class);

    public void onPreInit() {
        LOGGER.setMessageColorizer((level, string) -> {
            return string;
        });
        LoggingAgent.setDefaultLoggingClass(SimpleColouredLogger.class);
        LOGGER.info("Flux Puzzle Edition Initialized");
        FluxRegistries.EVENT_BUS.register(this);
    }

    @Subscribe
    public void onEvent(OnRegisterLanguageEvent event) {
        event.registerLanguage(FluxConstants.LanguageEnUs.load());
    }
}
