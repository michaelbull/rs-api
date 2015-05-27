package com.runescape.api.bestiary.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.runescape.api.bestiary.Bestiary;

import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

/**
 * Represents a beast in the RuneScape {@link Bestiary}.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#Beast_Data">Beast Data</a>
 */
public final class Beast {
	/**
	 * The name.
	 */
	private final String name;

	/**
	 * The id.
	 */
	private final int id;

	/**
	 * The description.
	 */
	private final String description;

	/**
	 * The weakness.
	 */
	private final String weakness;

	/**
	 * A flag indicating if it is attackable or not.
	 */
	private final boolean attackable;

	/**
	 * A flag indicating if it is aggressive or not.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Aggressive">Aggressive</a>
	 */
	private final boolean aggressive;

	/**
	 * A flag indicating if it is poisonous or not.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Poison">Poison</a>
	 */
	private final boolean poisonous;

	/**
	 * The amount of experience gained when killing this beast.
	 */
	private final String xp;

	/**
	 * The lifepoints.
	 */
	private final int lifepoints;

	/**
	 * The combat level.
	 */
	private final int level;

	/**
	 * The Defence level.
	 */
	private final int defence;

	/**
	 * The Attack level.
	 */
	private final int attack;

	/**
	 * The Magic level.
	 */
	private final int magic;

	/**
	 * The Ranged level.
	 */
	private final int ranged;

	/**
	 * The Slayer level required to attack this beast.
	 */
	private final int slayerlevel;

	/**
	 * The amount of tiles this beast occupies in both the x and y dimension.
	 */
	private final int size;

	/**
	 * A flag indicating whether this beast is only available on members' worlds.
	 */
	private final boolean members;

	/**
	 * The Slayer category this beast belongs to.
	 */
	private final String slayercat;

	/**
	 * The areas this beast is found in.
	 */
	private final String[] areas;

	/**
	 * A {@link Map} of animation names to animation ids.
	 */
	private final Map<String, Integer> animations;

	/**
	 * Creates a new {@link Beast}.
	 * @param name The name.
	 * @param id The id.
	 * @param description The description.
	 * @param weakness The weakness.
	 * @param attackable The attackable flag.
	 * @param aggressive The aggressive flag.
	 * @param poisonous The poisonous flag.
	 * @param xp The experience earned.
	 * @param lifepoints The lifepoints.
	 * @param level The combat level.
	 * @param defence The Defence level.
	 * @param attack The Attack level.
	 * @param magic The Magic level.
	 * @param ranged The Ranged level.
	 * @param slayerlevel The Slayer level requirement.
	 * @param size The tile size.
	 * @param members The members only flag.
	 * @param slayercat The Slayer category.
	 * @param areas The areas.
	 * @param animations The animations.
	 */
	public Beast(String name, int id, String description, String weakness, boolean attackable, boolean aggressive, boolean poisonous, String xp, int lifepoints, int level, int defence, int attack, int magic, int ranged, int slayerlevel, int size, boolean members, String slayercat, String[] areas, Map<String, Integer> animations) {
		this.name = Preconditions.checkNotNull(name);
		this.id = Preconditions.checkNotNull(id);
		this.description = Preconditions.checkNotNull(description);
		this.weakness = Preconditions.checkNotNull(weakness);
		this.attackable = Preconditions.checkNotNull(attackable);
		this.aggressive = Preconditions.checkNotNull(aggressive);
		this.poisonous = Preconditions.checkNotNull(poisonous);
		this.xp = Preconditions.checkNotNull(xp);
		this.lifepoints = Preconditions.checkNotNull(lifepoints);
		this.level = Preconditions.checkNotNull(level);
		this.defence = Preconditions.checkNotNull(defence);
		this.attack = Preconditions.checkNotNull(attack);
		this.magic = Preconditions.checkNotNull(magic);
		this.ranged = Preconditions.checkNotNull(ranged);
		this.slayerlevel = Preconditions.checkNotNull(slayerlevel);
		this.size = Preconditions.checkNotNull(size);
		this.members = Preconditions.checkNotNull(members);
		this.slayercat = Preconditions.checkNotNull(slayercat);
		this.areas = Preconditions.checkNotNull(areas);
		this.animations = Preconditions.checkNotNull(animations);
	}

