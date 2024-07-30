package dev.crmodders.puzzle.core;

import dev.crmodders.puzzle.core.localization.ILanguageFile;
import dev.crmodders.puzzle.core.localization.LanguageRegistry;
import dev.crmodders.puzzle.core.registries.GenericRegistry;
import dev.crmodders.puzzle.core.registries.IRegistry;
import dev.crmodders.puzzle.game.block.IModBlock;
import dev.crmodders.puzzle.game.block.PuzzleBlockAction;
import dev.crmodders.puzzle.game.factories.IFactory;
import dev.crmodders.puzzle.game.loot.PuzzleLootTable;
import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Logger;
import org.slf4j.LoggerFactory;

import java.util.logging.Level;

import static dev.crmodders.puzzle.core.Puzzle.MOD_ID;

public class PuzzleRegistries {

    public static final EventBus EVENT_BUS = EventBus.builder().sendNoSubscriberEvent(false).logNoSubscriberMessages(false).logger(new SLF4JEventBusLogger()).build();

    public static final IRegistry<PuzzleLootTable> LOOT_TABLES = new GenericRegistry<>(Identifier.of(MOD_ID, "loot_tables"));
    public static final LanguageRegistry LANGUAGES = new LanguageRegistry(Identifier.of(MOD_ID, "languages"));
    public static final IRegistry<ILanguageFile> LANGUAGES_FILES = new GenericRegistry<>(Identifier.of(MOD_ID, "language_files"));

    public static final IRegistry<Runnable> ON_PRE_INIT = new GenericRegistry<>(new Identifier(MOD_ID, "pre_init_stage"));
    public static final IRegistry<Runnable> ON_INIT = new GenericRegistry<>(new Identifier(MOD_ID, "init_stage"));
    public static final IRegistry<Runnable> ON_POST_INIT = new GenericRegistry<>(new Identifier(MOD_ID, "post_init_stage"));

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
