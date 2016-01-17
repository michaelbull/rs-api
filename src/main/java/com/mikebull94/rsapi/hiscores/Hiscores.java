package com.mikebull94.rsapi.hiscores;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.mikebull94.rsapi.Client;
import com.mikebull94.rsapi.HttpClient;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Collection;
import java.util.Objects;
import java.util.Optional;
import java.util.regex.Pattern;

/**
 * Represents the RuneScape Hiscores API.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs">Hiscores APIs</a>
 */
public final class Hiscores {
	/**
	 * The URL to the clan hiscores web-service.
	 */
	private static final String CLAN_HISCORES_URL = HttpClient.WEB_SERVICES_URL + "/m=clan-hiscores/members_lite.ws";

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
		"Divination"
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
		"AF15: Rats killed after the miniquest"
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
		Preconditions.checkArgument(records.size() >= (skills.size() + activities.size()));

		ImmutableMap.Builder<String, HiscoreActivity> builder = ImmutableMap.builder();

		for (int i = 0; i < activities.size(); i++) {
			builder.put(activities.get(i), new HiscoreActivity(records.get(skills.size() + i)));
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
		Preconditions.checkArgument(records.size() >= skills.size());

		ImmutableMap.Builder<String, Skill> builder = ImmutableMap.builder();

		for (int i = 0; i < skills.size(); i++) {
			builder.put(skills.get(i), new Skill(records.get(i)));
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

		ImmutableList<CSVRecord> records = client.fromCSV(HttpClient.WEB_SERVICES_URL + "/m=" + table.getName() + "/index_lite.ws?player=" + NAME_SPACER.matcher(displayName).replaceAll("+"));

		ImmutableList<String> skillNames = table.getSkillNames();
		ImmutableList<String> activityNames = table.getActivityNames();

		if (records.size() != (skillNames.size() + activityNames.size())) {
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

		ImmutableList<CSVRecord> records = client.fromCSV(CLAN_HISCORES_URL + "?clanName=" + NAME_SPACER.matcher(clanName).replaceAll("+"));
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

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Hiscores hiscores = (Hiscores) o;
		return Objects.equals(client, hiscores.client);
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
