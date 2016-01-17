package com.mikebull94.rsapi.hiscores;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apache.commons.csv.CSVRecord;

import java.util.Objects;
import java.util.OptionalInt;
import java.util.OptionalLong;

/**
 * Represents a players skill ranking on the RuneScape {@link Hiscores}.
 */
public final class Skill {
	/**
	 * The maximum amount of experience achievable in a single skill.
	 */
	public static final long MAX_EXPERIENCE = 200_000_000;

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
	private final long experience;

	/**
	 * Creates a new {@link Skill}.
	 * @param rank The rank the player has on the {@link Hiscores} for this type of skill.
	 * @param level The level the player has in this skill.
	 * @param experience The amount of experience the player has earned in this skill.
	 */
	public Skill(int rank, int level, long experience) {
		Preconditions.checkArgument(rank == -1 || rank > 0, "Rank must be either -1 (unranked) or positive.");
		Preconditions.checkArgument(level > 0, "Level must be positive.");
		Preconditions.checkArgument(experience == -1 || experience > 0, "Experience must be either -1 (unranked) or positive.");
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
		this.experience = Long.parseLong(record.get(2));
	}

	/**
	 * Gets the rank the player has on the {@link Hiscores} for this type of skill.
	 * @return An {@link OptionalInt} containing the player's rank, or {@link OptionalInt#empty()} if the player is unranked.
	 */
	public OptionalInt getRank() {
		return rank == -1 ? OptionalInt.empty() : OptionalInt.of(rank);
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
	 * @return An {@link OptionalLong} containing the amount of experience the player has earned in this skill, or
	 * {@link OptionalLong#empty()} if the player is unranked.
	 */
	public OptionalLong getExperience() {
		return experience == -1 ? OptionalLong.empty() : OptionalLong.of(experience);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Skill skill = (Skill) o;
		return rank == skill.rank
			&& level == skill.level
			&& experience == skill.experience;
	}

	@Override
	public int hashCode() {
		return Objects.hash(rank, level, experience);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("rank", getRank().orElse(-1))
			.add("level", level)
			.add("experience", getExperience().orElse(-1L))
			.toString();
	}
}
