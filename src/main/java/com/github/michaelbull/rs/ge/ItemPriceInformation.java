package com.github.michaelbull.rs.ge;

import com.google.common.base.MoreObjects;

import java.util.Objects;

/**
 * Represents the {@link Item} price information for an {@link Item} on the RuneScape {@link GrandExchange}.
 * @see <a href="https://runescape.wiki/w/Application_programming_interface#detail">GE Item price information</a>
 */
public final class ItemPriceInformation {

	/**
	 * The item.
	 */
	private final Item item;

	/**
	 * Creates a new {@link ItemPriceInformation}.
	 * @param item The item.
	 */
	public ItemPriceInformation(Item item) {
		this.item = item;
	}

	/**
	 * Gets the item.
	 * @return The item.
	 */
	public Item getItem() {
		return item;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ItemPriceInformation that = (ItemPriceInformation) o;
		return Objects.equals(item, that.item);
	}

	@Override
	public int hashCode() {
		return Objects.hash(item);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("item", item)
			.toString();
	}
}
