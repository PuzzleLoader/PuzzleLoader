package io.github.puzzle.core.localization.files;

import io.github.puzzle.core.localization.ILanguageFile;
import io.github.puzzle.core.localization.TranslationEntry;
import io.github.puzzle.core.localization.TranslationKey;
import io.github.puzzle.core.localization.TranslationLocale;
import org.jetbrains.annotations.NotNull;

import java.io.Serial;
import java.util.*;

public class MergedLanguageFile extends HashMap<TranslationKey, TranslationEntry> implements ILanguageFile {

	@Serial
    private static final long serialVersionUID = 6502304409622011948L;

	private final TranslationLocale locale;
	private final Set<TranslationLocale> fallbacks = new HashSet<>();

	public MergedLanguageFile(TranslationLocale locale) {
		this.locale = locale;
	}

	public void addLanguageFile(@NotNull ILanguageFile file) {
		putAll(file.all());
		fallbacks.addAll(file.fallbacks());
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

}
