package com.github.michaelbull.rs.hiscores;

import com.github.michaelbull.rs.Client;
import com.github.michaelbull.rs.HttpClient;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Collection;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Represents the RuneScape Hiscores API.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs">Hiscores APIs</a>
 */
public final class Hiscores {

	/**
	 * The format of the URL to fetch a {@link Player}.
	 */
	private static final String PLAYER_INFORMATION_URL_FORMAT = HttpClient.WEB_SERVICES_URL + "/m=%s/index_lite.ws?player=%s";

	/**
	 * The format of the URL to fetch a list of {@link ClanMate}s.
	 */
	private static final String CLAN_INFORMATION_URL_FORMAT = HttpClient.WEB_SERVICES_URL + "/m=clan-hiscores/members_lite.ws?clanName=%s";

	/**
	 * The RuneScape skill names.
	 */
	public static final ImmutableList<String> SKILL_NAMES = ImmutableList.of(
		"Overall",
		"Attack",
		"Defence",
		"Strength",
		"Constitution",
		"Ranged",
		"Prayer",
		"Magic",
		"Cooking",
		"Woodcutting",
		"Fletching",
		"Fishing",
		"Firemaking",
		"Crafting",
		"Smithing",
		"Mining",
		"Herblore",
		"Agility",
		"Thieving",
		"Slayer",
		"Farming",
		"Runecrafting",
		"Hunter",
		"Construction",
		"Summoning",
		"Dungeoneering",
		"Divination",
		"Invention"
	);

	/**
	 * The oldschool RuneScape skill names.
	 */
	public static final ImmutableList<String> OLDSCHOOL_SKILL_NAMES = ImmutableList.of(
		"Overall",
		"Attack",
		"Defence",
		"Strength",
		"Constitution",
		"Ranged",
		"Prayer",
		"Magic",
		"Cooking",
		"Woodcutting",
		"Fletching",
		"Fishing",
		"Firemaking",
		"Crafting",
		"Smithing",
		"Mining",
		"Herblore",
		"Agility",
		"Thieving",
		"Slayer",
		"Farming",
		"Runecrafting",
		"Hunter",
		"Construction"
	);

	/**
	 * The RuneScape activity names.
	 */
	public static final ImmutableList<String> ACTIVITY_NAMES = ImmutableList.of(
		"Bounty Hunter",
		"B.H. Rogues",
		"Dominion Tower",
		"The Crucible",
		"Castle Wars games",
		"B.A. Attackers",
		"B.A. Defenders",
		"B.A. Collectors",
		"B.A. Healers",
		"Duel Tournament",
		"Mobilising Armies",
		"Conquest",
		"Fist of Guthix",
		"GG: Athletics",
		"GG: Resource Race",
		"WE2: Armadyl Lifetime Contribution",
		"WE2: Bandos Lifetime Contribution",
		"WE2: Armadyl PvP kills",
		"WE2: Bandos PvP kills",
		"Heist Guard Level",
		"Heist Robber Level",
		"CFP: 5 game average",
		"AF15: Cow Tipping",
		"AF15: Rats killed after the miniquest",
		"Clue Scrolls (easy)",
		"Clue Scrolls (medium)",
		"Clue Scrolls (hard)",
		"Clue Scrolls (elite)",
		"Clue Scrolls (master)"
	);

	/**
	 * The oldschool RuneScape activity names.
	 */
	public static final ImmutableList<String> OLDSCHOOL_ACTIVITY_NAMES = ImmutableList.of(
		"Clues",
		"Bounty Hunter",
		"B.H. Rogues"
	);

	/**
	 * The {@link Pattern} that is replaced with '+' symbols when parsing player/clan names.
	 */
	private static final Pattern NAME_SPACER = Pattern.compile(" ");

