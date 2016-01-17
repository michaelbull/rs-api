package com.mikebull94.rsapi.grandexchange;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Represents the trend and price of an {@link Item}.
 */
public final class PriceTrend {
	/**
	 * The trend.
	 */
	private final String trend;

	/**
	 * The price.
	 */
	private final String price;

	/**
	 * Creates a new {@link PriceTrend}.
	 * @param trend The trend.
	 * @param price The price.
	 */
	public PriceTrend(String trend, String price) {
		this.trend = Preconditions.checkNotNull(trend);
		this.price = Preconditions.checkNotNull(price);
	}

	/**
	 * Creates a new {@link PriceTrend}.
	 * @param trend The trend.
	 * @param price The price.
	 */
	public PriceTrend(String trend, Number price) {
		this(trend, price.toString());
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
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PriceTrend that = (PriceTrend) o;
		return Objects.equals(trend, that.trend)
			&& Objects.equals(price, that.price);
	}

	@Override
	public int hashCode() {
		return Objects.hash(trend, price);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("trend", trend)
			.add("price", price)
			.toString();
	}
}
