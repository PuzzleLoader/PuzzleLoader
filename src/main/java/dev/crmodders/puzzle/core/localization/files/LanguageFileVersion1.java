package dev.crmodders.puzzle.core.localization.files;

import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.JsonReader;
import com.badlogic.gdx.utils.JsonValue;
import dev.crmodders.puzzle.core.localization.*;

import java.io.IOException;
import java.io.InputStream;
import java.io.Serial;
import java.util.*;

public class LanguageFileVersion1 extends HashMap<TranslationKey, TranslationEntry> implements ILanguageFile {

	@Serial
	private static final long serialVersionUID = 6502650265026502L;

	public static LanguageFileVersion1 loadLanguageFile(FileHandle file) throws IOException {
		JsonReader reader = new JsonReader();
		try (InputStream is = file.read()) {
			JsonValue value = reader.parse(is);
			return new LanguageFileVersion1(value);
		}
	}

	public static final int MAJOR_VERSION = 1;
	public static final int MINOR_VERSION = 0;

	private final TranslationLocale locale;
	private final TranslationLocale fallback;
	private final int majorVersion;
	private final int minorVersion;
	private final Map<String, Integer> priorities;

	public LanguageFileVersion1(JsonValue json) {
		JsonValue language_tag = json.get("language_tag");
		locale = TranslationLocale.fromLanguageTag(language_tag.asString());
		JsonValue fallback_tag = json.get("fallback_tag");
		if (fallback_tag != null)
			fallback = TranslationLocale.fromLanguageTag(fallback_tag.asString());
		else
			fallback = null;
		majorVersion = json.getInt("major_version");
		minorVersion = json.getInt("minor_version");
		priorities = new HashMap<>();
		JsonValue ids = json.get("ids");

		for (int i = 0; i < ids.size; i++) {
			JsonValue id = json.get(ids.get(i).asString());
			Map<TranslationKey, TranslationEntry> translation = parseId(id);
			putAll(translation);
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

	public TranslationLocale locale() {
		return locale;
	}

	@Override
	public List<TranslationLocale> fallbacks() {
		return fallback == null ? List.of() : List.of(fallback);
	}

	public int getMajorVersion() {
		return majorVersion;
	}

	public int getMinorVersion() {
		return minorVersion;
	}

	public Map<String, Integer> getPriorities() {
		return priorities;
	}

	// internal parser

	private Map<TranslationKey, TranslationEntry> parseId(JsonValue id) {
		int priority = id.getInt("priority");
		JsonValue strings = id.get("strings");
		JsonValue format_templates = id.get("format_templates");
		priorities.put(id.name, priority);
		return parseStrings(id, strings, format_templates);
	}

	private List<String> getStringTree(JsonValue strings, JsonValue string) {
		List<String> walk = new ArrayList<>();
		JsonValue parent = string;
		do {
			walk.add(parent.name);
			parent = parent.parent;
		} while (parent != strings);
		Collections.reverse(walk);
		return walk;
	}

	private JsonValue getFormatTemplateForStringTree(JsonValue strings, JsonValue format_templates, List<String> walk) {
		JsonValue child = format_templates;
		for (String name : walk) {
			if (child == null) {
				return null;
			}
			child = child.get(name);
		}
		return child;
	}

	private Map<TranslationKey, TranslationEntry> parseStrings(JsonValue id, JsonValue strings, JsonValue format_templates) {
		Map<TranslationKey, TranslationEntry> entries = new HashMap<>();

		List<JsonValue> list = new ArrayList<>();
		Stack<JsonValue> stack = new Stack<>();
		stack.add(strings);
		while (!stack.isEmpty()) {
			JsonValue child = stack.pop();
			if (child.isObject()) {
				for (int i = 0; i < child.size; i++) {
					stack.add(child.get(i));
				}
			} else {
				list.add(child);
			}
		}

		for (JsonValue string : list) {
			List<String> walk = getStringTree(strings, string);
			JsonValue format_template = getFormatTemplateForStringTree(strings, format_templates, walk);
			entries.put(parseKey(id, walk), parseEntry(string, format_template));
		}
		return entries;
	}

	private TranslationKey parseKey(JsonValue id, List<String> walk) {
		StringBuilder builder = new StringBuilder();
		for (String name : walk) {
			builder.append(name).append(".");
		}
		builder.deleteCharAt(builder.length() - 1);
		return new TranslationKey(id.name + ":" + builder);
	}

	private TranslationEntry parseEntry(JsonValue string, JsonValue format_template) {
		List<TranslationString> strings = parseArray(string).stream().map(TranslationString::new).toList();
		if (format_template == null) {
			return new TranslationEntry(strings);
		} else {
			List<String> parameters = parseArray(format_template);
			return new TranslationEntry(strings, parameters);
		}
	}

	private List<String> parseArray(JsonValue array) {
		if (array.isArray()) {
			return Arrays.asList(array.asStringArray());
		} else {
			return List.of(array.asString());
		}
	}

}