	/**
	 * Gets the beast's name.
	 * @return The beast's name,
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the beast's id.
	 * @return The beast's id.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the beast's description.
	 * @return The beast's description.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the beast's weakness.
	 * @return The beast's weakness.
	 */
	public String getWeakness() {
		return weakness;
	}

	/**
	 * Checks if the beast is attackable.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isAttackable() {
		return attackable;
	}

	/**
	 * Checks if the beast is aggressive.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isAggressive() {
		return aggressive;
	}

	/**
	 * Checks if this beast is poisonous.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isPoisonous() {
		return poisonous;
	}

	/**
	 * Gets the amount of experience earned when killing this beast.
	 * @return The amount of experience earned when killing this beast.
	 */
	public double getExperience() {
		return xp == null ? 0 : Double.parseDouble(xp);
	}

	/**
	 * Gets the amount of life points the beast has.
	 * @return The amount of life points the beast has.
	 */
	public int getLifePoints() {
		return lifepoints;
	}

	/**
	 * Gets the beast's combat level.
	 * @return The beast's combat level.
	 */
	public int getCombatLevel() {
		return level;
	}

	/**
	 * Gets the beast's Defence level.
	 * @return The beast's Defence level.
	 */
	public int getDefenceLevel() {
		return defence;
	}

	/**
	 * Gets the beast's Attack level.
	 * @return The beast's Attack level.
	 */
	public int getAttackLevel() {
		return attack;
	}

	/**
	 * Gets the beast's Magic level.
	 * @return The beast's Magic level.
	 */
	public int getMagicLevel() {
		return magic;
	}

	/**
	 * Gets the beast's Ranged level.
	 * @return The beast's Ranged level.
	 */
	public int getRangedLevel() {
		return ranged;
	}

	/**
	 * Gets the Slayer level required to attack this beast.
	 * @return The Slayer level required to attack this beast.
	 */
	public int getSlayerLevelRequirement() {
		return slayerlevel;
	}

	/**
	 * Gets the amount of tiles this beast occupies in the x and y dimension.
	 * @return The amount of tiles this beast occupies in the x and y dimension.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Checks if this beast is only accessible on member's worlds.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isMembersOnly() {
		return members;
	}

	/**
	 * Gets an {@link Optional} of the Slayer category this beast belongs to.
	 * @return An {@link Optional} of the Slayer category this beast belongs to, or {@code Optional.empty()} if it does not belong to a Slayer category.
	 */
	public Optional<String> getSlayerCategory() {
		return Optional.ofNullable(slayercat);
	}

	/**
	 * Gets an {@link ImmutableList} of areas in which this beast is found.
	 * @return An {@link ImmutableList} of areas in which this beast is found.
	 */
	public ImmutableList<String> getAreas() {
		return ImmutableList.copyOf(areas);
	}

	/**
	 * Gets an {@link ImmutableMap} of animation names to animation ids.
	 * @return An {@link ImmutableMap} of animation names to animation ids.
	 */
	public ImmutableMap<String, Integer> getAnimations() {
		return ImmutableMap.copyOf(animations);
	}

	/**
	 * Gets an {@link Optional} of an animation's id by its name.
	 * @param name The name of the animation.
	 * @return An {@link Optional} of an animation's id, or {@code Optional.empty()} if no animation of that name was found.
	 */
	public Optional<Integer> getAnimation(String name) {
		return Optional.ofNullable(animations.get(name));
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.add("id", id)
			.add("description", description)
			.add("weakness", weakness)
			.add("attackable", attackable)
			.add("aggressive", aggressive)
			.add("poisonous", poisonous)
			.add("experience", getExperience())
			.add("lifePoints", lifepoints)
			.add("combatLevel", level)
			.add("defence", defence)
			.add("attack", attack)
			.add("magic", magic)
			.add("ranged", ranged)
			.add("slayerLevelRequirement", slayerlevel)
			.add("size", size)
			.add("membersOnly", members)
			.add("slayerCategory", getSlayerCategory().orElse("none"))
			.add("areas", Arrays.toString(areas))
			.add("animations", animations)
			.toString();
	}
}
