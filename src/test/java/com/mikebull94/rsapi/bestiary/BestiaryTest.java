package com.mikebull94.rsapi.bestiary;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mikebull94.rsapi.Client;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

public final class BestiaryTest {
	private static final Beast KING_BLACK_DRAGON = Beast.builder(50)
		.name("King Black Dragon")
		.description("Was the biggest, meanest dragon around.")
		.attackable()
		.aggressive()
		.experience(2054.4)
		.lifePoints(45000)
		.combatLevel(276)
		.defenceLevel(60)
		.attackLevel(60)
		.magicLevel(60)
		.rangedLevel(60)
		.size(5)
		.slayerCategory("Black dragons")
		.area("Wilderness Dungeons")
		.animation("death", 17780)
		.animation("attack", 17782)
		.build();

	private static final Beast HANS = Beast.builder(0)
		.name("Hans")
		.description("Servant of the Duke of Lumbridge.")
		.attackable()
		.experience(0.2)
		.area("RuneScape Surface")
		.build();

	private static final Beast HELLHOUND = Beast.builder(49)
		.name("Hellhound")
		.description("Hello, nice doggy...")
		.weakness("Slashing")
		.attackable()
		.experience(344.4)
		.lifePoints(3300)
		.combatLevel(92)
		.defenceLevel(66)
		.attackLevel(66)
		.magicLevel(1)
		.rangedLevel(1)
		.size(2)
		.slayerCategory("Hellhounds")
		.area("Ardougne underground")
		.area("RuneScape Surface")
		.area("Forinthry Dungeon")
		.area("Taverley Dungeon")
		.area("Ancient cavern")
		.animation("death", 17347)
		.animation("attack", 17348)
		.build();

	private static final Beast GIANT_MOLE = Beast.builder(18932)
		.name("Giant mole")
		.description("Holy mole-y!")
		.attackable()
		.experience(2918.8)
		.lifePoints(78000)
		.combatLevel(230)
		.defenceLevel(45)
		.attackLevel(65)
		.magicLevel(65)
		.rangedLevel(1)
		.size(3)
		.animation("death", 22620)
		.animation("attack", 22610)
		.build();

	private static final ImmutableList<Beast> BEASTS = ImmutableList.of(
		KING_BLACK_DRAGON,
		HANS,
		HELLHOUND,
		GIANT_MOLE
	);

	private static final ImmutableMap<String, Integer> SLAYER_CATEGORIES = ImmutableMap.of(
		"TzHaar", 101,
		"Zombies", 10,
		"Vyrewatch", 110
	);

	private static final ImmutableMap<String, Integer> WEAKNESSES = ImmutableMap.of(
		"None", 0,
		"Air", 1,
		"Fire", 4,
		"Thrown", 10
	);

	private static final SearchResult[] SHEEP = {
		new SearchResult(2311, "Sheepdog"),
		new SearchResult(5163, "Sheep"),
		new SearchResult(1271, "Golden sheep")
	};

	private static final SearchResult[] BEGINNING_WITH_Z = {
		new SearchResult(541, "Zeke"),
		new SearchResult(568, "Zambo"),
		new SearchResult(1425, "Zooknock")
	};

	private static final SearchResult[] VARROCK = {
		new SearchResult(546, "Zaff"),
		new SearchResult(552, "Shop assistant"),
		new SearchResult(783, "Wilough")
	};

	private static final SearchResult[] ZOMBIES = {
		new SearchResult(2056, "Skogre (56)"),
		new SearchResult(5308, "Zombie (22)"),
		new SearchResult(8149, "Armoured zombie (86)"),
	};

	private static final SearchResult[] WEAK_TO_NOTHING = {
		new SearchResult(269, "Bouncer (77)"),
		new SearchResult(6198, "Stomp (77)"),
		new SearchResult(8304, "Cyrisus")
	};

	private static final SearchResult[] WEAK_TO_THROWN = {
		new SearchResult(1003, "Lord Iban"),
		new SearchResult(6471, "Mosschin (30)")
	};

	private static final class FakeClient implements Client {
		@Override
		@SuppressWarnings("unchecked")
		public <T> Optional<T> fromJson(String url, Type typeOfT) {
			Preconditions.checkNotNull(url);
			Preconditions.checkNotNull(typeOfT);

			if (url.equals("http://services.runescape.com/m=itemdb_rs/bestiary/slayerCatNames.json")) {
				return Optional.of((T) SLAYER_CATEGORIES);
			}

			if (url.equals("http://services.runescape.com/m=itemdb_rs/bestiary/weaknessNames.json")) {
				return Optional.of((T) WEAKNESSES);
			}

			return Optional.empty();
		}

		@Override
		@SuppressWarnings("unchecked")
		public <T> Optional<T> fromJson(String url, Class<T> classOfT) {
			Preconditions.checkNotNull(url);
			Preconditions.checkNotNull(classOfT);

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/bestiary/beastData.json?beastid=")) {
				int id = Integer.parseInt(url.substring(url.indexOf("beastid=") + "beastid=".length()));

				switch (id) {
					case 50:
						return Optional.of((T) KING_BLACK_DRAGON);

					case 0:
						return Optional.of((T) HANS);

					case 49:
						return Optional.of((T) HELLHOUND);

					default:
						return Optional.empty();
				}
			}

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/bestiary/beastSearch.json?term=sheep")) {
				String terms = url.substring(url.indexOf("term=") + "term=".length());

