package dev.crmodders.puzzle.core.localization.parsers;

import dev.crmodders.puzzle.core.localization.Language;

public interface LangParser {

    Language parse(String source);
    // maybe not pass source?
    boolean canParse(String fileName, String source);
}
