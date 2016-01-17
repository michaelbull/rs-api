package com.mikebull94.rsapi.grandexchange;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Represents a result returned by searching within the RuneScape {@link GrandExchange}.
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
		Preconditions.checkArgument(items >= 0, "Total amount of items must be non-negative.");
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		SearchResult that = (SearchResult) o;
		return items == that.items
			&& Objects.equals(letter, that.letter);
	}

	@Override
	public int hashCode() {
		return Objects.hash(letter, items);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("letter", letter)
			.add("items", items)
			.toString();
	}
}
