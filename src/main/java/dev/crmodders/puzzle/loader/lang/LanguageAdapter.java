package dev.crmodders.puzzle.loader.lang;

import dev.crmodders.puzzle.loader.lang.impl.JavaLanguageAdapter;
import dev.crmodders.puzzle.loader.mod.info.ModInfo;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public interface LanguageAdapter {

    Logger LOGGER = LoggerFactory.getLogger("Puzzle | Language Adapter");
    Map<String, LanguageAdapter> ADAPTERS = new HashMap<>();

    LanguageAdapter JAVA_INSTANCE = new JavaLanguageAdapter();

    <T> T create(ModInfo info, String value, Class<T> type) throws LanguageAdapterException;

    static LanguageAdapter getDefault() {
        return JAVA_INSTANCE;
    }

}
