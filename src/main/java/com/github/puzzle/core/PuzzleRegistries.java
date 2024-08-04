package com.github.puzzle.core;

import com.github.puzzle.core.localization.LanguageRegistry;
import com.github.puzzle.core.registries.GenericRegistry;
import com.github.puzzle.core.registries.IRegistry;
import com.github.puzzle.game.block.IModBlock;
import com.github.puzzle.game.block.PuzzleBlockAction;
import com.github.puzzle.game.factories.IFactory;
import com.github.puzzle.game.loot.PuzzleLootTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

import static com.github.puzzle.core.Puzzle.MOD_ID;

/**
 * List of all available registries
 * as public static final fields
 */
public class PuzzleRegistries {

    public static final EventBus EVENT_BUS = EventBus.builder().sendNoSubscriberEvent(false).logNoSubscriberMessages(false).logger(new SLF4JEventBusLogger()).build();

    public static final IRegistry<PuzzleLootTable> LOOT_TABLES = new GenericRegistry<>(Identifier.of(MOD_ID, "loot_tables"));
    public static final LanguageRegistry LANGUAGES = new LanguageRegistry(Identifier.of(MOD_ID, "languages"));
    public static final IRegistry<IFactory<PuzzleBlockAction>> BLOCK_EVENT_ACTION_FACTORIES = new GenericRegistry<>(new Identifier(MOD_ID, "block_event_actions_factories"));

    public static final IRegistry<IModBlock> BLOCKS = new GenericRegistry<>(new Identifier(MOD_ID, "blocks"));

    public static final IRegistry<Runnable> BLOCK_MODEL_FINALIZERS = new GenericRegistry<>(new Identifier(MOD_ID, "block_model_finalizers"));
    public static final IRegistry<Runnable> BLOCK_FINALIZERS = new GenericRegistry<>(new Identifier(MOD_ID, "block_finalizers"));

    private static class SLF4JEventBusLogger implements Logger {
        private static final org.slf4j.Logger LOGGER = LoggerFactory.getLogger("Puzzle | EventBus");

        @Override
        public void log(Level level, String s) {
            if(level == Level.INFO) {
                LOGGER.info(s);
            } else if(level == Level.WARNING) {
                LOGGER.warn(s);
            } else if(level == Level.SEVERE) {
                LOGGER.error(s);
            }
        }

        @Override
        public void log(Level level, String s, Throwable t) {
            if(level == Level.INFO) {
                LOGGER.info(s, t);
            } else if(level == Level.WARNING) {
                LOGGER.warn(s, t);
            } else if(level == Level.SEVERE) {
                LOGGER.error(s, t);
            }
        }
    }

}
