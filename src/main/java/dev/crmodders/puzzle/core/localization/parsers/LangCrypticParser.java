package dev.crmodders.puzzle.core.localization.parsers;

import dev.crmodders.puzzle.core.localization.Language;
import dev.crmodders.puzzle.loader.mod.Version;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
  this code was originally for translations in CrypticClient by Tympanicblock61
*/

public class LangCrypticParser implements LangParser {

    private static final Pattern transPattern = Pattern.compile("\\S+::\\S+");
    private final boolean nestedTranslations;
    private boolean hasNamespace = false;
    private String[] parts; // helpful variable for string split.
    private final List<String> namespaces = new ArrayList<>();

    public LangCrypticParser(boolean nested) {
        this.nestedTranslations = nested;
    }
    @Override
    public ParseResult parse(String source) {
        Language language = new Language();
        Map<String, String> translatables = new HashMap<>();

        String[] lines = source.split("\n");
        for (String line : lines) {
            if (!line.startsWith("~")) {
                parts = line.split("->", 2);
                String key = parts[0].replace("::", ".").trim();
                String value = parts.length > 1 ? parts[1].trim() : "";

                if (key.startsWith("namespaces")) {
                    namespaces.addAll(Arrays.asList(value.split(",")));
                } else {
                    parts = key.split("\\.");
                    if (parts.length > 1 && namespaces.contains(parts[0])) {
                        hasNamespace = true;
                        key = parts[0] + ":" + String.join(".", Arrays.copyOfRange(parts, 1, parts.length));
                        // should I not add if not in namespaces??
                    }
                    translatables.put(key, value);
                }
            }
        }

        if (namespaces.isEmpty()) {
            return ParseResult.error(ParseResult.Errors.Namespaces, false);
        }
        if (!hasNamespace) {
            return ParseResult.error(ParseResult.Errors.NameSpaceContent, false);
        }

        if (nestedTranslations) {
            resolveTranslations(translatables);
        }

        if (translatables.containsKey("language_tag")) {
            language.languageTag = translatables.get("language_tag");
            translatables.remove("language_tag");
        } else return ParseResult.error(ParseResult.Errors.LanguageTag,false);

        int major = 0, minor = 0, patch = 0;

        // should we allow all empty?
        if (translatables.containsKey("version.major")) {
            major = Integer.parseInt(translatables.remove("version.major").trim());
        }
        if (translatables.containsKey("version.minor")) {
            minor = Integer.parseInt(translatables.remove("version.minor").trim());
        }
        if (translatables.containsKey("version:patch")) {
            patch = Integer.parseInt(translatables.remove("version:patch").trim());
        }

        if (major == 0 && minor == 0 && patch == 0) return ParseResult.error(ParseResult.Errors.Version, false);

        language.version = new Version(major, minor, patch);
        language.namespaces = namespaces.toArray(new String[0]);
        language.translations = translatables;

        return ParseResult.parsed(language);
    }

    @Override
    public ParseResult canParse(String fileName, String source) {
        if (!fileName.endsWith(".cr")) {
            return ParseResult.error("None", transPattern.matcher(source).find());
        }
        return ParseResult.error("None", fileName.endsWith(".cr"));
    }

    private void resolveTranslations(Map<String, String> translatables) {
        for (Map.Entry<String, String> entry : translatables.entrySet()) {
            String translation = entry.getValue();
            Matcher matcher = transPattern.matcher(translation);
            while (matcher.find()) {
                String found = matcher.group();
                String internalKey = found.replace("::",".").trim();
                parts = internalKey.split("\\.");
                if (namespaces.contains(parts[0])) {
                    internalKey = parts[0] + ":" + String.join(".", Arrays.copyOfRange(parts, 1, parts.length));
                }
                String resolvedTranslation = translatables.getOrDefault(internalKey, "Unknown");
                translation = translation.replace(found, resolvedTranslation);
            }
            entry.setValue(translation);
        }
    }
}
