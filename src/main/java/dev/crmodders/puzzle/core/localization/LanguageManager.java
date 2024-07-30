package dev.crmodders.puzzle.core.localization;

import com.badlogic.gdx.scenes.scene2d.Actor;
import com.badlogic.gdx.scenes.scene2d.Stage;
import com.badlogic.gdx.scenes.scene2d.ui.Label;
import com.badlogic.gdx.scenes.scene2d.ui.ProgressBar;
import dev.crmodders.puzzle.core.Identifier;
import dev.crmodders.puzzle.core.PuzzleRegistries;
import dev.crmodders.puzzle.core.localization.files.MergedLanguageFile;
import dev.crmodders.puzzle.game.engine.GameLoader;
import dev.crmodders.puzzle.game.ui.TranslationParameters;
import finalforeach.cosmicreach.lang.Lang;
import finalforeach.cosmicreach.settings.Preferences;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.List;

import static dev.crmodders.puzzle.core.PuzzleRegistries.LANGUAGES;

public class LanguageManager {
	private static final Logger LOGGER = LoggerFactory.getLogger("Language Manager");
	public static TranslationEntry UNDEFINED = new TranslationEntry();
	private static Language selectedLanguage;

	public static boolean hasLanguageInstalled(@NotNull TranslationLocale locale) {
		return LANGUAGES.contains(locale.toIdentifier());
	}

	public static void updateLabels(Stage stage) {
		for(Actor actor : stage.getActors()) {
			if(actor instanceof Label label && label.getUserObject() instanceof TranslationParameters params) {
				updateLabel(label, params);
			}
		}
	}

	public static void registerLanguageFile(ILanguageFile lang) {
		TranslationLocale locale = lang.locale();
		Identifier localeIdentifier = locale.toIdentifier();

		if (PuzzleRegistries.LANGUAGES.contains(localeIdentifier))
			if (PuzzleRegistries.LANGUAGES.get(localeIdentifier).file() instanceof MergedLanguageFile merged) {
				merged.addLanguageFile(lang);
			} else {
				MergedLanguageFile merged = new MergedLanguageFile(locale);
				merged.addLanguageFile(lang);
				PuzzleRegistries.LANGUAGES.register(merged);
			}

		if (!PuzzleRegistries.LANGUAGES.contains(localeIdentifier)) {
			PuzzleRegistries.LANGUAGES.register(lang);
		}
	}

	public static void updateLabel(Label label, TranslationParameters parameters) {
		if(parameters.attachedProgressBar != null) {
			ProgressBar bar = parameters.attachedProgressBar;
			String text;
			if(bar.getStepSize() == 1.0F) {
				text = format(parameters.key, (int) bar.getValue(), (int) bar.getMaxValue());
			} else {
				text = format(parameters.key, bar.getValue(), bar.getMaxValue());
			}
			label.setText(text);
			label.invalidate();
		} else {
			label.setText(string(parameters.key));
			label.invalidate();
		}
	}

	public static void selectLanguage(@NotNull TranslationLocale locale) {
		if (locale.toIdentifier().name.equals("und")) {
			LOGGER.error("Language not found {}", locale);
			return;
		}
		Language newLanguage = LANGUAGES.get(locale.toIdentifier());
		if (newLanguage != null) {
			selectedLanguage = newLanguage;
		} else {
            LOGGER.error("Language not found {}", locale);
		}
	}

	private static Lang lastLang;
	public static Language getLanguage() {
		if(Lang.currentLang != lastLang) {
			selectLanguage(TranslationLocale.fromLanguageTag(Preferences.chosenLang.getValue()));
			lastLang = Lang.currentLang;
		}
		return selectedLanguage;
	}

	public static TranslationEntry translate(TranslationKey key) {
		Language current = getLanguage();
		if(current == null) {
			if(key == null) {
				return UNDEFINED;
			}
			return new TranslationEntry(key.getIdentifier());
		} else {
			return current.entry(key);
		}
	}

	public static String string(TranslationKey key) {
		return translate(key).string().string();
	}

	public static List<TranslationString> strings(TranslationKey key) {
		return translate(key).strings();
	}

	public static String format(TranslationKey key, Object... args) {
		return translate(key).string().format(args);
	}
}
