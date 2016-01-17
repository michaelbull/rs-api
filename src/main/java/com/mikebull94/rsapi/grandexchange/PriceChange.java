package com.mikebull94.rsapi.grandexchange;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;

import java.util.Objects;

/**
 * Represents the change in price of an {@link Item} on the RuneScape {@link GrandExchange}.
 */
public final class PriceChange {
	/**
	 * The trend.
	 */
	private final String trend;

	/**
	 * The amount of change.
	 */
	private final String change;

	/**
	 * Creates a new {@link PriceChange}.
	 * @param trend The trend.
	 * @param change The amount of change.
	 */
	public PriceChange(String trend, String change) {
		this.trend = Preconditions.checkNotNull(trend);
		this.change = Preconditions.checkNotNull(change);
	}

	/**
	 * Gets the trend.
	 * @return The trend.
	 */
	public String getTrend() {
		return trend;
	}

	/**
	 * Gets the amount of change.
	 * @return The amount of change.
	 */
	public String getChange() {
		return change;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		PriceChange that = (PriceChange) o;
		return Objects.equals(trend, that.trend)
			&& Objects.equals(change, that.change);
	}

	@Override
	public int hashCode() {
		return Objects.hash(trend, change);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("trend", trend)
			.add("change", change)
			.toString();
	}
}
