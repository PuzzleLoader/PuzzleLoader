package dev.crmodders.puzzle.core.loader.localization.parsers;

import com.google.gson.*;
import dev.crmodders.puzzle.core.loader.localization.Language;
import dev.crmodders.puzzle.core.loader.mod.Version;

import java.util.*;

public class LangJsonParser implements LangParser {
    private JsonObject stored = null;
    private String languageTag = null;
    private JsonObject version = null;
    private List<String> namespaces = new ArrayList<>();
    private boolean hasNamespace = false;

    @Override
    public Language parse(String source) {
        if (!isInit()) {
            try {
                getMembers(source);
            } catch (JsonParseException ignored) {
                // logger will log if it cannot parse in Language.java
                return null;
            }
        }

        Language language = new Language();
        language.languageTag = languageTag;

        int major = 0, minor = 0, patch = 0;

        if (version.has("major")) {
            major = version.get("major").getAsInt();
        }
        if (version.has("minor")) {
            minor = version.get("minor").getAsInt();
        }
        if (version.has("patch")) {
            patch = version.get("patch").getAsInt();
        }

        language.version = new Version(major, minor, patch);
        language.namespaces = namespaces.toArray(new String[0]);


        // main parsing stuff
        Deque<JsonElement> elements = new ArrayDeque<>();
        Deque<String> keyStack = new ArrayDeque<>();

        for (Map.Entry<String, JsonElement> entry : stored.entrySet()) {
            if (namespaces.contains(entry.getKey())) {
                elements.add(entry.getValue());
                keyStack.add(entry.getKey());
            }
        }

        while (!elements.isEmpty()) {
            JsonElement element = elements.pop();
            String currentKey = keyStack.pop();

            if (element.isJsonPrimitive()) {
                System.out.println(currentKey+"->"+element.getAsString());
                language.translations.put(currentKey, element.getAsString());
            } else if (element.isJsonObject()) {
                for (Map.Entry<String, JsonElement> child : element.getAsJsonObject().entrySet()) {
                    elements.push(child.getValue());
                    if (namespaces.contains(currentKey)) {
                        keyStack.push(currentKey + ":" + child.getKey());
                    } else {
                        keyStack.push(currentKey + "." + child.getKey());
                    }
                    // use dot for now
                }
            }
        }

        return language;
    }

    @Override
    public boolean canParse(String fileName, String source) {
        if (!fileName.endsWith(".json")) {
            try {
                getMembers(source);
            } catch (JsonParseException ignored) {}
        }
        return fileName.endsWith(".json") || isInit();
    }

    private boolean isInit() {
        // TODO more verbose logging for missing values
        return (stored != null && languageTag != null && version != null && !namespaces.isEmpty() && hasNamespace);
    }
    private void reset() {
        stored = null;
        languageTag = null;
        version = null;
        namespaces = new ArrayList<>();
        hasNamespace = false;
    }

    private void getMembers(String source) throws JsonParseException {
        reset();
        // detect the required parts
        // string language_tag
        // object version
        // int/string version.major
        // int/string version.minor
        // int/string version.patch
        // string list namespaces
        // object {namespace} n* namespaces.length

        stored = JsonParser.parseString(source).getAsJsonObject();
        if (stored.has("language_tag")) {
            languageTag = stored.get("language_tag").getAsString();
        } else return; // avoid extra code execution, there is no point if this is not defined
        if (stored.has("version")) {
            version = stored.getAsJsonObject("version");
        } else return; // avoid extra code execution, there is no point if this is not defined
        if (stored.has("namespaces")) {
            namespaces = jsonToJavaList(stored.getAsJsonArray("namespaces"));
            for (String namespace : namespaces) {
                if (!hasNamespace && stored.has(namespace)) {
                    hasNamespace = true;
                    break;
                }
            }
        }
    }


    private List<String> jsonToJavaList(JsonArray array) {
        List<String> list = new ArrayList<>();
        for (JsonElement element : array) {
            list.add(element.getAsString());
        }
        return list;
    }
}