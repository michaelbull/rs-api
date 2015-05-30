package com.runescape.api.ge;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.Client;
import com.runescape.api.ge.model.Category;
import com.runescape.api.ge.model.CategoryPrices;
import com.runescape.api.ge.model.GraphingData;
import com.runescape.api.ge.model.Item;
import com.runescape.api.ge.model.ItemPriceInformation;
import com.runescape.api.ge.model.PriceChange;
import com.runescape.api.ge.model.PriceTrend;
import com.runescape.api.ge.model.SearchResult;
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

public final class TestGrandExchange {
	private static final int POTIONS_CATEGORY_ID = 26;

	private static final Category POTIONS = new Category(new Object[0], new SearchResult[] {
		new SearchResult("a", 44),
		new SearchResult("c", 16),
		new SearchResult("x", 0)
	});

	private static final Category EMPTY_CATEGORY = new Category(new Object[0], new SearchResult[0]);

	private static final int ADAMANT_BRUTAL_ID = 4798;
	private static final Item ADAMANT_BRUTAL = new Item(
		/* icon url */       "",
		/* large icon url */ "",
		/* id */             ADAMANT_BRUTAL_ID,
		/* type */           "Ammo",
		/* type icon url */  "",
		/* name */           "Adamant brutal",
		/* description */    "Blunt adamantite arrow...ouch",
		/* daily price */    new PriceTrend("neutral", 247),
		/* average price */  new PriceTrend("neutral", 0),
		/* members only */   true
	);

	private static final int AZURE_SKILLCHOMPA_ID = 31597;
	private static final Item AZURE_SKILLCHOMPA = new Item(
		/* icon url */       "",
		/* large icon url */ "",
		/* id */             AZURE_SKILLCHOMPA_ID,
		/* type */           "Ammo",
		/* type icon url */  "",
		/* name */           "Azure skillchompa",
		/* description */    "Can be used to enhance gathering of energy, fish, logs and ore, as a rune-equivalent tool, where applicable.",
		/* daily price */    new PriceTrend("neutral", 749),
		/* average price */  new PriceTrend("neutral", 0),
		/* members only */   true,
		/* 30 day change */  new PriceChange("positive", "+1.0%"),
		/* 90 day change */  new PriceChange("positive", "+5.0%"),
		/* 180 day change */ new PriceChange("negative", "-13.0%")
	);

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
		public <T> Optional<T> fromJson(String url, Type typeOfT) throws IOException {
			Preconditions.checkNotNull(url);
			Preconditions.checkNotNull(typeOfT);
			return Optional.empty();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> Optional<T> fromJson(String url, Class<T> classOfT) throws IOException {
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
			} else if (url.startsWith("http://services.runescape.com/m=itemdb_rs/api/catalogue/items.json?")) {
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
		public ImmutableList<CSVRecord> fromCSV(String url) throws IOException {
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
		assertEquals(90, (int) data.getDailyPrice(LocalDate.of(2014, Month.DECEMBER, 25)).get());
		assertEquals(400, (int) data.getAveragePrice(LocalDate.of(2014, Month.DECEMBER, 29)).get());
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