package io.github.puzzle.core.localization;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class TranslationKey {
	private final String identifier;

	private final int hashCode;

	@Contract(pure = true)
	public TranslationKey(@NotNull String identifier) {
		this.identifier = identifier;
		this.hashCode = identifier.hashCode();
	}

	@Override
	public int hashCode() {
		return hashCode;
	}

	@Override
	public boolean equals(Object obj) {
		return obj instanceof TranslationKey key && key.identifier.equals(identifier);
	}

	public String getIdentifier() {
		return identifier;
	}

	@Override
	public String toString() {
		return identifier;
	}
}
