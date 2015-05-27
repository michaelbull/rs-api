package com.runescape.api.hiscores.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.runescape.api.hiscores.Hiscores;
import org.apache.commons.csv.CSVRecord;

import java.util.Optional;

/**
 * Represents a player's rank in an activity on the {@link Hiscores}.
 */
public final class Activity {
	/**
	 * The rank the player has in the activity.
	 */
	private final int rank;

	/**
	 * The score the player has in the activity.
	 */
	private final int score;

	/**
	 * Creates a new {@link Activity}.
	 * @param rank The rank the player has in the activity.
	 * @param score The score the player has in the activity.
	 */
	public Activity(int rank, int score) {
		this.rank = rank;
		this.score = score;
	}

	/**
	 * Creates a new {@link Activity} from a {@link CSVRecord}.
	 * @param record The {@link CSVRecord}.
	 */
	public Activity(CSVRecord record) {
		Preconditions.checkNotNull(record);
		this.rank = Integer.parseInt(record.get(0));
		this.score = Integer.parseInt(record.get(1));
	}

	/**
	 * Gets the rank the player has in the activity.
	 * @return An {@link Optional} of the player's rank, or {@code Optional.empty()} if the player is unranked for this activity.
	 */
	public Optional<Integer> getRank() {
		return rank == -1 ? Optional.empty() : Optional.of(rank);
	}

	/**
	 * Gets the score the player has in the activity.
	 * @return An {@link Optional} of the player's score, or {@code Optional.empty()} if the player is unranked for this activity.
	 */
	public Optional<Integer> getScore() {
		return rank == -1 ? Optional.empty() : Optional.of(score);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("rank", getRank().orElse(-1))
			.add("score", getScore().orElse(-1))
			.toString();
	}
}