	/**
	 * Reads the {@link HiscoreActivity}s from an {@link ImmutableList} of {@link CSVRecord}s.
	 * @param records The {@link CSVRecord}s.
	 * @param skills The skill names.
	 * @param activities The activity names.
	 * @return An {@link ImmutableMap} of {@link HiscoreActivity} names to {@link HiscoreActivity}s.
	 */
	private static ImmutableMap<String, HiscoreActivity> readActivities(ImmutableList<CSVRecord> records, Collection<String> skills, ImmutableList<String> activities) {
		ImmutableMap.Builder<String, HiscoreActivity> builder = ImmutableMap.builder();

		if (records.size() >= (skills.size() + activities.size())) {
			for (int i = 0; i < activities.size(); i++) {
				builder.put(activities.get(i), new HiscoreActivity(records.get(skills.size() + i)));
			}
		}

		return builder.build();
	}

	/**
	 * Reads the {@link Skill}s from an {@link ImmutableList} of {@link CSVRecord}s.
	 * @param records The {@link CSVRecord}s.
	 * @param skills The skill names.
	 * @return An {@link ImmutableMap} of {@link Skill} names to {@link Skill}s.
	 */
	private static ImmutableMap<String, Skill> readSkills(ImmutableList<CSVRecord> records, ImmutableList<String> skills) {
		ImmutableMap.Builder<String, Skill> builder = ImmutableMap.builder();

		if (records.size() >= skills.size()) {
			for (int i = 0; i < skills.size(); i++) {
				builder.put(skills.get(i), new Skill(records.get(i)));
			}
		}

		return builder.build();
	}

	/**
	 * The web-services {@link Client}.
	 */
	private final Client client;

	/**
	 * Creates a new {@link Hiscores}.
	 * @param client The web-services {@link Client}.
	 */
	public Hiscores(Client client) {
		this.client = Preconditions.checkNotNull(client);
	}

	/**
	 * Gets a {@link Player} based on their display name.
	 * @param displayName The player's display name.
	 * @param table The table of {@link Hiscores}.
	 * @return An {@link Optional} containing the {@link Player}, or {@link Optional#empty()} if no {@link Player} was found with that name.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Top_Players">Top Players</a>
	 */
	public Optional<Player> playerInformation(String displayName, HiscoreTable table) throws IOException {
		Preconditions.checkNotNull(displayName);
		Preconditions.checkNotNull(table);

		String escapedName = NAME_SPACER.matcher(displayName).replaceAll("+");
		String url = String.format(PLAYER_INFORMATION_URL_FORMAT, table.getName(), escapedName);
		ImmutableList<CSVRecord> records = client.fromCSV(url);

		ImmutableList<String> skillNames = table.getSkillNames();
		ImmutableList<String> activityNames = table.getActivityNames();

		if (records.size() < (skillNames.size() + activityNames.size())) {
			return Optional.empty();
		}

		try {
			ImmutableMap<String, Skill> skills = readSkills(records, skillNames);
			ImmutableMap<String, HiscoreActivity> activities = readActivities(records, skillNames, activityNames);

			return Optional.of(new Player(skills, activities));
		} catch (NumberFormatException ignored) {
			/* ignore */
		}

		return Optional.empty();
	}

	/**
	 * Gets an {@link ImmutableList} of {@link ClanMate}s within a clan, based on the clan's name.
	 * @param clanName The clan's name.
	 * @return An {@link ImmutableList} of {@link ClanMate}s in the clan.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Clans">Clans</a>
	 */
	public ImmutableList<ClanMate> clanInformation(String clanName) throws IOException {
		Preconditions.checkNotNull(clanName);

		String escapedName = NAME_SPACER.matcher(clanName).replaceAll("+");
		String url = String.format(CLAN_INFORMATION_URL_FORMAT, escapedName);
		ImmutableList<CSVRecord> records = client.fromCSV(url);

		ImmutableList.Builder<ClanMate> builder = ImmutableList.builder();
		for (int i = 1; i < records.size(); i++) {
			CSVRecord record = records.get(i);

			if (record.size() == 4) {
				try {
					builder.add(new ClanMate(records.get(i)));
				} catch (NumberFormatException ignored) {
					/* ignore */
				}
			}
		}

		return builder.build();
	}
}
