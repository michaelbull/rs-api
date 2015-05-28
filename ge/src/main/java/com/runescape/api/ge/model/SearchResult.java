package com.runescape.api.ge.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * Represents a result returned by searching within the RuneScape {@link com.runescape.api.ge.GrandExchange}.
 */
public final class SearchResult {
	/**
	 * The first letter of the result.
	 */
	private final String letter;

	/**
	 * The amount of items in the result.
	 */
	private final int items;

	/**
	 * Creates a new {@link SearchResult}.
	 * @param letter The first letter.
	 * @param items The amount of items.
	 */
	public SearchResult(String letter, int items) {
		this.letter = Preconditions.checkNotNull(letter);
		this.items = items;
	}

	/**
	 * Gets the first letter of the result.
	 * @return The first letter of the result.
	 */
	public String getLetter() {
		return letter;
	}

	/**
	 * Gets the amount of items in the result.
	 * @return The amount of items in the result.
	 */
	public int getItems() {
		return items;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("letter", letter)
			.add("items", items)
			.toString();
	}
}
