package com.runescape.api.bestiary.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.runescape.api.bestiary.Bestiary;

import java.util.Optional;

/**
 * Represents a result returned by searching within the RuneScape {@link Bestiary}.
 */
public final class SearchResult {
	/**
	 * The value of the result.
	 */
	private final int value;

	/**
	 * The label of the result
	 */
	private final String label;

	/**
	 * Creates a new {@link SearchResult}.
	 * @param value The value of the result.
	 * @param label The label of the result.
	 */
	public SearchResult(int value, String label) {
		this.value = Preconditions.checkNotNull(value);
		this.label = Preconditions.checkNotNull(label);
	}

	/**
	 * Gets the value of the result.
	 * @return The value of the result.
	 */
	public int getValue() {
		return value;
	}

	/**
	 * Gets an {@link Optional} of the result's label.
	 * @return An {@link Optional} of the result's label, or {@code Optional.empty()} if no label was found.
	 */
	public Optional<String> getLabel() {
		return Optional.ofNullable(label);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("value", value)
			.add("label", label)
			.toString();
	}
}
