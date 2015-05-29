package com.runescape.api.bestiary;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.Client;
import com.runescape.api.bestiary.model.Beast;
import org.junit.Test;

import java.io.IOException;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class TestBestiary {
	private static final int HELLHOUND = 49;
	private static final int KING_BLACK_DRAGON = 50;
	private static final int BLUE_DRAGON = 55;
	private static final int ZEKE = 541;
	private static final int GOLDEN_SHEEP = 1271;
	private static final int SHEEP = 5163;

	private final Bestiary bestiary = new Bestiary(new Client());

	@Test
	public void testBeastData() throws IOException {
		Optional<Beast> optional = bestiary.beastData(-5);
		assertFalse(optional.isPresent());

		Beast kbd = bestiary.beastData(KING_BLACK_DRAGON).get();
		assertEquals("King Black Dragon", kbd.getName());
		assertEquals(276, kbd.getCombatLevel());
		assertEquals(45000, kbd.getLifePoints());
		assertEquals("Black dragons", kbd.getSlayerCategory().get());
		assertEquals(ImmutableList.of("Wilderness Dungeons"), kbd.getAreas());

		Beast hans = bestiary.beastData(0).get();
		assertFalse(hans.getWeakness().isPresent());
		assertFalse(hans.getSlayerCategory().isPresent());

		Beast hellhound = bestiary.beastData(HELLHOUND).get();
		assertEquals("Hello, nice doggy...", hellhound.getDescription());
		assertEquals(344.4, hellhound.getExperience(), 0);
		assertEquals(92, hellhound.getCombatLevel());
		assertEquals("Hellhounds", hellhound.getSlayerCategory().get());
	}

	@Test
	public void testSearchByTerms() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.searchByTerms("weirdterm");
		assertTrue(results.isEmpty());

		results = bestiary.searchByTerms("sheep");
		assertNotNull(results.get(SHEEP));
		assertEquals("Golden sheep", results.get(GOLDEN_SHEEP));
	}

	@Test
	public void testSearchByFirstLetter() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.searchByFirstLetter('Z');
		assertEquals("Zeke", results.get(ZEKE));

		results = bestiary.searchByFirstLetter('x');
		assertTrue(results.isEmpty());
	}

	@Test
	public void testAreaNames() throws IOException {
		assertFalse(bestiary.areaNames().isEmpty());
	}

	@Test
	public void testBeastsInArea() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.beastsInArea("Nowhere");
		assertTrue(results.isEmpty());

		results = bestiary.beastsInArea("Varrock");
		assertTrue(results.containsValue("Zaff"));
	}

	@Test
	public void testSlayerCategories() throws IOException {
		assertFalse(bestiary.slayerCategories().isEmpty());
	}

	@Test
	public void testBeastsInSlayerCategory() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.beastsInSlayerCategory(-1);
		assertTrue(results.isEmpty());

		results = bestiary.beastsInSlayerCategory("Unknown");
		assertTrue(results.isEmpty());

		results = bestiary.beastsInSlayerCategory("Blue dragons");
		assertTrue(results.containsKey(BLUE_DRAGON));
		assertTrue(results.containsValue("Baby blue dragon (44)"));
		assertFalse(results.containsValue("King Black Dragon (276)"));
	}

	@Test
	public void testWeaknesses() throws IOException {
		assertFalse(bestiary.weaknesses().isEmpty());
	}

	@Test
	public void testBeastsWeakTo() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.beastsWeakTo(-1);
		assertTrue(results.isEmpty());

		results = bestiary.beastsWeakTo("Everything");
		assertTrue(results.isEmpty());

		results = bestiary.beastsWeakTo("None");
		assertTrue(results.containsValue("Stomp (77)"));

		results = bestiary.beastsWeakTo("Thrown");
		assertTrue(results.containsValue("Lord Iban"));
	}

	@Test
	public void testBeastsInLevelGroup() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.beastsInLevelGroup(301, 302);
		assertTrue(results.isEmpty());

		results = bestiary.beastsInLevelGroup(200, 300);
		assertTrue(results.containsKey(KING_BLACK_DRAGON));
		assertTrue(results.containsValue("Giant mole (230)"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBeastsInLevelGroupThrowsIllegalArgumentException() throws IOException {
		bestiary.beastsInLevelGroup(50, 30);
	}
}