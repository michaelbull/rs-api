package com.runescape.api.ge;

import com.runescape.api.Client;
import com.runescape.api.ge.model.Category;
import com.runescape.api.ge.model.CategoryPrices;
import com.runescape.api.ge.model.GraphingData;
import com.runescape.api.ge.model.ItemPriceInformation;
import org.junit.Test;

import java.io.IOException;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public class TestGrandExchange {
	private static final int ABYSSAL_WHIP = 4151;

	private final GrandExchange ge = new GrandExchange(new Client());

	@Test
	public void testCategory() throws IOException {
		Category category = ge.category(0).get();
		assertTrue(category.getTypes().isEmpty());
		assertFalse(category.getAlpha().isEmpty());

		category = ge.category("Potions").get();
		assertTrue(category.getTypes().isEmpty());
		assertFalse(category.getAlpha().isEmpty());
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
	}

	@Test(expected = IllegalArgumentException.class)
	public void testCategoryPricesThrowsIllegalArgumentException() throws IOException {
		ge.categoryPrices("Bolts", "", 1);
	}

	@Test
	public void testGraphingData() throws IOException {
		assertFalse(ge.graphingData(0).isPresent());

		GraphingData data = ge.graphingData(ABYSSAL_WHIP).get();
		assertFalse(data.getAveragePrices().isEmpty());
		assertFalse(data.getDailyPrices().isEmpty());
	}

	@Test
	public void testItemPriceInformation() throws IOException {
		assertFalse(ge.itemPriceInformation(0).isPresent());

		ItemPriceInformation information = ge.itemPriceInformation(ABYSSAL_WHIP).get();
		assertTrue(information.getItem().getDay30().isPresent());
		assertTrue(information.getItem().getDay90().isPresent());
		assertTrue(information.getItem().getDay180().isPresent());
	}
}