				switch (terms) {
					case "sheep":
						return Optional.of((T) SHEEP);

					default:
						return Optional.empty();
				}
			}

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/bestiary/bestiaryNames.json?letter=")) {
				char letter = url.charAt(url.length() - 1);

				switch (letter) {
					case 'Z':
						return Optional.of((T) BEGINNING_WITH_Z);

					default:
						return Optional.empty();
				}
			}

			if (url.equals("http://services.runescape.com/m=itemdb_rs/bestiary/areaNames.json")) {
				return Optional.of((T) new String[] { "Bank", "Battlefield", "Cave" });
			}

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/bestiary/areaBeasts.json?identifier=")) {
				String area = url.substring(url.indexOf("identifier=") + "identifier=".length());

				switch (area) {
					case "Varrock":
						return Optional.of((T) VARROCK);

					default:
						return Optional.empty();
				}
			}

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/bestiary/slayerBeasts.json?identifier=")) {
				int id = Integer.parseInt(url.substring(url.indexOf("identifier=") + "identifier=".length()));

				switch (id) {
					case 10:
						return Optional.of((T) ZOMBIES);

					default:
						return Optional.empty();
				}
			}

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/bestiary/weaknessBeasts.json?identifier=")) {
				int id = Integer.parseInt(url.substring(url.indexOf("identifier=") + "identifier=".length()));

				switch (id) {
					case 0:
						return Optional.of((T) WEAK_TO_NOTHING);

					case 10:
						return Optional.of((T) WEAK_TO_THROWN);

					default:
						return Optional.empty();
				}
			}

			if (url.startsWith("http://services.runescape.com/m=itemdb_rs/bestiary/levelGroup.json?identifier=")) {
				String identifier = url.substring(url.indexOf("identifier=") + "identifier=".length());
				int[] bounds = Arrays.stream(identifier.split("-")).mapToInt(Integer::parseInt).toArray();

				int lowerBound = bounds[0];
				int upperBound = bounds[1];

				List<SearchResult> results = BEASTS.stream()
					.filter(beast -> beast.getCombatLevel() >= lowerBound && beast.getCombatLevel() <= upperBound)
					.map(beast -> new SearchResult(beast.getId(), beast.getName() + " (" + beast.getCombatLevel() + ")"))
					.collect(Collectors.toList());

				SearchResult[] resultsArray = results.toArray(new SearchResult[results.size()]);

				return Optional.of((T) resultsArray);
			}

			return Optional.empty();
		}

		@Override
		public ImmutableList<CSVRecord> fromCSV(String url) {
			return ImmutableList.of();
		}
	}

	private final Bestiary bestiary = new Bestiary(new FakeClient());

	@Test
	public void testBeastData() throws IOException {
		Optional<Beast> optional = bestiary.beastData(-5);
		assertFalse(optional.isPresent());

		Beast kbd = bestiary.beastData(50).get();
		assertEquals("King Black Dragon", kbd.getName());
		assertEquals(276, kbd.getCombatLevel());
		assertEquals(45000, kbd.getLifePoints());
		assertEquals("Black dragons", kbd.getSlayerCategory().get());
		assertEquals(ImmutableList.of("Wilderness Dungeons"), kbd.getAreas());

		Beast hans = bestiary.beastData(0).get();
		assertFalse(hans.getWeakness().isPresent());
		assertFalse(hans.getSlayerCategory().isPresent());

		Beast hellhound = bestiary.beastData(49).get();
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
		assertNotNull(results.get(5163));
		assertEquals("Golden sheep", results.get(1271));
	}

	@Test
	public void testSearchByFirstLetter() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.searchByFirstLetter('Z');
		assertEquals("Zeke", results.get(541));

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
		assertTrue(bestiary.slayerCategories().containsKey("Zombies"));
		assertEquals(101, (int) bestiary.slayerCategories().get("TzHaar"));
	}

	@Test
	public void testBeastsInSlayerCategory() throws IOException {
		ImmutableMap<Integer, String> results = bestiary.beastsInSlayerCategory(-1);
		assertTrue(results.isEmpty());

		results = bestiary.beastsInSlayerCategory("Unknown");
		assertTrue(results.isEmpty());

		results = bestiary.beastsInSlayerCategory("Zombies");
		assertTrue(results.containsKey(2056));
		assertTrue(results.containsValue("Zombie (22)"));
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
		assertTrue(results.containsKey(KING_BLACK_DRAGON.getId()));
		assertTrue(results.containsValue("Giant mole (230)"));
	}

	@Test(expected = IllegalArgumentException.class)
	public void testBeastsInLevelGroupThrowsIllegalArgumentException() throws IOException {
		bestiary.beastsInLevelGroup(50, 30);
	}
}
