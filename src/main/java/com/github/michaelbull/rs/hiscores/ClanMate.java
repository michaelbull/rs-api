package com.github.michaelbull.rs.hiscores;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.primitives.Ints;
import com.google.common.primitives.Longs;
import org.apache.commons.csv.CSVRecord;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents a member of a clan.
 */
public final class ClanMate {

	/**
	 * Creates a {@link ClanMate} from a {@link CSVRecord}.
	 * @param record The record.
	 * @return The {@link ClanMate} or {@link Optional#empty()} if the record was invalid.
	 */
	public static Optional<ClanMate> fromCsv(CSVRecord record) {
		if (record.size() < 4) {
			return Optional.empty();
		}

		String name = record.get(0);
		String rank = record.get(1);

		Long experience = Longs.tryParse(record.get(2));
		if (experience == null) {
			return Optional.empty();
		}

		Integer kills = Ints.tryParse(record.get(3));
		if (kills == null) {
			return Optional.empty();
		}

		return Optional.of(new ClanMate(name, rank, experience, kills));
	}

	/**
	 * The name of the clan mate.
	 */
	private final String name;

	/**
	 * The rank the clan mate has in the clan.
	 */
	private final String rank;

	/**
	 * The experience contributed to the clan by this clan mate.
	 */
	private final long experience;

	/**
	 * The amount of kills earned by the clan clan mate.
	 */
	private final int kills;

	/**
	 * Creates a new {@link ClanMate}.
	 * @param name The name of the clan mate.
	 * @param rank The rank of the clan mate.
	 * @param experience The contributed experience of the clan mate.
	 * @param kills The amount of earned kills by the clan mate.
	 */
	public ClanMate(String name, String rank, long experience, int kills) {
		Preconditions.checkArgument(experience >= 0, "Experience must be non-negative.");
		Preconditions.checkArgument(kills >= 0, "Kills must be more be non-negative.");
		this.name = Preconditions.checkNotNull(name);
		this.rank = Preconditions.checkNotNull(rank);
		this.experience = experience;
		this.kills = kills;
	}

	/**
	 * Creates a new {@link ClanMate} from a {@link CSVRecord}.
	 * @param record The {@link CSVRecord}.
	 * @deprecated Use {@link #fromCsv(CSVRecord)}.
	 */
	@Deprecated
	public ClanMate(CSVRecord record) {
		this(record.get(0), record.get(1), Long.parseLong(record.get(2)), Integer.parseInt(record.get(3)));
	}

	/**
	 * Gets the name of this clan mate.
	 * @return The name of this clan mate.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the rank of this clan mate.
	 * @return The rank of this clan mate.
	 */
	public String getRank() {
		return rank;
	}

	/**
	 * Gets the experience contributed to the clan by this clan mate.
	 * @return The experience contributed to the clan by this clan mate.
	 */
	public long getExperience() {
		return experience;
	}

	/**
	 * Gets the amount of kills earned by this clan mate.
	 * @return The amount of kills earned by this clan mate.
	 */
	public int getKills() {
		return kills;
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		ClanMate clanMate = (ClanMate) o;
		return experience == clanMate.experience
			&& kills == clanMate.kills
			&& Objects.equals(name, clanMate.name)
			&& Objects.equals(rank, clanMate.rank);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, rank, experience, kills);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.add("rank", rank)
			.add("experience", experience)
			.add("kills", kills)
			.toString();
	}
}
