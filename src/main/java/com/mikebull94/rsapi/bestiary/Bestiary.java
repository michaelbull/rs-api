package com.mikebull94.rsapi.bestiary;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.reflect.TypeToken;
import com.mikebull94.rsapi.Client;
import com.mikebull94.rsapi.HttpClient;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.StringJoiner;
import java.util.regex.Pattern;

/**
 * Represents the RuneScape Bestiary API.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs">Bestiary APIs</a>
 */
public final class Bestiary {
	/**
	 * A {@link TypeToken} which represents a {@link Map} of {@link String}s to {@link Integer}s.
	 */
	private static final Type TYPE_TOKEN = new TypeToken<Map<String, Integer>>() {
	}.getType();

	/**
	 * The URL to the Bestiary web-service.
	 */
	private static final String BESTIARY_URL = HttpClient.WEB_SERVICES_URL + "/m=itemdb_rs/bestiary";

	/**
	 * The URL to the results of the {@link Beast} data function.
	 */
	private static final String BEAST_DATA_URL = BESTIARY_URL + "/beastData.json";

	/**
	 * The URL to the results of the {@link Beast} search function.
	 */
	private static final String BEAST_SEARCH_URL = BESTIARY_URL + "/beastSearch.json";

	/**
	 * The URL to the list of bestiary names.
	 */
	private static final String BESTIARY_NAMES_URL = BESTIARY_URL + "/bestiaryNames.json";

	/**
	 * The URL to the list of area names.
	 */
	private static final String AREA_NAMES_URL = BESTIARY_URL + "/areaNames.json";

	/**
	 * The URL to the results of the beasts in area function.
	 */
	private static final String AREA_BEASTS_URL = BESTIARY_URL + "/areaBeasts.json";

	/**
	 * The URL to the list of Slayer category names.
	 */
	private static final String SLAYER_CATEGORY_NAMES_URL = BESTIARY_URL + "/slayerCatNames.json";

	/**
	 * The URL to the results of the beasts by Slayer category function.
	 */
	private static final String SLAYER_BEASTS_URL = BESTIARY_URL + "/slayerBeasts.json";

	/**
	 * The URL to the list of weakness names.
	 */
	private static final String WEAKNESS_NAMES_URL = BESTIARY_URL + "/weaknessNames.json";

	/**
	 * The URL to the results of the {@link Beast} by weakness function.
	 */
	private static final String WEAKNESS_BEASTS_URL = BESTIARY_URL + "/weaknessBeasts.json";

	/**
	 * The URL to the results of the {@link Beast} by level group function.
	 */
	private static final String LEVEL_GROUP_URL = BESTIARY_URL + "/levelGroup.json";

	/**
	 * The {@link Pattern} that is replaced with '+' symbols when parsing a beast's name.
	 */
	private static final Pattern NAME_SPACER = Pattern.compile(" ");

	/**
	 * Converts an array of {@link SearchResult} to an {@link ImmutableMap} of {@link Integer}s to {@link String}s.
	 * @param results The array of {@link SearchResult}s.
	 * @return An {@link ImmutableMap} of {@link Integer}s to {@link String}s.
	 */
	private static ImmutableMap<Integer, String> resultsToImmutableMap(SearchResult... results) {
		if (results == null) {
			return ImmutableMap.of();
		}

		ImmutableMap.Builder<Integer, String> builder = ImmutableMap.builder();

		for (SearchResult result : results) {
			result.getLabel().ifPresent(label -> builder.put(result.getValue(), label));
		}

		return builder.build();
	}

	/**
	 * The web-services {@link Client}.
	 */
	private final Client client;

	/**
	 * Creates a new {@link Bestiary}.
	 * @param client The web-services {@link Client}.
	 */
	public Bestiary(Client client) {
		this.client = Preconditions.checkNotNull(client);
	}

	/**
	 * Creates a new {@link Search} that will use results from this {@link Bestiary}.
	 * @return The {@link Search}.
	 */
	public Search search() {
		return new Search(this);
	}

	/**
	 * Gets the a {@link Beast} by its id.
	 * @param beastId The id of the {@link Beast}.
	 * @return An {@link Optional} containing the {@link Beast}, or {@link Optional#empty()} if no {@link Beast} of that id was found.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#Beast_Data">Beast Data</a>
	 */
	public Optional<Beast> beastData(int beastId) throws IOException {
		return client.fromJson(BEAST_DATA_URL + "?beastid=" + beastId, Beast.class);
	}

	/**
	 * Searches for a {@link Beast}'s id by a set of terms.
	 * @param terms The terms to search by.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#Searching_Names">Searching Names</a>
	 */
	public ImmutableMap<Integer, String> searchByTerms(String... terms) throws IOException {
		Preconditions.checkNotNull(terms);

		StringJoiner joiner = new StringJoiner("+");

		for (String term : terms) {
			joiner.add(term);
		}

		return resultsToImmutableMap(client.fromJson(BEAST_SEARCH_URL + "?term=" + joiner, SearchResult[].class).orElse(null));
	}

	/**
	 * Searches for a {@link Beast} by the first letter in it's name.
	 * @param letter The letter to search by.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#Beasts_A_to_Z">Beasts A to Z</a>
	 */
	public ImmutableMap<Integer, String> searchByFirstLetter(char letter) throws IOException {
		return resultsToImmutableMap(client.fromJson(BESTIARY_NAMES_URL + "?letter=" + letter, SearchResult[].class).orElse(null));
	}

