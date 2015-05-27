package com.runescape.api.ge.model;

import com.google.common.base.MoreObjects;
import com.runescape.api.ge.GrandExchange;

/**
 * Represents the {@link Item} price information for an {@link Item} on the RuneScape {@link GrandExchange}.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#GE_Item_price_information">GE Item price information</a>
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
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("item", item)
			.toString();
	}
}
