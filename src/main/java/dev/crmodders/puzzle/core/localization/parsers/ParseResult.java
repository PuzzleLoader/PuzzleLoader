package dev.crmodders.puzzle.core.localization.parsers;

import dev.crmodders.puzzle.core.localization.Language;

public class ParseResult {
    private final boolean error;
    public final boolean canParse;
    private Language parsedValue;
    private String errorValue;

    private ParseResult(String errorValue, boolean canParse) {
        this.error = true;
        this.errorValue = errorValue;
        this.canParse = canParse;
    }

    private ParseResult(Language parsedValue) {
        this.error = false;
        this.canParse = true;
        this.parsedValue = parsedValue;
    }

    public static ParseResult error(String errorValue, boolean canParse) {
        return new ParseResult(errorValue, canParse);
    }

    public static ParseResult parsed(Language parsedValue) {
        return new ParseResult(parsedValue);
    }

    public boolean isError() {
        return error;
    }

    public Language getParsedValue() {
        return parsedValue;
    }

    public String getErrorValue() {
        return errorValue;
    }

    public static class Errors {
        public static String Namespaces = "'namespaces' is empty or missing.";
        public static String NameSpaceContent = "Language File contains no namespace content.";
        public static String LanguageTag = "'language_tag' is missing.";
        public static String Version = "'version' is missing or has all empty values";
    }
}

