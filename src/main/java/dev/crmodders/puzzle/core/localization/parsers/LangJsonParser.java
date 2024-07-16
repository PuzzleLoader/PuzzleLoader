package dev.crmodders.puzzle.core.localization.parsers;

import com.google.gson.*;
import dev.crmodders.puzzle.core.localization.Language;
import dev.crmodders.puzzle.loader.mod.Version;

import java.util.*;

public class LangJsonParser implements LangParser {
    private JsonObject stored = null;
    private String languageTag = null;
    private JsonObject version = null;
    private List<String> namespaces = Collections.emptyList();
    private boolean hasNamespace = false;

    @Override
    public ParseResult parse(String source) {
        if (!isInit()) {
            try {
                getMembers(source);
            } catch (JsonParseException ignored) {}
            if (!isInit()) {
                return ParseResult.error(getError(), false);
            }
        }

        Language language = new Language();
        language.languageTag = languageTag;

        // version != null unnecessary if we only want to parse languages with versions
        int major = version != null && version.has("major") ? version.get("major").getAsInt() : 0;
        int minor = version != null && version.has("minor") ? version.get("minor").getAsInt() : 0;
        int patch = version != null && version.has("patch") ? version.get("patch").getAsInt() : 0;

        language.version = new Version(major, minor, patch);
        language.namespaces = namespaces.toArray(new String[0]);

        // Main parsing
        Deque<Map.Entry<String, JsonElement>> elements = new ArrayDeque<>();
        for (Map.Entry<String, JsonElement> entry : stored.entrySet()) {
            if (namespaces.contains(entry.getKey())) {
                elements.add(entry);
            }
        }

        while (!elements.isEmpty()) {
            Map.Entry<String, JsonElement> entry = elements.pop();
            String currentKey = entry.getKey();
            JsonElement element = entry.getValue();

            if (element.isJsonPrimitive()) {
                language.translations.put(currentKey, element.getAsString());
            } else if (element.isJsonObject()) {
                for (Map.Entry<String, JsonElement> child : element.getAsJsonObject().entrySet()) {
                    elements.push(new AbstractMap.SimpleEntry<>(
                            namespaces.contains(currentKey) ? currentKey + ":" + child.getKey() : currentKey + "." + child.getKey(),
                            child.getValue()
                    ));
                }
            }
        }

        return ParseResult.parsed(language);
    }

    @Override
    public ParseResult canParse(String fileName, String source) {
        if (!fileName.endsWith(".json")) {
            try {
                getMembers(source);
            } catch (JsonParseException ignored) {}
            return ParseResult.error("None", isInit());
        }
        return ParseResult.error("None", fileName.endsWith(".json"));
    }

    private boolean isInit() {
        return stored != null && languageTag != null && version != null && !namespaces.isEmpty() && hasNamespace;
    }

    private void reset() {
        stored = null;
        languageTag = null;
        version = null;
        namespaces = Collections.emptyList();
        hasNamespace = false;
    }

    private void getMembers(String source) throws JsonParseException {
        reset();
        stored = JsonParser.parseString(source).getAsJsonObject();

        languageTag = stored.has("language_tag") ? stored.get("language_tag").getAsString() : null;
        if (languageTag == null) return;

        version = stored.has("version") ? stored.getAsJsonObject("version") : null;
        if (version == null) return;

        if (stored.has("namespaces")) {
            namespaces = jsonToJavaList(stored.getAsJsonArray("namespaces"));
            hasNamespace = namespaces.stream().anyMatch(stored::has);
        }
    }

    private List<String> jsonToJavaList(JsonArray array) {
        List<String> list = new ArrayList<>(array.size());
        for (JsonElement element : array) {
            list.add(element.getAsString());
        }
        return list;
    }

    private String getError() {
        // should move this to ParseResult
        if (stored == null) return "Cannot parse JsonObject.";
        else if (languageTag == null) return "'language_tag' is missing. Initialization requires a language tag.";
        else if (version == null) return "'version' is missing. Version information is missing.";
        else if (namespaces.isEmpty()) return "'namespaces' is empty or missing. At least one namespace is required.";
        else if (!hasNamespace) return "Namespace information is missing.";
        return "Unknown Error";
    }
}
