package com.mikebull94.rsapi.grandexchange;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.mikebull94.rsapi.Client;
import com.mikebull94.rsapi.HttpClient;

import java.io.IOException;
import java.util.Objects;
import java.util.Optional;

/**
 * Represents the RuneScape Grand Exchange API.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs">Grand Exchange APIs</a>
 */
public final class GrandExchange {
	/**
	 * The URL to the Grand Exchange web-service.
	 */
	private static final String GRAND_EXCHANGE_URL = HttpClient.WEB_SERVICES_URL + "/m=itemdb_rs/api";

	/**
	 * The URL to the results of the {@link Category} information function.
	 */
	private static final String CATEGORY_URL = GRAND_EXCHANGE_URL + "/catalogue/category.json";

	/**
	 * The URL to the results of the {@link Item} information function.
	 */
	private static final String ITEMS_URL = GRAND_EXCHANGE_URL + "/catalogue/items.json";

	/**
	 * The URL to the results of the graphing data function.
	 */
	private static final String GRAPH_URL = GRAND_EXCHANGE_URL + "/graph";

	/**
	 * The URL to the results of the {@link Item} price information function.
	 */
	private static final String DETAILS_URL = GRAND_EXCHANGE_URL + "/catalogue/detail.json";

	/**
	 * The categories of {@link Item}s on the Grand Exchange.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Category_IDs">Category IDs</a>
	 */
	public static final ImmutableList<String> CATEGORIES = ImmutableList.of(
		/* 00 */ "Miscellaneous",
		/* 01 */ "Ammo",
		/* 02 */ "Arrows",
		/* 03 */ "Bolts",
		/* 04 */ "Construction materials",
		/* 05 */ "Construction projects",
		/* 06 */ "Cooking ingredients",
		/* 07 */ "Costumes",
		/* 08 */ "Crafting materials",
		/* 09 */ "Familiars",
		/* 10 */ "Farming produce",
		/* 11 */ "Fletching materials",
		/* 12 */ "Food and drink",
		/* 13 */ "Herblore materials",
		/* 14 */ "Hunting equipment",
		/* 15 */ "Hunting produce",
		/* 16 */ "Jewellery",
		/* 17 */ "Mage armour",
		/* 18 */ "Mage weapons",
		/* 19 */ "Melee armour - low level",
		/* 20 */ "Melee armour - mid level",
		/* 21 */ "Melee armour - high level",
		/* 22 */ "Melee weapons - low level",
		/* 23 */ "Melee weapons - mid level",
		/* 24 */ "Melee weapons - high level",
		/* 25 */ "Mining and smithing",
		/* 26 */ "Potions",
		/* 27 */ "Prayer armour",
		/* 28 */ "Prayer materials",
		/* 29 */ "Range armour",
		/* 30 */ "Range weapons",
		/* 31 */ "Runecrafting",
		/* 32 */ "Runes, Spells and Teleports",
		/* 33 */ "Seeds",
		/* 34 */ "Summoning scrolls",
		/* 35 */ "Tools and containers",
		/* 36 */ "Woodcutting product",
		/* 37 */ "Pocket items"
	);

	/**
	 * The web-services {@link Client}.
	 */
	private final Client client;

	/**
	 * Creates a new {@link GrandExchange}.
	 * @param client The web-services {@link Client}.
	 */
	public GrandExchange(Client client) {
		this.client = Preconditions.checkNotNull(client);
	}

	/**
	 * Gets a {@link Category} by its id.
	 * @param categoryId The id of the {@link Category}.
	 * @return An {@link Optional} containing the {@link Category}, or {@link Optional#empty()} if there is no {@link Category} for the {@link Category} id.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Category_information_details">Category information details</a>
	 */
	public Optional<Category> category(int categoryId) throws IOException {
		Preconditions.checkElementIndex(categoryId, CATEGORIES.size(), "Category id must be between 0 and " + (CATEGORIES.size() - 1) + " inclusive.");
		return client.fromJson(CATEGORY_URL + "?category=" + categoryId, Category.class);
	}

	/**
	 * Gets a {@link Category} by its name.
	 * @param categoryName The name of the {@link Category}.
	 * @return An {@link Optional} containing the {@link Category}, or {@link Optional#empty()} if there is no {@link Category} for the {@link Category} name.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Category_information_details">Category information details</a>
	 */
	public Optional<Category> category(String categoryName) throws IOException {
		Preconditions.checkNotNull(categoryName);
		return category(CATEGORIES.indexOf(categoryName));
	}

	/**
	 * Gets a set {@link CategoryPrices} based on the {@link Category}'s id, an item's prefix, and specified page.
	 * @param categoryId The id of the {@link Category}.
	 * @param prefix An item's prefix.
	 * @param page The page.
	 * @return An {@link Optional} containing the {@link CategoryPrices}, or {@link Optional#empty()} if no {@link CategoryPrices} were found.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Category_price_details">Category price details</a>
	 */
	public Optional<CategoryPrices> categoryPrices(int categoryId, String prefix, int page) throws IOException {
		Preconditions.checkElementIndex(categoryId, CATEGORIES.size(), "Category id must be between 0 and " + (CATEGORIES.size() - 1) + " inclusive.");
		Preconditions.checkArgument(!prefix.isEmpty(), "Prefix must be at least 1 character long.");

		String alpha = prefix;
		try {
			alpha = "%" + Integer.parseInt(prefix);
		} catch (NumberFormatException ignored) {
			/* ignore */
		}

		return client.fromJson(ITEMS_URL + "?category=" + categoryId + "&alpha=" + alpha + "&page=" + page, CategoryPrices.class);
	}

	/**
	 * Gets a set {@link CategoryPrices} based on the {@link Category}'s name, an item's prefix, and specified page.
	 * @param categoryName The name of the {@link Category}.
	 * @param prefix An item's prefix.
	 * @param page The page.
	 * @return An {@link Optional} containing the {@link CategoryPrices}, or {@link Optional#empty()} if no {@link CategoryPrices} were found.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Category_price_details">Category price details</a>
	 */
	public Optional<CategoryPrices> categoryPrices(String categoryName, String prefix, int page) throws IOException {
		Preconditions.checkNotNull(categoryName);
		Preconditions.checkArgument(!prefix.isEmpty(), "Prefix must not be empty.");
		return categoryPrices(CATEGORIES.indexOf(categoryName), prefix, page);
	}

	/**
	 * Gets the {@link GraphingData} of an {@link Item}.
	 * @param itemId The id of the {@link Item}.
	 * @return An {@link Optional} containing the {@link GraphingData}, or {@link Optional#empty()} if no {@link GraphingData} was found for the {@link Item} id.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#Graphing_Data">Graphing Data</a>
	 */
	public Optional<GraphingData> graphingData(int itemId) throws IOException {
		return client.fromJson(GRAPH_URL + "/" + itemId + ".json", GraphingData.class);
	}

	/**
	 * Gets the {@link ItemPriceInformation} of an {@link Item}.
	 * @param itemId The id of the {@link Item}.
	 * @return An {@link Optional} containing the {@link ItemPriceInformation}, or {@link Optional#empty()} if no {@link ItemPriceInformation} was found for the {@link Item} id.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs#GE_Item_price_information">GE Item price information</a>
	 */
	public Optional<ItemPriceInformation> itemPriceInformation(int itemId) throws IOException {
		return client.fromJson(DETAILS_URL + "?item=" + itemId, ItemPriceInformation.class);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		GrandExchange that = (GrandExchange) o;
		return Objects.equals(client, that.client);
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
