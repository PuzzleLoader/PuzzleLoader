package dev.crmodders.puzzle.core.localization.parsers;

public interface LangParser {
    ParseResult parse(String source);
    // maybe not pass source?
    ParseResult canParse(String fileName, String source);
}
