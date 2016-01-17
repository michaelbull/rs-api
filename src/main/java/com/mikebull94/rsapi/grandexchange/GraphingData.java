package com.mikebull94.rsapi.grandexchange;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.time.Instant;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Map;
import java.util.Objects;
import java.util.OptionalInt;

/**
 * Represents the data used to graphically display an {@link Item}s price history on the RuneScape {@link GrandExchange}.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Graphing_Data">Graphing Data</a>
 */
public final class GraphingData {
	/**
	 * Converts a {@link LocalDate} to the number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
	 * @param date The {@link LocalDate}.
	 * @return The number of milliseconds from the epoch of 1970-01-01T00:00:00Z.
	 */
	private static long localDateToEpochMilli(LocalDate date) {
		return date.atStartOfDay().toInstant(ZoneOffset.UTC).toEpochMilli();
	}

	/**
	 * Creates a set of {@link GraphingData} from a {@link Map} of datecodes (milliseconds since the epoch of 1970-01-01T00:00:00Z) to prices.
	 * @param daily The daily price values.
	 * @param average The average price values.
	 * @return The {@link GraphingData}.
	 */
	public static GraphingData fromDatecodes(Map<String, Integer> daily, Map<String, Integer> average) {
		return new GraphingData(Preconditions.checkNotNull(daily), Preconditions.checkNotNull(average));
	}

	/**
	 * Creates a set of {@link GraphingData} from a {@link Map} of {@link LocalDate}s to prices.
	 * @param daily The daily price values.
	 * @param average The average price values.
	 * @return The {@link GraphingData}.
	 */
	public static GraphingData fromLocalDates(Map<LocalDate, Integer> daily, Map<LocalDate, Integer> average) {
		Preconditions.checkNotNull(daily);
		Preconditions.checkNotNull(average);

		ImmutableMap.Builder<String, Integer> dailyBuilder = ImmutableMap.builder();
		for (Map.Entry<LocalDate, Integer> entry : daily.entrySet()) {
			dailyBuilder.put(String.valueOf(localDateToEpochMilli(entry.getKey())), entry.getValue());
		}

		ImmutableMap.Builder<String, Integer> averageBuilder = ImmutableMap.builder();
		for (Map.Entry<LocalDate, Integer> entry : average.entrySet()) {
			averageBuilder.put(String.valueOf(localDateToEpochMilli(entry.getKey())), entry.getValue());
		}

		return new GraphingData(dailyBuilder.build(), averageBuilder.build());
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link LocalDate}s to prices.
	 * @param prices A {@link Map} of datecodes to prices.
	 * @return An {@link ImmutableMap} of {@link LocalDate} to prices.
	 */
	private static ImmutableMap<LocalDate, Integer> getPrices(Map<String, Integer> prices) {
		ImmutableMap.Builder<LocalDate, Integer> builder = ImmutableMap.builder();

		for (Map.Entry<String, Integer> entry : prices.entrySet()) {
			long epochMilli = Long.parseLong(entry.getKey());
			LocalDate dateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli(epochMilli), ZoneOffset.UTC).toLocalDate();

			builder.put(dateTime, entry.getValue());
		}

		return builder.build();
	}

	/**
	 * A {@link Map} of datecodes to daily price values.
	 */
	private final Map<String, Integer> daily;

	/**
	 * A {@link Map} of datecodes to average price values.
	 */
	private final Map<String, Integer> average;

	/**
	 * Creates new {@link GraphingData}.
	 * @param daily The daily price values.
	 * @param average The average price values.
	 */
	private GraphingData(Map<String, Integer> daily, Map<String, Integer> average) {
		this.daily = Preconditions.checkNotNull(daily);
		this.average = Preconditions.checkNotNull(average);
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link LocalDate}s to daily prices.
	 * @return An {@link ImmutableMap} of {@link LocalDate}s to daily prices.
	 */
	public ImmutableMap<LocalDate, Integer> getDailyPrices() {
		return getPrices(daily);
	}

	/**
	 * Gets the daily price of an {@link Item} on a given a {@link LocalDate} on the UTC time-zone.
	 * @param dateTime The {@link LocalDate}.
	 * @return An {@link OptionalInt} containing the price, or {@link OptionalInt#empty()} if no daily value was recorded on this date.
	 */
	public OptionalInt getDailyPrice(LocalDate dateTime) {
		Integer price = daily.get(String.valueOf(localDateToEpochMilli(dateTime)));
		return price == null ? OptionalInt.empty() : OptionalInt.of(price);
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link LocalDate}s to average prices.
	 * @return An {@link ImmutableMap} of {@link LocalDate}s to average prices.
	 */
	public ImmutableMap<LocalDate, Integer> getAveragePrices() {
		return getPrices(average);
	}

	/**
	 * Gets the average price of an {@link Item} given on a given {@link LocalDate}.
	 * @param dateTime The {@link LocalDate}.
	 * @return An {@link OptionalInt} containing the price, or {@link OptionalInt#empty()} if no average value was recorded on this date.
	 */
	public OptionalInt getAveragePrice(LocalDate dateTime) {
		Integer price = average.get(String.valueOf(localDateToEpochMilli(dateTime)));
		return price == null ? OptionalInt.empty() : OptionalInt.of(price);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GraphingData that = (GraphingData) o;
		return Objects.equals(daily, that.daily)
			&& Objects.equals(average, that.average);
	}

	@Override
	public int hashCode() {
		return Objects.hash(daily, average);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("daily", daily)
			.add("average", average)
			.toString();
	}
}
