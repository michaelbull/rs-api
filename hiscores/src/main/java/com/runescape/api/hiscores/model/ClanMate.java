package com.runescape.api.hiscores.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import org.apache.commons.csv.CSVRecord;

/**
 * Represents a member of a clan.
 */
public final class ClanMate {
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
	private final int experience;

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
	public ClanMate(String name, String rank, int experience, int kills) {
		this.name = Preconditions.checkNotNull(name);
		this.rank = Preconditions.checkNotNull(rank);
		this.experience = experience;
		this.kills = kills;
	}

	/**
	 * Creates a new {@link ClanMate} from a {@link CSVRecord}.
	 * @param record The {@link CSVRecord}.
	 */
	public ClanMate(CSVRecord record) {
		this(record.get(0), record.get(1), Integer.parseInt(record.get(2)), Integer.parseInt(record.get(3)));
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
	public int getExperience() {
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
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.add("rank", rank)
			.add("experience", experience)
			.add("kills", kills)
			.toString();
	}
}