	/**
	 * Gets an {@link ImmutableList} of area names.
	 * @return An {@link ImmutableList} of area names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#areaNames">Beasts by Area - areaNames</a>
	 */
	public ImmutableList<String> areaNames() throws IOException {
		Optional<String[]> optional = client.fromJson(AREA_NAMES_URL, String[].class);
		return optional.map(ImmutableList::copyOf).orElse(ImmutableList.of());
	}

	/**
	 * Searches for the {@link Beast}s in a given area.
	 * @param area The name of the area to search for.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#areaBeasts">Beasts by Area - areaBeasts</a>
	 */
	public ImmutableMap<Integer, String> beastsInArea(String area) throws IOException {
		Preconditions.checkNotNull(area);
		return resultsToImmutableMap(client.fromJson(AREA_BEASTS_URL + "?identifier=" + NAME_SPACER.matcher(area).replaceAll("+"), SearchResult[].class).orElse(null));
	}

	/**
	 * Gets an {@link ImmutableMap} of Slayer category names to their corresponding ids.
	 * @return An {@link ImmutableMap} of Slayer category names to their corresponding ids.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#slayerCatNames">Beasts by Slayer Category - slayerCatNames</a>
	 */
	public ImmutableMap<String, Integer> slayerCategories() throws IOException {
		Optional<Map<String, Integer>> optional = client.fromJson(SLAYER_CATEGORY_NAMES_URL, TYPE_TOKEN);
		return optional.map(ImmutableMap::copyOf).orElse(ImmutableMap.of());
	}

	/**
	 * Searches for the {@link Beast}s in a given Slayer category.
	 * @param categoryId The id of the Slayer category.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#slayerBeasts">Beasts by Slayer Category - slayerBeasts</a>
	 */
	public ImmutableMap<Integer, String> beastsInSlayerCategory(int categoryId) throws IOException {
		return resultsToImmutableMap(client.fromJson(SLAYER_BEASTS_URL + "?identifier=" + categoryId, SearchResult[].class).orElse(null));
	}

	/**
	 * Searches for the {@link Beast}s in a given Slayer category.
	 * @param categoryName The name of the Slayer category.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#slayerBeasts">Beasts by Slayer Category - slayerBeasts</a>
	 */
	public ImmutableMap<Integer, String> beastsInSlayerCategory(String categoryName) throws IOException {
		Preconditions.checkNotNull(categoryName);
		ImmutableMap<String, Integer> categories = slayerCategories();
		return categories.containsKey(categoryName) ? beastsInSlayerCategory(categories.get(categoryName)) : ImmutableMap.of();
	}

	/**
	 * Gets an {@link ImmutableMap} of weakness category names to their corresponding ids.
	 * @return An {@link ImmutableMap} of weakness category names to their corresponding ids.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#weaknessNames">Beasts by Weakness - weaknessNames</a>
	 */
	public ImmutableMap<String, Integer> weaknesses() throws IOException {
		Optional<Map<String, Integer>> optional = client.fromJson(WEAKNESS_NAMES_URL, TYPE_TOKEN);
		return optional.map(ImmutableMap::copyOf).orElse(ImmutableMap.of());
	}

	/**
	 * Searches for the {@link Beast}s that are weak to a specific weakness.
	 * @param weaknessId The id of the weakness.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#weaknessBeasts">Beasts by Weakness - weaknessBeasts</a>
	 */
	public ImmutableMap<Integer, String> beastsWeakTo(int weaknessId) throws IOException {
		return resultsToImmutableMap(client.fromJson(WEAKNESS_BEASTS_URL + "?identifier=" + weaknessId, SearchResult[].class).orElse(null));
	}

	/**
	 * Searches for the {@link Beast}s that are weak to a specific weakness.
	 * @param weaknessName The name of the weakness.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#weaknessBeasts">Beasts by Weakness - weaknessBeasts</a>
	 */
	public ImmutableMap<Integer, String> beastsWeakTo(String weaknessName) throws IOException {
		Preconditions.checkNotNull(weaknessName);
		ImmutableMap<String, Integer> weaknesses = weaknesses();
		return weaknesses.containsKey(weaknessName) ? beastsWeakTo(weaknesses.get(weaknessName)) : ImmutableMap.of();
	}

	/**
	 * Searches for the {@link Beast}s that have a combat level between the lower and upper bound inclusively.
	 * @param lowerBound The lowest combat level.
	 * @param upperBound The highest combat level.
	 * @return An {@link ImmutableMap} of {@link Beast} ids to {@link Beast} names.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#Beasts_by_Level">Beasts by Level</a>
	 */
	public ImmutableMap<Integer, String> beastsInLevelGroup(int lowerBound, int upperBound) throws IOException {
		Preconditions.checkArgument(upperBound > lowerBound, "The upper combat level bound must be higher than the lower combat level bound.");
		return resultsToImmutableMap(client.fromJson(LEVEL_GROUP_URL + "?identifier=" + lowerBound + "-" + upperBound, SearchResult[].class).orElse(null));
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Bestiary bestiary = (Bestiary) o;
		return Objects.equals(client, bestiary.client);
	}

	@Override
	public int hashCode() {
		return Objects.hash(client);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("client", client)
			.toString();
	}
}
