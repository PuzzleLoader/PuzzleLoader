package dev.crmodders.puzzle.loader.lang;

import dev.crmodders.puzzle.loader.mod.info.ModInfo;

public interface LanguageAdapter {

    <T> T create(ModInfo info, String value, Class<T> type) throws LanguageAdapterException;

}
