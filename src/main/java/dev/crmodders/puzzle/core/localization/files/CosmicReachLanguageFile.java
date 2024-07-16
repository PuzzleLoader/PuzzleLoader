package dev.crmodders.puzzle.core.localization.files;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import dev.crmodders.puzzle.core.localization.ILanguageFile;
import dev.crmodders.puzzle.core.localization.TranslationEntry;
import dev.crmodders.puzzle.core.localization.TranslationKey;
import dev.crmodders.puzzle.core.localization.TranslationLocale;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CosmicReachLanguageFile extends HashMap<TranslationKey, TranslationEntry> implements ILanguageFile {

	@Serial
	private static final long serialVersionUID = 5048234800713263956L;

	public static CosmicReachLanguageFile loadLanguageFile(FileHandle file) throws IOException {
		JsonReader reader = new JsonReader();
		try (InputStream is = file.read()) {
			JsonValue value = reader.parse(is);
			return new CosmicReachLanguageFile(value, file.nameWithoutExtension());
		}
	}

	private TranslationLocale locale;
	private final List<TranslationLocale> fallbacks = new ArrayList<>();

	public CosmicReachLanguageFile(JsonValue json, String languageTag) {
		parseMetadata(json.get("metadata"), languageTag);
		parseStrings(json);
	}

	private void parseMetadata(JsonValue metadata, String languageTag) {
		locale = TranslationLocale.fromLanguageTag(languageTag.replace("_", "-"));
		if(metadata.has("fallbacks")) {
			for (String fallback : metadata.get("fallbacks").asStringArray()) {
				this.fallbacks.add(TranslationLocale.fromLanguageTag(fallback.replace("_", "-")));
			}
		}
	}

	private void parseStrings(JsonValue json) {
		for (int i = 0; i < json.size; i++) {
			JsonValue string = json.get(i);
			if (string.name.equals("metadata"))
				continue;
			TranslationKey key = new TranslationKey(string.name);
			TranslationEntry entry = new TranslationEntry(string.asString());
			put(key, entry);
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
		return this;
	}

	@Override
	public TranslationLocale locale() {
		return locale;
	}

	@Override
	public List<TranslationLocale> fallbacks() {
		return fallbacks;
	}

}
