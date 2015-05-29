package com.runescape.api.hiscores;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.Client;
import com.runescape.api.hiscores.model.Activity;
import com.runescape.api.hiscores.model.ClanMate;
import com.runescape.api.hiscores.model.Player;
import com.runescape.api.hiscores.model.Skill;
import com.runescape.api.hiscores.model.Table;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.util.Optional;

/**
 * Represents the RuneScape Hiscores API.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs">Hiscores APIs</a>
 */
public final class Hiscores {
	/**
	 * The URL to the clan hiscores web-service.
	 */
	private static final String CLAN_HISCORES_URL = Client.WEB_SERVICES_URL + "/m=clan-hiscores/members_lite.ws";

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
	 * Reads the {@link Skill}s from an {@link ImmutableList} of {@link CSVRecord}s.
	 * @param records The {@link CSVRecord}s.
	 * @param skillNames The skill names.
	 * @return An {@link ImmutableMap} of {@link Skill} names to {@link Skill}s.
	 */
	private ImmutableMap<String, Skill> readSkills(ImmutableList<CSVRecord> records, ImmutableList<String> skillNames) {
		Preconditions.checkNotNull(records);
		Preconditions.checkNotNull(skillNames);
		ImmutableMap.Builder<String, Skill> builder = ImmutableMap.builder();
		for (int i = 0; i < skillNames.size(); i++) {
			builder.put(skillNames.get(i), new Skill(records.get(i)));
		}
		return builder.build();
	}

	/**
	 * Reads the {@link Activity}s from an {@link ImmutableList} of {@link CSVRecord}s.
	 * @param records The {@link CSVRecord}s.
	 * @param skillNames The skill names.
	 * @param activityNames The activity names.
	 * @return An {@link ImmutableMap} of {@link Activity} names to {@link Activity}s.
	 */
	private ImmutableMap<String, Activity> readActivities(ImmutableList<CSVRecord> records, ImmutableList<String> skillNames, ImmutableList<String> activityNames) {
		Preconditions.checkNotNull(records);
		Preconditions.checkNotNull(skillNames);
		Preconditions.checkNotNull(activityNames);
		ImmutableMap.Builder<String, Activity> builder = ImmutableMap.builder();
		for (int i = 0; i < activityNames.size(); i++) {
			builder.put(activityNames.get(i), new Activity(records.get(skillNames.size() + i)));
		}
		return builder.build();
	}

	/**
	 * Gets a {@link Player} based on their display name.
	 * @param displayName The player's display name.
	 * @param table The table of {@link Hiscores}.
	 * @return An {@link Optional} of the {@link Player}, or {@code Optional.empty()} if no {@link Player} was found with that name.
	 * @throws IOException If an I/O error occurs.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Top_Players">Top Players</a>
	 */
	public Optional<Player> playerInformation(String displayName, Table table) throws IOException {
		Preconditions.checkNotNull(displayName);
		Preconditions.checkNotNull(table);

		ImmutableList<CSVRecord> records = client.fromCSV(Client.WEB_SERVICES_URL + "/m=" + table.getName() + "/index_lite.ws?player=" + displayName.replaceAll(" ", "+"));

		try {
			ImmutableMap<String, Skill> skills = readSkills(records, table.getSkillNames());
			ImmutableMap<String, Activity> activities = readActivities(records, table.getSkillNames(), table.getActivityNames());

			return Optional.of(new Player(skills, activities));
		} catch (Exception e) {
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

		ImmutableList<CSVRecord> records = client.fromCSV(CLAN_HISCORES_URL + "?clanName=" + clanName.replaceAll(" ", "+"));
		ImmutableList.Builder<ClanMate> builder = ImmutableList.builder();

		for (int i = 1; i < records.size(); i++) {
			CSVRecord record = records.get(i);
			if (record.size() == 4) {
				try {
					builder.add(new ClanMate(records.get(i)));
				} catch (NumberFormatException e) {
					/* ignore */
				}
			}
		}

		return builder.build();
	}
}
