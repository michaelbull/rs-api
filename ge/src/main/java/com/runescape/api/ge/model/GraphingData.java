package com.runescape.api.ge.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.ge.GrandExchange;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
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
	 * Converts a {@link LocalDateTime} to the number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
	 * @param localDateTime The {@link LocalDateTime}.
	 * @return The number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
	 */
	private long localDateTimeToEpochMilli(LocalDateTime localDateTime) {
		return localDateTime.toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link LocalDateTime}s to prices.
	 * @param prices A {@link Map} of datecodes to prices.
	 * @return An {@link ImmutableMap} of {@link LocalDateTime} to prices.
	 */
	private ImmutableMap<LocalDateTime, Integer> getPrices(Map<String, Integer> prices) {
		ImmutableMap.Builder<LocalDateTime, Integer> builder = ImmutableMap.builder();

		for (Map.Entry<String, Integer> entry : prices.entrySet()) {
			long epochMilli = Long.parseLong(entry.getKey());
			LocalDateTime dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC);

			builder.put(dateTime, entry.getValue());
		}
		return builder.build();
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link LocalDateTime}s to daily prices.
	 * @return An {@link ImmutableMap} of {@link LocalDateTime}s to daily prices.
	 */
	public ImmutableMap<LocalDateTime, Integer> getDailyPrices() {
		return getPrices(daily);
	}

	/**
	 * Gets the daily price of an {@link Item} on a given a {@link LocalDateTime} on the UTC time-zone.
	 * @param dateTime The {@link LocalDateTime}.
	 * @return An {@link Optional} of the price, or {@code Optional.empty()} if no daily value was recorded on this date.
	 */
	public Optional<Integer> getDailyValue(LocalDateTime dateTime) {
		return Optional.ofNullable(daily.get(String.valueOf(localDateTimeToEpochMilli(dateTime))));
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link LocalDateTime}s to average prices.
	 * @return An {@link ImmutableMap} of {@link LocalDateTime}s to average prices.
	 */
	public ImmutableMap<LocalDateTime, Integer> getAveragePrices() {
		return getPrices(average);
	}

	/**
	 * Gets the average price of an {@link Item} given on a given {@link LocalDateTime}.
	 * @param dateTime The {@link LocalDateTime}.
	 * @return An {@link Optional} of the price, or {@code Optional.empty()} if no average value was recorded on this date.
	 */
	public Optional<Integer> getAverageValue(LocalDateTime dateTime) {
		return Optional.ofNullable(average.get(String.valueOf(localDateTimeToEpochMilli(dateTime))));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("daily", daily)
			.add("average", average)
			.toString();
	}
}
