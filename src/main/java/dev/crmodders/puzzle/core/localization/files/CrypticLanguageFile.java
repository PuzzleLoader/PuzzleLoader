package dev.crmodders.puzzle.core.localization.files;

import dev.crmodders.puzzle.core.localization.*;

import java.io.Serial;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrypticLanguageFile extends HashMap<TranslationKey, TranslationEntry> implements ILanguageFile {
    @Serial
    private static final long serialVersionUID = 235800189204634733L;

    private TranslationLocale locale;
    private final Set<TranslationLocale> fallbacks = new HashSet<>();

    public CrypticLanguageFile(String source) {
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
                translatables.put(key, value);
            }
        }
        // if you really want nested translations, YES I WANT EVERYTHING
        resolveTranslations(translatables);

        if (translatables.containsKey("language_tag")) {
            locale = TranslationLocale.fromLanguageTag(translatables.get("language_tag"));
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

        // if (translatables.containsKey("namespaces")) {
            // language.namespaces = translatables.get("namespaces").split(",");
        // }

        // language.version = new Version(major, minor, patch);
        for(Map.Entry<String, String> entry : translatables.entrySet()) {
            put(new TranslationKey(entry.getKey()), new TranslationEntry(entry.getValue()));
        }
    }

    @Override
    public boolean contains(TranslationKey key) {
        return containsKey(key);
    }

    @Override
    public TranslationEntry get(TranslationKey key) {
        return get((Object) key);
    }

    @Override
    public Map<TranslationKey, TranslationEntry> all() {
        return this;
    }

    @Override
    public Map<TranslationKey, TranslationEntry> group(String id) {
        return this; // TODO
    }

    @Override
    public TranslationLocale locale() {
        return locale;
    }

    @Override
    public List<TranslationLocale> fallbacks() {
        return new ArrayList<>(fallbacks);
    }

    private static final Pattern transPattern = Pattern.compile("\\S+\\.\\S+");

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
