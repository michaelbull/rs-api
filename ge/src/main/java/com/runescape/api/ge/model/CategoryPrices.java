package com.runescape.api.ge.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.runescape.api.ge.GrandExchange;

import java.util.Arrays;

/**
 * Represents the prices of a collection of {@link Item}s within a {@link Category} on the {@link GrandExchange}.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Category_price_details">Category price details</a>
 */
public final class CategoryPrices {
	/**
	 * The total amount of items on this page of the {@link Category}.
	 */
	private final int total;

	/**
	 * The {@link Item}s on this page of the {@link Category}.
	 */
	private final Item[] items;

	/**
	 * Creates a new {@link CategoryPrices}
	 * @param total The total amount of items on this page.
	 * @param items The items on this page.
	 */
	public CategoryPrices(int total, Item[] items) {
		this.total = Preconditions.checkNotNull(total);
		this.items = Preconditions.checkNotNull(items).clone();
	}

	/**
	 * Gets the total amount of items on this page of the {@link Category}.
	 * @return The total amount of items on this page of the {@link Category}.
	 */
	public int getTotal() {
		return total;
	}

	/**
	 * Gets the {@link Item}s on this page of the {@link Category}.
	 * @return The {@link Item}s on this page of the {@link Category}.
	 */
	public ImmutableList<Item> getItems() {
		return ImmutableList.copyOf(items);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("total", total)
			.add("items", Arrays.toString(items))
			.toString();
	}
}
