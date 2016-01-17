package com.mikebull94.rsapi.hiscores;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;

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
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Ironman_modes">Ironman modes</a>
	 */
	IRONMAN("hiscore_ironman", Hiscores.SKILL_NAMES, Hiscores.ACTIVITY_NAMES),

	/**
	 * The {@link Hiscores} for hardcore ironman players.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Ironman_modes">Ironman modes</a>
	 */
	HARDCORE_IRONMAN("hiscore_hardcore_ironman", Hiscores.SKILL_NAMES, Hiscores.ACTIVITY_NAMES),

	/**
	 * The oldschool RuneScape {@link Hiscores}.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Old_School_RuneScape">Old School RuneScape</a>
	 */
	OLDSCHOOL("hiscore_oldschool", Hiscores.OLDSCHOOL_SKILL_NAMES, Hiscores.OLDSCHOOL_ACTIVITY_NAMES),

	/**
	 * The {@link Hiscores} for oldschool RuneScape ironman players.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Old_School_RuneScape">Old School RuneScape</a>
	 */
	OLDSCHOOL_IRONMAN("hiscore_oldschool_ironman", Hiscores.OLDSCHOOL_SKILL_NAMES, Hiscores.OLDSCHOOL_ACTIVITY_NAMES),

	/**
	 * The {@link Hiscores} for oldschool RuneScape ultimate ironman players.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Hiscores_APIs#Old_School_RuneScape">Old School RuneScape</a>
	 */
	OLDSCHOOL_ULTIMATE_IRONMAN("hiscore_oldschool_ultimate", Hiscores.OLDSCHOOL_SKILL_NAMES, Hiscores.OLDSCHOOL_ACTIVITY_NAMES);

	/**
	 * Gets a {@link HiscoreTable} from its name.
	 * @param name The name of the {@link HiscoreTable}.
	 * @return The {@link HiscoreTable}.
	 */
	public static HiscoreTable from(String name) {
		for (HiscoreTable table : HiscoreTable.values()) {
			if (table.name.equals(name)) {
				return table;
			}
		}
		throw new IllegalArgumentException();
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
