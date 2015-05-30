package com.runescape.api.hiscores.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.hiscores.Hiscores;

import java.util.Objects;

/**
 * Represents a player ranked on the RuneScape {@link Hiscores}.
 */
public final class Player {
	/**
	 * An {@link ImmutableMap} of {@link Skill} names to the player's {@link Skill} rankings.
	 */
	private final ImmutableMap<String, Skill> skills;

	/**
	 * An {@link ImmutableMap} of {@link Activity} names to the player's {@link Activity} rankings.
	 */
	private final ImmutableMap<String, Activity> activities;

	/**
	 * Creates a new {@link Player}.
	 * @param skills The player's skill rankings.
	 * @param activities The player's activity rankings.
	 */
	public Player(ImmutableMap<String, Skill> skills, ImmutableMap<String, Activity> activities) {
		this.skills = Preconditions.checkNotNull(skills);
		this.activities = Preconditions.checkNotNull(activities);
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link Skill} names to the player's {@link Skill} rankings.
	 * @return An {@link ImmutableMap} of {@link Skill} names to the player's {@link Skill} rankings.
	 */
	public ImmutableMap<String, Skill> getSkills() {
		return skills;
	}

	/**
	 * Gets an {@link ImmutableMap} of {@link Activity} names to the player's {@link Activity} rankings.
	 * @return An {@link ImmutableMap} of {@link Activity} names to the player's {@link Activity} rankings.
	 */
	public ImmutableMap<String, Activity> getActivities() {
		return activities;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Player player = (Player) o;
		return Objects.equals(skills, player.skills) &&
			Objects.equals(activities, player.activities);
	}

	@Override
	public int hashCode() {
		return Objects.hash(skills, activities);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("skills", skills)
			.add("activities", activities)
			.toString();
	}
}
