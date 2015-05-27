package com.runescape.api.ge.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.ge.GrandExchange;

import java.util.Calendar;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

/**
 * Represents the data used to graphically display an {@link Item}s price history on the RuneScape {@link GrandExchange}.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Graphing_Data">Graphing Data</a>
 */
public final class GraphingData {
	/**
	 * A {@link Map} of datecodes to daily price values.
	 */
	private final Map<String, Integer> daily;

	/**
	 * A {@link Map} of datecodes to average price values.
	 */
	private final Map<String, Integer> average;

	/**
	 * Creates a new {@link GraphingData}.
	 * @param daily The daily price values.
	 * @param average The average price values.
	 */
	public GraphingData(Map<String, Integer> daily, Map<String, Integer> average) {
		this.daily = Preconditions.checkNotNull(daily);
		this.average = Preconditions.checkNotNull(average);
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link Date}s to prices.
	 * @param prices A {@link Map} of datecodes to prices.
	 * @return An {@link ImmutableMap} of {@link Date} to prices.
	 */
	private ImmutableMap<Date, Integer> getPrices(Map<String, Integer> prices) {
		ImmutableMap.Builder<Date, Integer> builder = ImmutableMap.builder();

		for (Map.Entry<String, Integer> entry : prices.entrySet()) {
			Calendar calendar = Calendar.getInstance();
			calendar.setTimeInMillis(Long.parseLong(entry.getKey()));
			builder.put(calendar.getTime(), entry.getValue());
		}
		return builder.build();
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link Date}s to daily prices.
	 * @return An {@link ImmutableMap} of {@link Date} to daily prices.
	 */
	public ImmutableMap<Date, Integer> getDailyPrices() {
		return getPrices(daily);
	}

	/**
	 * Gets the daily price of an {@link Item} on a given a {@link Date}.
	 * @param date The {@link Date}.
	 * @return An {@link Optional} of the price, or {@code Optional.empty()} if no daily value was recorded on this date.
	 */
	public Optional<Integer> getDailyValue(Date date) {
		return Optional.ofNullable(daily.get(String.valueOf(date.getTime())));
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link Date}s to average prices.
	 * @return An {@link ImmutableMap} of {@link Date} to average prices.
	 */
	public ImmutableMap<Date, Integer> getAveragePrices() {
		return getPrices(average);
	}

	/**
	 * Gets the average price of an {@link Item} given on a given {@link Date}.
	 * @param date The {@link Date}.
	 * @return An {@link Optional} of the price, or {@code Optional.empty()} if no average value was recorded on this date.
	 */
	public Optional<Integer> getAverageValue(Date date) {
		return Optional.ofNullable(average.get(String.valueOf(date.getTime())));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("daily", daily)
			.add("average", average)
			.toString();
	}
}
