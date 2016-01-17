package com.mikebull94.rsapi.bestiary;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.gson.annotations.SerializedName;

import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.OptionalInt;
import java.util.Set;

/**
 * Represents a beast in the RuneScape {@link Bestiary}.
 * @see <a href="http://services.runescape.com/m=rswiki/en/Bestiary_APIs#Beast_Data">Beast Data</a>
 */
public final class Beast {
	public static final class Builder {
		private boolean built = false;

		private String name = "";
		private final int id;
		private String description = "";
		private String weakness = "None";
		private boolean attackable = false;
		private boolean aggressive = false;
		private boolean poisonous = false;
		private double experience = 0;
		private int lifePoints = 0;
		private int combatLevel = 0;
		private int defenceLevel = 0;
		private int attackLevel = 0;
		private int magicLevel = 0;
		private int rangedLevel = 0;
		private int requiredSlayerLevel = 0;
		private int size = 1;
		private boolean membersOnly = false;
		private String slayerCategory;
		private final Set<String> areas = new HashSet<>();
		private final Map<String, Integer> animations = new HashMap<>();

		private Builder(int id) {
			this.id = id;
		}

		public Builder name(String name) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.name.isEmpty(), "Name already set.");
			Preconditions.checkArgument(!name.isEmpty(), "Name must not be empty.");
			this.name = name;
			return this;
		}

		public Builder description(String description) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.description.isEmpty(), "Description already set.");
			Preconditions.checkArgument(!description.isEmpty(), "Description must not be empty.");
			this.description = description;
			return this;
		}

		public Builder weakness(String weakness) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.weakness.equals("None"), "Weakness already set.");
			Preconditions.checkArgument(!weakness.isEmpty(), "Weakness must not be empty.");
			Preconditions.checkArgument(!weakness.equals("None"), "Weakness must not be \"None\".");
			this.weakness = weakness;
			return this;
		}

		public Builder attackable() {
			Preconditions.checkState(!built);
			Preconditions.checkState(!attackable, "Attackable already set to true.");
			this.attackable = true;
			return this;
		}

		public Builder aggressive() {
			Preconditions.checkState(!built);
			Preconditions.checkState(!aggressive, "Aggressive already set to true.");
			this.aggressive = true;
			return this;
		}

		public Builder poisonous() {
			Preconditions.checkState(!built);
			Preconditions.checkState(!poisonous, "Poisonous already set to true.");
			this.poisonous = true;
			return this;
		}

		public Builder experience(double experience) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.experience == 0, "Experience already set.");
			Preconditions.checkArgument(experience > 0, "Experience must be positive.");
			this.experience = experience;
			return this;
		}

		public Builder lifePoints(int lifePoints) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.lifePoints == 0, "Life points already set.");
			Preconditions.checkArgument(lifePoints > 0, "Life points must be positive.");
			this.lifePoints = lifePoints;
			return this;
		}

		public Builder combatLevel(int combatLevel) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.combatLevel == 0, "Combat level already set.");
			Preconditions.checkArgument(combatLevel > 0, "Combat level must be positive.");
			this.combatLevel = combatLevel;
			return this;
		}

		public Builder defenceLevel(int defenceLevel) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.defenceLevel == 0, "Defence level already set.");
			Preconditions.checkArgument(defenceLevel > 0, "Defence level must be positive.");
			this.defenceLevel = defenceLevel;
			return this;
		}

		public Builder attackLevel(int attackLevel) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.attackLevel == 0, "Attack level already set.");
			Preconditions.checkArgument(attackLevel > 0, "Attack level must be positive.");
			this.attackLevel = attackLevel;
			return this;
		}

		public Builder magicLevel(int magicLevel) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.magicLevel == 0, "Magic level already set.");
			Preconditions.checkArgument(magicLevel > 0, "Magic level must be positive.");
			this.magicLevel = magicLevel;
			return this;
		}

		public Builder rangedLevel(int rangedLevel) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.rangedLevel == 0, "Ranged level already set.");
			Preconditions.checkArgument(rangedLevel > 0, "Ranged level must be positive.");
			this.rangedLevel = rangedLevel;
			return this;
		}

		public Builder requiredSlayerLevel(int requiredSlayerLevel) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.requiredSlayerLevel == 0, "Required Slayer level already set.");
			Preconditions.checkArgument(requiredSlayerLevel > 0, "Required Slayer level must be positive.");
			this.requiredSlayerLevel = requiredSlayerLevel;
			return this;
		}

		public Builder size(int size) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.size == 1, "Size already set.");
			Preconditions.checkArgument(size > 1, "Size must be positive.");
			this.size = size;
			return this;
		}

		public Builder membersOnly() {
			Preconditions.checkState(!built);
			Preconditions.checkState(!membersOnly, "Members only already set to true.");
			this.membersOnly = true;
			return this;
		}

		public Builder slayerCategory(String slayerCategory) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.slayerCategory == null, "Slayer category already set.");
			Preconditions.checkArgument(!slayerCategory.isEmpty(), "Slayer category must not be empty.");
			this.slayerCategory = slayerCategory;
			return this;
		}

		public Builder area(String area) {
			Preconditions.checkState(!built);
			Preconditions.checkArgument(!area.isEmpty(), "Area must not be empty.");
			this.areas.add(area);
			return this;
		}

		public Builder animation(String name, int id) {
			Preconditions.checkState(!built);
			Preconditions.checkArgument(!name.isEmpty(), "Animation name must not be empty.");
			Preconditions.checkArgument(id > 0, "Animation id must be positive.");
			this.animations.put(name, id);
			return this;
		}

		public Beast build() {
			Preconditions.checkState(!built);
			built = true;

			return new Beast(
				name,
				id,
				description,
				weakness,
				attackable,
				aggressive,
				poisonous,
				experience,
				lifePoints,
				combatLevel,
				defenceLevel,
				attackLevel,
				magicLevel,
				rangedLevel,
				requiredSlayerLevel,
				size,
				membersOnly,
				slayerCategory,
				areas.toArray(new String[areas.size()]),
				animations
			);
		}
	}

	public static Builder builder(int id) {
		return new Builder(id);
	}

	/**
	 * The name of this beast.
	 */
	private final String name;

	/**
	 * The unique ID number of this beast.
	 */
	private final int id;

	/**
	 * The description of this beast.
	 */
	private final String description;

	/**
	 * The weakness of this beast, empty if it has no weakness.
	 */
	private final String weakness;

	/**
	 * A flag indicating if this beast is attackable or not.
	 */
	private final boolean attackable;

	/**
	 * A flag indicating if this beast is aggressive or not.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Aggressive">Aggressive</a>
	 */
	private final boolean aggressive;

	/**
	 * A flag indicating if this beast is poisonous or not.
	 * @see <a href="http://services.runescape.com/m=rswiki/en/Poison">Poison</a>
	 */
	private final boolean poisonous;

	/**
	 * The amount of experience gained when killing this beast.
	 */
	@SerializedName("xp")
	private final String experience;

	/**
	 * The life points of this beast.
	 */
	@SerializedName("lifepoints")
	private final int lifePoints;

	/**
	 * The combat combatLevel of this beast.
	 */
	@SerializedName("level")
	private final int combatLevel;

	/**
	 * The Defence level of this beast.
	 */
	@SerializedName("defence")
	private final int defenceLevel;

	/**
	 * The Attack level of this beast.
	 */
	@SerializedName("attack")
	private final int attackLevel;

	/**
	 * The Magic level of this beast.
	 */
	@SerializedName("magic")
	private final int magicLevel;

	/**
	 * The Ranged level of this beast.
	 */
	@SerializedName("ranged")
	private final int rangedLevel;

	/**
	 * The Slayer level required to attackLevel this beast.
	 */
	@SerializedName("slayerlevel")
	private final int requiredSlayerLevel;

	/**
	 * The amount of tiles this beast occupies in both the x and y dimension.
	 */
	private final int size;

	/**
	 * A flag indicating whether this beast is only available on members' worlds.
	 */
	@SerializedName("members")
	private final boolean membersOnly;

	/**
	 * The Slayer category this beast belongs to.
	 */
	@SerializedName("slayercat")
	private final String slayerCategory;

	/**
	 * The areas this beast is found in.
	 */
	private final String[] areas;

	/**
	 * A {@link Map} of this beast's animation names to animation ids.
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
	 * @param experience The experience earned.
	 * @param lifePoints The life points.
	 * @param combatLevel The combat level.
	 * @param defenceLevel The Defence level.
	 * @param attackLevel The Attack level.
	 * @param magicLevel The Magic level.
	 * @param rangedLevel The Ranged level.
	 * @param requiredSlayerLevel The Slayer level requirement.
	 * @param size The tile size.
	 * @param membersOnly The members only flag.
	 * @param slayerCategory The Slayer category.
	 * @param areas The areas.
	 * @param animations The animations.
	 */
	private Beast(String name, int id, String description, String weakness, boolean attackable, boolean aggressive, boolean poisonous, double experience, int lifePoints, int combatLevel, int defenceLevel, int attackLevel, int magicLevel, int rangedLevel, int requiredSlayerLevel, int size, boolean membersOnly, String slayerCategory, String[] areas, Map<String, Integer> animations) {
		this.name = Preconditions.checkNotNull(name);
		this.id = id;
		this.description = Preconditions.checkNotNull(description);
		this.weakness = weakness;
		this.attackable = attackable;
		this.aggressive = aggressive;
		this.poisonous = poisonous;
		this.experience = String.valueOf(experience);
		this.lifePoints = lifePoints;
		this.combatLevel = combatLevel;
		this.defenceLevel = defenceLevel;
		this.attackLevel = attackLevel;
		this.magicLevel = magicLevel;
		this.rangedLevel = rangedLevel;
		this.requiredSlayerLevel = requiredSlayerLevel;
		this.size = size;
		this.membersOnly = membersOnly;
		this.slayerCategory = slayerCategory;
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
	public Optional<String> getWeakness() {
		if (weakness != null && weakness.equals("None")) {
			return Optional.empty();
		}
		return Optional.ofNullable(weakness);
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
		return experience == null ? 0 : Double.parseDouble(experience);
	}

	/**
	 * Gets the amount of life points the beast has.
	 * @return The amount of life points the beast has.
	 */
	public int getLifePoints() {
		return lifePoints;
	}

	/**
	 * Gets the beast's combat level.
	 * @return The beast's combat level.
	 */
	public int getCombatLevel() {
		return combatLevel;
	}

	/**
	 * Gets the beast's Defence level.
	 * @return The beast's Defence level.
	 */
	public int getDefenceLevel() {
		return defenceLevel;
	}

	/**
	 * Gets the beast's Attack level.
	 * @return The beast's Attack level.
	 */
	public int getAttackLevel() {
		return attackLevel;
	}

	/**
	 * Gets the beast's Magic level.
	 * @return The beast's Magic level.
	 */
	public int getMagicLevel() {
		return magicLevel;
	}

	/**
	 * Gets the beast's Ranged level.
	 * @return The beast's Ranged level.
	 */
	public int getRangedLevel() {
		return rangedLevel;
	}

	/**
	 * Gets the Slayer level required to attackLevel this beast.
	 * @return The Slayer level required to attackLevel this beast.
	 */
	public int getRequiredSlayerLevel() {
		return requiredSlayerLevel;
	}

	/**
	 * Gets the amount of tiles this beast occupies in the x and y dimension.
	 * @return The amount of tiles this beast occupies in the x and y dimension.
	 */
	public int getSize() {
		return size;
	}

	/**
	 * Checks if this beast is only accessible on members' worlds.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isMembersOnly() {
		return membersOnly;
	}

	/**
	 * Gets an {@link Optional} containing the Slayer category this beast belongs to.
	 * @return An {@link Optional} containing the Slayer category this beast belongs to, or {@link Optional#empty()} if it does not belong to a Slayer category.
	 */
	public Optional<String> getSlayerCategory() {
		return Optional.ofNullable(slayerCategory);
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
	 * Gets an {@link Optional} containing an animation's id by its name.
	 * @param name The name of the animation.
	 * @return An {@link OptionalInt} containing an animation's id, or {@link OptionalInt#empty()} if no animation of that name was found.
	 */
	public OptionalInt getAnimation(String name) {
		Integer id = animations.get(name);
		return id == null ? OptionalInt.empty() : OptionalInt.of(id);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Beast beast = (Beast) o;
		return id == beast.id
			&& attackable == beast.attackable
			&& aggressive == beast.aggressive
			&& poisonous == beast.poisonous
			&& lifePoints == beast.lifePoints
			&& combatLevel == beast.combatLevel
			&& defenceLevel == beast.defenceLevel
			&& attackLevel == beast.attackLevel
			&& magicLevel == beast.magicLevel
			&& rangedLevel == beast.rangedLevel
			&& requiredSlayerLevel == beast.requiredSlayerLevel
			&& size == beast.size
			&& membersOnly == beast.membersOnly
			&& Objects.equals(name, beast.name)
			&& Objects.equals(description, beast.description)
			&& Objects.equals(weakness, beast.weakness)
			&& Objects.equals(experience, beast.experience)
			&& Objects.equals(slayerCategory, beast.slayerCategory)
			&& Arrays.equals(areas, beast.areas)
			&& Objects.equals(animations, beast.animations);
	}

	@Override
	public int hashCode() {
		return Objects.hash(name, id, description, weakness, attackable, aggressive, poisonous, experience, lifePoints, combatLevel, defenceLevel, attackLevel, magicLevel, rangedLevel, requiredSlayerLevel, size, membersOnly, slayerCategory, areas, animations);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("name", name)
			.add("id", id)
			.add("description", description)
			.add("weakness", getWeakness().orElse("n/a"))
			.add("attackable", attackable)
			.add("aggressive", aggressive)
			.add("poisonous", poisonous)
			.add("experience", getExperience())
			.add("lifePoints", lifePoints)
			.add("combatLevel", combatLevel)
			.add("defenceLevel", defenceLevel)
			.add("attackLevel", attackLevel)
			.add("magicLevel", magicLevel)
			.add("rangedLevel", rangedLevel)
			.add("slayerLevelRequirement", requiredSlayerLevel)
			.add("size", size)
			.add("membersOnly", membersOnly)
			.add("slayerCategory", getSlayerCategory().orElse("n/a"))
			.add("areas", Arrays.toString(areas))
			.add("animations", animations)
			.toString();
	}
}
