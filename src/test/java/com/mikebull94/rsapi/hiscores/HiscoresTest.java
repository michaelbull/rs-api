package com.mikebull94.rsapi.hiscores;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mikebull94.rsapi.Client;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.junit.Test;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

public final class HiscoresTest {
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

		ImmutableMap.Builder<String, HiscoreActivity> activityBuilder = ImmutableMap.builder();

		for (String activityName : Hiscores.ACTIVITY_NAMES) {
			activityBuilder.put(activityName, new HiscoreActivity(1, 999));
		}

		MAXED_PLAYER = new Player(skillBuilder.build(), activityBuilder.build());
	}

	private static final class FakeClient implements Client {
		@Override
		public <T> Optional<T> fromJson(String url, Type typeOfT) {
			Preconditions.checkNotNull(url);
			Preconditions.checkNotNull(typeOfT);
			return Optional.empty();
		}

		@Override
		public <T> Optional<T> fromJson(String url, Class<T> classOfT) {
			Preconditions.checkNotNull(url);
			Preconditions.checkNotNull(classOfT);
			return Optional.empty();
		}

		@Override
		public ImmutableList<CSVRecord> fromCSV(String url) throws IOException {
			Preconditions.checkNotNull(url);

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
				HiscoreTable table = HiscoreTable.from(module);

				String player = url.substring(url.indexOf("player=") + "player=".length());

				if (table == HiscoreTable.DEFAULT && player.equals("Max")) {
					StringBuilder csv = new StringBuilder();

					for (Skill skill : MAXED_PLAYER.getSkills().values()) {
						csv.append(skill.getRank().orElse(-1)).append(CSV_SEPARATOR);
						csv.append(skill.getLevel()).append(CSV_SEPARATOR);
						csv.append(skill.getExperience().orElse(-1L)).append(CSV_DELIMITER);
					}

					for (HiscoreActivity activity : MAXED_PLAYER.getActivities().values()) {
						csv.append(activity.getRank().orElse(-1)).append(CSV_SEPARATOR);
						csv.append(activity.getScore().orElse(-1)).append(CSV_DELIMITER);
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
		assertFalse(hiscores.playerInformation("Andrew", HiscoreTable.OLDSCHOOL).isPresent());

		Player player = hiscores.playerInformation("Max", HiscoreTable.DEFAULT).get();
		assertEquals(99, player.getSkills().get("Attack").getLevel());

		Skill thieving = player.getSkills().get("Thieving");
		int rank = thieving.getRank().getAsInt();
		long experience = thieving.getExperience().getAsLong();

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
