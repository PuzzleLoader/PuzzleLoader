package io.github.puzzle.core.localization;

import java.util.List;
import java.util.Map;

public interface ILanguageFile {

	boolean contains(TranslationKey key);

	TranslationEntry get(TranslationKey key);

	Map<TranslationKey, TranslationEntry> all();

	Map<TranslationKey, TranslationEntry> group(String id);

	TranslationLocale locale();

	List<TranslationLocale> fallbacks();

}
