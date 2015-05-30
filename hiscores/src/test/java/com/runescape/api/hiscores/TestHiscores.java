package com.runescape.api.hiscores;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.Client;
import com.runescape.api.hiscores.model.Activity;
import com.runescape.api.hiscores.model.ClanMate;
import com.runescape.api.hiscores.model.Player;
import com.runescape.api.hiscores.model.Skill;
import com.runescape.api.hiscores.model.Table;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class TestHiscores {
	private static final String CSV_SEPARATOR = ",";
	private static final String CSV_DELIMITER = "\n";

	private static final ClanMate MAX = new ClanMate("Max", "Owner", 5_000_000_000L, 999);
	private static final ClanMate ZEZIMA = new ClanMate("Zezima", "Deputy Owner", 4_000_000_000L, 500);
	private static final ClanMate DRUMGUN = new ClanMate("Drumgun", "Coordinator", 3_000_000_000L, 250);
	private static final ImmutableList<ClanMate> CLAN = ImmutableList.of(MAX, ZEZIMA);

	private static final Player MAXED_PLAYER;

	static {
		ImmutableMap.Builder<String, Skill> skillBuilder = ImmutableMap.builder();

		for (String skillName : Hiscores.SKILL_NAMES) {
			long experience = Skill.MAX_EXPERIENCE;

			if (skillName.equals("Overall")) {
				experience *= Hiscores.SKILL_NAMES.size();
			}

			int level = 99;

			if (skillName.equals("Dungeoneering")) {
				level = 120;
			}

			skillBuilder.put(skillName, new Skill(1, level, experience));
		}

		ImmutableMap.Builder<String, Activity> activityBuilder = ImmutableMap.builder();

		for (String activityName : Hiscores.ACTIVITY_NAMES) {
			activityBuilder.put(activityName, new Activity(1, 999));
		}

		MAXED_PLAYER = new Player(skillBuilder.build(), activityBuilder.build());
	}

	private static final class FakeClient implements Client {
		@Override
		public <T> Optional<T> fromJson(String url, Type typeOfT) throws IOException {
			return Optional.empty();
		}

		@Override
		public <T> Optional<T> fromJson(String url, Class<T> classOfT) throws IOException {
			return Optional.empty();
		}

		@Override
		public ImmutableList<CSVRecord> fromCSV(String url) throws IOException {
			if (url.startsWith("http://services.runescape.com/m=clan-hiscores/members_lite.ws?clanName=")) {
				String clanName = url.substring(url.indexOf("clanName=") + "clanName=".length());

				if (clanName.equals("Maxs+Clan")) {
					StringBuilder csv = new StringBuilder();
					csv.append("Clanmate, Clan Rank, Total XP, Kills").append(CSV_DELIMITER);

					for (ClanMate clanMate : CLAN) {
						csv.append(clanMate.getName()).append(CSV_SEPARATOR);
						csv.append(clanMate.getRank()).append(CSV_SEPARATOR);
						csv.append(clanMate.getExperience()).append(CSV_SEPARATOR);
						csv.append(clanMate.getKills());
						csv.append(CSV_DELIMITER);
					}

					try (CSVParser parser = CSVParser.parse(csv.toString(), CSV_FORMAT)) {
						return ImmutableList.copyOf(parser.getRecords());
					}
				}
			} else if (url.startsWith("http://services.runescape.com/m=")) {
				String module = url.substring(url.indexOf("m=") + "m=".length());
				module = module.substring(0, module.indexOf('/'));
				Table table = Table.from(module);

				String player = url.substring(url.indexOf("player=") + "player=".length());

				if (table == Table.DEFAULT && player.equals("Max")) {
					StringBuilder csv = new StringBuilder();

					for (Skill skill : MAXED_PLAYER.getSkills().values()) {
						csv.append(String.valueOf(skill.getRank().orElse(-1))).append(CSV_SEPARATOR);
						csv.append(String.valueOf(skill.getLevel())).append(CSV_SEPARATOR);
						csv.append(String.valueOf(skill.getExperience().orElse(-1L))).append(CSV_DELIMITER);
					}

					for (Activity activity : MAXED_PLAYER.getActivities().values()) {
						csv.append(String.valueOf(activity.getRank().orElse(-1))).append(CSV_SEPARATOR);
						csv.append(String.valueOf(activity.getScore().orElse(-1))).append(CSV_DELIMITER);
					}

					try (CSVParser parser = CSVParser.parse(csv.toString(), CSV_FORMAT)) {
						return ImmutableList.copyOf(parser.getRecords());
					}
				}
			}
			return ImmutableList.of();
		}
	}

	private final Hiscores hiscores = new Hiscores(new FakeClient());

	@Test
	public void testPlayerInformation() throws IOException {
		assertFalse(hiscores.playerInformation("Andrew", Table.OLDSCHOOL).isPresent());

		Player player = hiscores.playerInformation("Max", Table.DEFAULT).get();
		assertEquals(99, player.getSkills().get("Attack").getLevel());

		Skill thieving = player.getSkills().get("Thieving");
		int rank = thieving.getRank().get();
		long experience = thieving.getExperience().get();

		assertEquals(1, rank);
		assertEquals(Skill.MAX_EXPERIENCE, experience);
	}

	@Test
	public void testClanInformation() throws IOException {
		ImmutableList<ClanMate> clan = hiscores.clanInformation("Maxs Clan");
		assertTrue(clan.contains(MAX));
		assertTrue(clan.contains(ZEZIMA));
		assertFalse(clan.contains(DRUMGUN));
	}
}