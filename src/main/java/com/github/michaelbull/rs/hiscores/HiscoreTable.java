package com.github.michaelbull.rs.hiscores;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

import java.util.Optional;

/**
 * Represents a type of hiscore table found on the RuneScape {@link Hiscores}.
 */
public enum HiscoreTable {

	/**
	 * The default RuneScape {@link Hiscores}.
	 */
	DEFAULT("hiscore", Hiscores.SKILL_NAMES, Hiscores.ACTIVITY_NAMES),

	/**
	 * The {@link Hiscores} for ironman players.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#Ironman_Lite">Ironman Lite</a>
	 */
	IRONMAN("hiscore_ironman", Hiscores.SKILL_NAMES, Hiscores.ACTIVITY_NAMES),

	/**
	 * The {@link Hiscores} for hardcore ironman players.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#Hardcore_Ironman_Lite">Hardcore Ironman Lite</a>
	 */
	HARDCORE_IRONMAN("hiscore_hardcore_ironman", Hiscores.SKILL_NAMES, Hiscores.ACTIVITY_NAMES),

	/**
	 * The oldschool RuneScape {@link Hiscores}.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#Old_School_Hiscores">Old School Hiscores</a>
	 */
	OLDSCHOOL("hiscore_oldschool", Hiscores.OLDSCHOOL_SKILL_NAMES, Hiscores.OLDSCHOOL_ACTIVITY_NAMES),

	/**
	 * The {@link Hiscores} for oldschool RuneScape ironman players.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#Ironman_Lite_2">Old School Ironman Hiscores</a>
	 */
	OLDSCHOOL_IRONMAN("hiscore_oldschool_ironman", Hiscores.OLDSCHOOL_SKILL_NAMES, Hiscores.OLDSCHOOL_ACTIVITY_NAMES),

	/**
	 * The {@link Hiscores} for oldschool RuneScape ultimate ironman players.
	 * @see <a href="https://runescape.wiki/w/Application_programming_interface#Ultimate_Ironman_Lite">Old School Ultimate Ironman Hiscores</a>
	 */
	OLDSCHOOL_ULTIMATE_IRONMAN("hiscore_oldschool_ultimate", Hiscores.OLDSCHOOL_SKILL_NAMES, Hiscores.OLDSCHOOL_ACTIVITY_NAMES);

	/**
	 * Gets a {@link HiscoreTable} from its {@link #name}.
	 * @param name The name of the {@link HiscoreTable}.
	 * @return The {@link HiscoreTable} or {@link Optional#empty()} if no table was found.
	 */
	public static Optional<HiscoreTable> from(String name) {
		for (HiscoreTable table : HiscoreTable.values()) {
			if (table.name.equals(name)) {
				return Optional.of(table);
			}
		}
		return Optional.empty();
	}

	/**
	 * The name of this hiscore table.
	 */
	private final String name;

	/**
	 * The names of skills found on this hiscore board.
	 */
	private final ImmutableList<String> skillNames;

	/**
	 * The names of activities found on this hiscore board.
	 */
	private final ImmutableList<String> activityNames;

	/**
	 * Creates a new {@link HiscoreTable}.
	 * @param name The name.
	 * @param skillNames The skill names.
	 * @param activityNames The activity names.
	 */
	HiscoreTable(String name, ImmutableList<String> skillNames, ImmutableList<String> activityNames) {
		this.name = Preconditions.checkNotNull(name);
		this.skillNames = Preconditions.checkNotNull(skillNames);
		this.activityNames = Preconditions.checkNotNull(activityNames);
	}

	/**
	 * Gets the name of this hiscore table.
	 * @return The name of this hiscore table.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the names of skills found on this hiscore table.
	 * @return The names of skills found on this hiscore table.
	 */
	public ImmutableList<String> getSkillNames() {
		return skillNames;
	}

	/**
	 * Gets the names of activities found on this hiscore table.
	 * @return The names of activities found on this hiscore table.
	 */
	public ImmutableList<String> getActivityNames() {
		return activityNames;
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.add("skillNames", skillNames)
			.add("activityNames", activityNames)
			.toString();
	}
}
