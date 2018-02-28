package com.github.michaelbull.rs.hiscores;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

import java.util.Objects;

/**
 * Represents a player ranked on the RuneScape {@link Hiscores}.
 */
public final class Player {
	public static final class Builder {
		private final ImmutableMap.Builder<String, Skill> skills = ImmutableMap.builder();
		private final ImmutableMap.Builder<String, HiscoreActivity> activities = ImmutableMap.builder();

		private Builder() {
			/* empty */
		}

		public Builder skill(String name, Skill skill) {
			skills.put(name, skill);
			return this;
		}

		public Builder activity(String name, HiscoreActivity activity) {
			activities.put(name, activity);
			return this;
		}

		public Player build() {
			return new Player(skills.build(), activities.build());
		}
	}

	public static Builder builder() {
		return new Builder();
	}

	/**
	 * An {@link ImmutableMap} of {@link Skill} names to the player's {@link Skill} rankings.
	 */
	private final ImmutableMap<String, Skill> skills;

	/**
	 * An {@link ImmutableMap} of {@link HiscoreActivity} names to the player's {@link HiscoreActivity} rankings.
	 */
	private final ImmutableMap<String, HiscoreActivity> activities;

	/**
	 * Creates a new {@link Player}.
	 * @param skills The player's skill rankings.
	 * @param activities The player's activity rankings.
	 */
	public Player(ImmutableMap<String, Skill> skills, ImmutableMap<String, HiscoreActivity> activities) {
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
	 * Gets an {@link ImmutableMap} of {@link HiscoreActivity} names to the player's {@link HiscoreActivity} rankings.
	 * @return An {@link ImmutableMap} of {@link HiscoreActivity} names to the player's {@link HiscoreActivity} rankings.
	 */
	public ImmutableMap<String, HiscoreActivity> getActivities() {
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
		return Objects.equals(skills, player.skills)
			&& Objects.equals(activities, player.activities);
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
