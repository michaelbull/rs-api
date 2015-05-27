package com.runescape.api.ge.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

/**
 * Represents the trend and price of an {@link Item}.
 */
public final class Price {
	/**
	 * The trend.
	 */
	private final String trend;

	/**
	 * The price.
	 */
	private final String price;

	/**
	 * Creates a new {@link Price}.
	 * @param trend The trend.
	 * @param price The price.
	 */
	public Price(String trend, String price) {
		this.trend = Preconditions.checkNotNull(trend);
		this.price = Preconditions.checkNotNull(price);
	}

	/**
	 * Gets the trend.
	 * @return The trend.
	 */
	public String getTrend() {
		return trend;
	}

	/**
	 * Gets the price.
	 * @return The price.
	 */
	public String getPrice() {
		return price;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("trend", trend)
			.add("price", price)
			.toString();
	}
}
