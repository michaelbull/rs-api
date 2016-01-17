package com.mikebull94.rsapi.ge;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mikebull94.rsapi.Client;
import com.mikebull94.rsapi.grandexchange.Category;
import com.mikebull94.rsapi.grandexchange.CategoryPrices;
import com.mikebull94.rsapi.grandexchange.GrandExchange;
import com.mikebull94.rsapi.grandexchange.GraphingData;
import com.mikebull94.rsapi.grandexchange.Item;
import com.mikebull94.rsapi.grandexchange.ItemPriceInformation;
import com.mikebull94.rsapi.grandexchange.PriceChange;
import com.mikebull94.rsapi.grandexchange.PriceTrend;
import com.mikebull94.rsapi.grandexchange.SearchResult;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.Month;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class GrandExchangeTest {
	private static final int POTIONS_CATEGORY_ID = 26;

	private static final Category POTIONS = new Category(
		new Object[0],
		new SearchResult("a", 44),
		new SearchResult("c", 16),
		new SearchResult("x", 0)
	);

	private static final Category EMPTY_CATEGORY = new Category(new Object[0]);

	private static final int ADAMANT_BRUTAL_ID = 4798;
	private static final Item ADAMANT_BRUTAL = Item.builder(ADAMANT_BRUTAL_ID)
		.type("Ammo")
		.name("Adamant brutal")
		.description("Blunt adamantite arrow...ouch")
		.currentPrice(new PriceTrend("neutral", 247))
		.todaysPrice(new PriceTrend("neutral", 0))
		.membersOnly()
		.build();

	private static final int AZURE_SKILLCHOMPA_ID = 31597;
	private static final Item AZURE_SKILLCHOMPA = Item.builder(AZURE_SKILLCHOMPA_ID)
		.type("Ammo")
		.name("Azure skillchompa")
		.description("Can be used to enhance gathering of energy, fish, logs and ore, as a rune-equivalent tool, where applicable.")
		.currentPrice(new PriceTrend("neutral", 749))
		.todaysPrice(new PriceTrend("neutral", 0))
		.membersOnly()
		.priceChange30Days(new PriceChange("positive", "+1.0%"))
		.priceChange90Days(new PriceChange("positive", "+5.0%"))
		.priceChange180Days(new PriceChange("negative", "-13.0%"))
		.build();

	private static final Item[] ITEMS = { ADAMANT_BRUTAL, AZURE_SKILLCHOMPA };
	private static final CategoryPrices DUMMY_CATEGORY_PRICES = new CategoryPrices(82, ITEMS);

	private static final GraphingData DUMMY_GRAPHING_DATA = GraphingData.fromLocalDates(
		/* daily */
		ImmutableMap.of(
			LocalDate.of(2014, Month.DECEMBER, 24), 100,
			LocalDate.of(2014, Month.DECEMBER, 25), 90,
			LocalDate.of(2014, Month.DECEMBER, 26), 110
		),

		/* average */
		ImmutableMap.of(
			LocalDate.of(2014, Month.DECEMBER, 27), 200,
			LocalDate.of(2014, Month.DECEMBER, 28), 300,
			LocalDate.of(2014, Month.DECEMBER, 29), 400
		)
	);

	private static final class FakeClient implements Client {
		@Override
		public <T> Optional<T> fromJson(String url, Type typeOfT) {
			Preconditions.checkNotNull(url);
			Preconditions.checkNotNull(typeOfT);
			return Optional.empty();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> Optional<T> fromJson(String url, Class<T> classOfT) {
			Preconditions.checkNotNull(url);
			Preconditions.checkNotNull(classOfT);

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/api/catalogue/category.json?category=")) {
				int categoryId = Integer.parseInt(url.substring(url.indexOf("category=") + "category=".length()));

				switch (categoryId) {
					case POTIONS_CATEGORY_ID:
						return Optional.of((T) POTIONS);

					default:
						return Optional.of((T) EMPTY_CATEGORY);
				}
			}

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/api/catalogue/items.json?")) {
				int categoryId = Integer.parseInt(url.substring(url.indexOf("category=") + "category=".length(), url.indexOf('&')));
				String alpha = url.substring(url.indexOf("alpha=") + "alpha=".length(), url.lastIndexOf('&'));
				int page = Integer.parseInt(url.substring(url.indexOf("page=") + "page=".length()));

				if (categoryId == 1 && alpha.equals("a") && page == 1) {
					return Optional.of((T) DUMMY_CATEGORY_PRICES);
				}
			} else if (url.startsWith("http://services.runescape.com/m=itemdb_rs/api/graph/")) {
				int itemId = Integer.parseInt(url.substring(url.indexOf("graph/") + "graph/".length(), url.indexOf(".json")));

				switch (itemId) {
					case ADAMANT_BRUTAL_ID:
						return Optional.of((T) DUMMY_GRAPHING_DATA);

					default:
						return Optional.empty();
				}
			} else if (url.startsWith("http://services.runescape.com/m=itemdb_rs/api/catalogue/detail.json?item=")) {
				int itemId = Integer.parseInt(url.substring(url.indexOf("item=") + "item=".length()));

				switch (itemId) {
					case AZURE_SKILLCHOMPA_ID:
						return Optional.of((T) new ItemPriceInformation(AZURE_SKILLCHOMPA));

					default:
						return Optional.empty();
				}
			}

			return Optional.empty();
		}

		@Override
		public ImmutableList<CSVRecord> fromCSV(String url) {
			Preconditions.checkNotNull(url);
			return ImmutableList.of();
		}
	}

	private final GrandExchange ge = new GrandExchange(new FakeClient());

	@Test
	public void testCategory() throws IOException {
		Category category = ge.category(0).get();
		assertTrue(category.getTypes().isEmpty());
		assertTrue(category.getAlpha().isEmpty());

		category = ge.category("Potions").get();
		assertTrue(category.getTypes().isEmpty());
		assertFalse(category.getAlpha().isEmpty());
		assertEquals(44, category.getResult(0).get().getItems());
	}

	@Test(expected = IndexOutOfBoundsException.class)
	public void testCategoryThrowsIndexOutOfBoundsException() throws IOException {
		ge.category(-1).get();
	}

	@Test
	public void testCategoryPrices() throws IOException {
		CategoryPrices prices = ge.categoryPrices("Ammo", "a", 1).get();
		assertTrue(prices.getTotal() > 0);
		assertFalse(prices.getItems().isEmpty());
		assertEquals(ADAMANT_BRUTAL, prices.getItems().get(0));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCategoryPricesThrowsIllegalArgumentException() throws IOException {
		ge.categoryPrices("Bolts", "", 1);
	}

	@Test
	public void testGraphingData() throws IOException {
		assertFalse(ge.graphingData(AZURE_SKILLCHOMPA_ID).isPresent());

		GraphingData data = ge.graphingData(ADAMANT_BRUTAL_ID).get();
		assertFalse(data.getDailyPrices().isEmpty());
		assertFalse(data.getAveragePrices().isEmpty());
		assertEquals(90, data.getDailyPrice(LocalDate.of(2014, Month.DECEMBER, 25)).getAsInt());
		assertEquals(400, data.getAveragePrice(LocalDate.of(2014, Month.DECEMBER, 29)).getAsInt());
	}

	@Test
	public void testItemPriceInformation() throws IOException {
		assertFalse(ge.itemPriceInformation(ADAMANT_BRUTAL_ID).isPresent());

		ItemPriceInformation information = ge.itemPriceInformation(AZURE_SKILLCHOMPA_ID).get();
		assertEquals("+1.0%", information.getItem().getDay30().get().getChange());
		assertEquals("+5.0%", information.getItem().getDay90().get().getChange());
		assertEquals("-13.0%", information.getItem().getDay180().get().getChange());
	}
}
