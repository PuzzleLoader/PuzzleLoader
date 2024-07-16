package dev.crmodders.puzzle.core.localization.files;

import com.badlogic.gdx.files.FileHandle;
import dev.crmodders.puzzle.core.localization.ILanguageFile;
import dev.crmodders.puzzle.core.localization.TranslationEntry;
import dev.crmodders.puzzle.core.localization.TranslationKey;
import dev.crmodders.puzzle.core.localization.TranslationLocale;
import dev.crmodders.puzzle.loader.mod.Version;

import java.io.Serial;
import java.util.*;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CrypticLanguageFile extends HashMap<TranslationKey, TranslationEntry> implements ILanguageFile {
    @Serial
    private static final long serialVersionUID = 235800189204634733L;

    public static CrypticLanguageFile loadLanguageFile(FileHandle file) {
        return new CrypticLanguageFile(file.readString());
    }

    private TranslationLocale locale = null;
    private final Version version;
    private final Set<TranslationLocale> fallbacks = new HashSet<>();

    public CrypticLanguageFile(String source) {
        HashMap<String, String> translatables = new HashMap<>();
        String[] lines = source.split("\n");
        for (String line : lines) {
            if (!line.startsWith("~")) {
                String[] parts = line.split("->", 2);
                if (parts[0].equals("language_tag")) {
                    // have to be early set or will skip translations till it reads this
                    locale = TranslationLocale.fromLanguageTag(parts[1].trim());
                } else {
                    String key = parts[0].replace("::", ".");
                    String value = parts.length > 1 ? parts[1] : "";
                    // Replace :: with . to support all translation files
                    String[] keyParts = key.split("\\.");
                    if (keyParts.length > 1 && locale != null && keyParts[0].equals(locale.toLanguageTag())) {
                        key = keyParts[0] + ":" + String.join(".", Arrays.copyOfRange(keyParts, 1, keyParts.length));
                    }
                    translatables.put(key, value);
                }
            }
        }
        // if you really want nested translations, YES I WANT EVERYTHING, nuh uh made it a setting improves speed
        if (translatables.containsKey("nesting")) {
            if (Boolean.parseBoolean(translatables.get("nesting").trim())) {
                resolveTranslations(translatables);
            }
        }

        int major = 1, minor = 0, patch = 0;

        if (translatables.containsKey("version.major")) {
            major = Integer.parseInt(translatables.get("version.major").trim());
        }
        if (translatables.containsKey("version.minor")) {
            minor = Integer.parseInt(translatables.get("version.minor").trim());
        }
        if (translatables.containsKey("version.patch")) {
            patch = Integer.parseInt(translatables.get("version.patch").trim());
        }

        version = new Version(major, minor, patch);

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
        return get((Object) new TranslationKey(key.getIdentifier().replace("::", ".")));
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

    public Version getVersion() {
        return version;
    }

    // needs to remain as :: because in the file it is `base::test->help translation::stuff`
    private static final Pattern transPattern = Pattern.compile("\\S+::\\S+");

    private void resolveTranslations(Map<String, String> translatables) {
        for (Map.Entry<String, String> entry : translatables.entrySet()) {
            String translation = entry.getValue();
            Matcher matcher = transPattern.matcher(translation);
            while (matcher.find()) {
                String found = matcher.group();
                String internalKey = found.replace("::",".").trim();
                String[] parts = internalKey.split("\\.");
                if (parts[0].equals(locale.toLanguageTag())) {
                    internalKey = parts[0] + ":" + String.join(".", Arrays.copyOfRange(parts, 1, parts.length));
                }
                // replace :: with . to support all translation files
                String resolvedTranslation = translatables.getOrDefault(internalKey, "Unknown");
                translation = translation.replace(found, resolvedTranslation);
            }
            entry.setValue(translation);
        }
    }

}
