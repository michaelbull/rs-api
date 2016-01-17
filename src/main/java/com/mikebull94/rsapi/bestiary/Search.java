package com.mikebull94.rsapi.bestiary;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.Maps;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Facilitates searching of the RuneScape {@link Bestiary} by applying various {@link Filter}s to the search.
 */
public final class Search {
	/**
	 * Represents a filter that may be applied to a {@link Search}.
	 */
	@FunctionalInterface
	private interface Filter {
		/**
		 * The results of applying the filter to the {@link Search}.
		 * @return A {@link Map} of {@link Beast} ids to {@link Beast} names.
		 * @throws IOException If an I/O error occurs.
		 */
		Map<Integer, String> results() throws IOException;
	}

	/**
	 * The {@link Bestiary} to search in.
	 */
	private final Bestiary bestiary;

	/**
	 * The {@link Filter}s to apply to the search.
	 */
	private final List<Filter> filters = new ArrayList<>();

	/**
	 * Creates a new {@link Search}.
	 * @param bestiary The {@link Bestiary}.
	 */
	Search(Bestiary bestiary) {
		this.bestiary = Preconditions.checkNotNull(bestiary);
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s that have one of the specified search terms within their name.
	 * @param terms The terms to search by.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterByNameTerms(String... terms) {
		Preconditions.checkNotNull(terms);
		filters.add(() -> bestiary.searchByTerms(terms));
		return this;
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s that have names starting with a specified letter.
	 * @param letter The starting letter.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterByNameFirstLetter(char letter) {
		filters.add(() -> bestiary.searchByFirstLetter(letter));
		return this;
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s found in a specific area.
	 * @param areaName The name of the area.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterByArea(String areaName) {
		Preconditions.checkNotNull(areaName);
		filters.add(() -> bestiary.beastsInArea(areaName));
		return this;
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s that belong to a specific Slayer category.
	 * @param categoryId The id of the Slayer category.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterBySlayerCategory(int categoryId) {
		filters.add(() -> bestiary.beastsInSlayerCategory(categoryId));
		return this;
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s that belong to a specific Slayer category.
	 * @param categoryName The name of the Slayer category.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterBySlayerCategory(String categoryName) {
		Preconditions.checkNotNull(categoryName);
		filters.add(() -> bestiary.beastsInSlayerCategory(categoryName));
		return this;
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s that are weak to a specific weakness.
	 * @param weaknessId The id of the weakness.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterByWeakness(int weaknessId) {
		filters.add(() -> bestiary.beastsWeakTo(weaknessId));
		return this;
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s that are weak to a specific weakness.
	 * @param weaknessName The name of the weakness.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterByWeakness(String weaknessName) {
		Preconditions.checkNotNull(weaknessName);
		filters.add(() -> bestiary.beastsWeakTo(weaknessName));
		return this;
	}

	/**
	 * Applies a {@link Filter} to the {@link Search} that filters by {@link Beast}s that have a combat level between the lower and upper bound inclusively.
	 * @param lowerBound The lowest combat level.
	 * @param upperBound The highest combat level.
	 * @return The {@link Search}, for chaining.
	 */
	public Search filterByLevel(int lowerBound, int upperBound) {
		Preconditions.checkArgument(upperBound > lowerBound, "The upper combat level bound must be higher than the lower combat level bound.");
		filters.add(() -> bestiary.beastsInLevelGroup(lowerBound, upperBound));
		return this;
	}

	/**
	 * Executes the search.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 */
	public ImmutableMap<Integer, String> results() throws IOException {
		Iterator<Filter> it = filters.iterator();
		Preconditions.checkState(it.hasNext(), "At least one filter must be applied to the search.");

		Map<Integer, String> results = it.next().results();
		while (it.hasNext()) {
			Map<Integer, String> results2 = it.next().results();
			results = Maps.filterKeys(results, results2::containsKey);
		}

		return ImmutableMap.copyOf(results);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Search search = (Search) o;
		return Objects.equals(bestiary, search.bestiary)
			&& Objects.equals(filters, search.filters);
	}

	@Override
	public int hashCode() {
		return Objects.hash(bestiary, filters);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("bestiary", bestiary)
			.add("filters", filters)
			.toString();
	}
}
