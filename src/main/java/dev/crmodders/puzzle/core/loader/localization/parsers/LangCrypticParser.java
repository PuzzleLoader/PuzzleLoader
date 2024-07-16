package dev.crmodders.puzzle.core.loader.localization.parsers;

import dev.crmodders.puzzle.core.loader.localization.Language;
import dev.crmodders.puzzle.core.loader.mod.Version;

import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/*
  this code was originally for translations in CrypticClient by Tympanicblock61
*/

public class LangCrypticParser implements LangParser {

    private static final Pattern transPattern = Pattern.compile("\\S+\\.\\S+");
    private final boolean nestedTranslations;

    public LangCrypticParser(boolean nested) {
        nestedTranslations = nested;
    }


    @Override
    public Language parse(String source) {
        Language language = new Language();

        HashMap<String, String> translatables = new HashMap<>();
        String[] lines = source.split("\n");
        for (String line : lines) {
            if (!line.startsWith("~")) {
                String[] parts = line.split("->", 2);
                String key = parts[0].replace("::", ".");
                String value = parts.length > 1 ? parts[1] : "";
                // Replace :: with . to support all translation files
                String[] keyParts = key.split("\\.");
                if (keyParts.length > 1) {
                    key = keyParts[0] + ":" + String.join(".", Arrays.copyOfRange(keyParts, 1, keyParts.length));
                }
                System.out.println(key);
                translatables.put(key, value);
            }
        }
        // if you really want nested translations
        if (nestedTranslations) resolveTranslations(translatables);

        System.out.println(translatables.containsKey("language_tag"));
        if (translatables.containsKey("language_tag")) {
            language.languageTag = translatables.get("language_tag");
        } //  avoid extra code execution, there is no point if this is not defined

        int major = 0, minor = 0, patch = 0;

        if (translatables.containsKey("version:major")) {
            major = Integer.parseInt(translatables.get("version:major").trim());
        }
        if (translatables.containsKey("version:minor")) {
            minor = Integer.parseInt(translatables.get("version:minor").trim());
        }
        if (translatables.containsKey("version:patch")) {
            patch = Integer.parseInt(translatables.get("version:patch").trim());
        }

        if (translatables.containsKey("namespaces")) {
            language.namespaces = translatables.get("namespaces").split(",");
        }
        language.version = new Version(major, minor, patch);
        language.translations = translatables;

        System.out.println(translatables);

        return language;
    }

    @Override
    public boolean canParse(String fileName, String source) {
        boolean crypticFormat = false;
        if (!fileName.endsWith(".cr")) {
            crypticFormat = transPattern.matcher(source).find();
        }
        return fileName.endsWith("cr") || crypticFormat;
    }

    private static void resolveTranslations(Map<String, String> translatables) {
        for (Map.Entry<String, String> entry : translatables.entrySet()) {
            String translation = entry.getValue();
            Matcher matcher = transPattern.matcher(translation);
            while (matcher.find()) {
                String found = matcher.group();
                // replace :: with . to support all translation files
                String resolvedTranslation = translatables.getOrDefault(found.replace("::", "."), "Unknown");
                translation = translation.replace(found, resolvedTranslation);
            }
            entry.setValue(translation);
        }
    }
}
