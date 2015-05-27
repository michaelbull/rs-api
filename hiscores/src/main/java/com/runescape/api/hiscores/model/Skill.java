package com.runescape.api.hiscores.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.runescape.api.hiscores.Hiscores;
import org.apache.commons.csv.CSVRecord;

import java.util.Optional;

/**
 * Represents a players skill ranking on the RuneScape {@link Hiscores}.
 */
public final class Skill {
	/**
	 * The rank the player has on the {@link Hiscores} for this type of skill.
	 */
	private final int rank;

	/**
	 * The level the player has in this skill.
	 */
	private final int level;

	/**
	 * The amount of experience the player has earned in this skill.
	 */
	private final int experience;

	/**
	 * Creates a new {@link Skill}.
	 * @param rank The rank the player has on the {@link Hiscores} for this type of skill.
	 * @param level The level the player has in this skill.
	 * @param experience The amount of experience the player has earned in this skill.
	 */
	public Skill(int rank, int level, int experience) {
		this.rank = rank;
		this.level = level;
		this.experience = experience;
	}

	/**
	 * Creates a new {@link Skill} from a {@link CSVRecord}.
	 * @param record The {@link CSVRecord}.
	 */
	public Skill(CSVRecord record) {
		Preconditions.checkNotNull(record);
		this.rank = Integer.parseInt(record.get(0));
		this.level = Integer.parseInt(record.get(1));
		this.experience = Integer.parseInt(record.get(2));
	}

	/**
	 * Gets the rank the player has on the {@link Hiscores} for this type of skill.
	 * @return An {@link Optional} of the player's rank, or {@code Optional.empty()} if the player is unranked.
	 */
	public Optional<Integer> getRank() {
		return rank == -1 ? Optional.empty() : Optional.of(rank);
	}

	/**
	 * Gets the level the player has in this skill.
	 * @return The level the player has in this skill.
	 */
	public int getLevel() {
		return level;
	}

	/**
	 * Gets the amount of experience the player has earned in this skill.
	 * @return The amount of experience the player has earned in this skill.
	 */
	public Optional<Integer> getExperience() {
		return experience == -1 ? Optional.empty() : Optional.of(experience);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("rank", getRank().orElse(-1))
			.add("level", level)
			.add("experience", getExperience().orElse(-1))
			.toString();
	}
}
