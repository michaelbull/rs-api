package com.mikebull94.rsapi.grandexchange;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.gson.annotations.SerializedName;

import java.util.Objects;
import java.util.Optional;

/**
 * Represents an item found on the RuneScape {@link GrandExchange}.
 */
public final class Item {
	public static final class Builder {
		private boolean built = false;

		private final int id;

		private String icon = "";
		private String iconLarge = "";
		private String type = "";
		private String typeIcon = "";
		private String name = "";
		private String description = "";
		private PriceTrend current;
		private PriceTrend today;
		private boolean membersOnly = false;
		private PriceChange day30;
		private PriceChange day90;
		private PriceChange day180;

		private Builder(int id) {
			this.id = id;
		}

		public Builder icon(String icon) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.icon.isEmpty(), "Icon already set.");
			Preconditions.checkArgument(!icon.isEmpty(), "Icon must not be empty.");
			this.icon = icon;
			return this;
		}

		public Builder largeIcon(String iconLarge) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.iconLarge.isEmpty(), "Large icon already set.");
			Preconditions.checkArgument(!iconLarge.isEmpty(), "Large i must not be empty.");
			this.iconLarge = iconLarge;
			return this;
		}

		public Builder type(String type) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.type.isEmpty(), "Type already set.");
			Preconditions.checkArgument(!type.isEmpty(), "Type must not be empty.");
			this.type = type;
			return this;
		}

		public Builder typeIcon(String typeIcon) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.typeIcon.isEmpty(), "Type icon already set.");
			Preconditions.checkArgument(!typeIcon.isEmpty(), "Type icon must not be empty.");
			this.typeIcon = typeIcon;
			return this;
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

		public Builder currentPrice(PriceTrend current) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.current == null, "Current price trend already set.");
			Preconditions.checkNotNull(current);
			this.current = current;
			return this;
		}

		public Builder todaysPrice(PriceTrend today) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.today == null, "Todays price trend already set.");
			Preconditions.checkNotNull(today);
			this.today = today;
			return this;
		}

		public Builder membersOnly() {
			Preconditions.checkState(!built);
			Preconditions.checkState(!membersOnly, "Members only already set to true.");
			this.membersOnly = true;
			return this;
		}

		public Builder priceChange30Days(PriceChange day30) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.day30 == null, "Price change for last 30 days already set.");
			Preconditions.checkNotNull(day30);
			this.day30 = day30;
			return this;
		}

		public Builder priceChange90Days(PriceChange day90) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.day90 == null, "Price change for last 90 days already set.");
			Preconditions.checkNotNull(day90);
			this.day90 = day90;
			return this;
		}

		public Builder priceChange180Days(PriceChange day180) {
			Preconditions.checkState(!built);
			Preconditions.checkState(this.day180 == null, "Price change for last 180 days already set.");
			Preconditions.checkNotNull(day180);
			this.day180 = day180;
			return this;
		}

		public Item build() {
			Preconditions.checkState(!built);
			built = true;

			return new Item(
				icon,
				iconLarge,
				id,
				type,
				typeIcon,
				name,
				description,
				current,
				today,
				membersOnly,
				day30,
				day90,
				day180
			);
		}
	}

	public static Builder builder(int id) {
		return new Builder(id);
	}

	/**
	 * The URL to the item's inventory sprite.
	 */
	private final String icon;

	/**
	 * The URL to the item's large sprite.
	 */
	@SerializedName("icon_Large")
	private final String iconLarge;

	/**
	 * The id of the item.
	 */
	private final int id;

	/**
	 * The type of item.
	 */
	private final String type;

	/**
	 * The URL to the type of item.
	 */
	private final String typeIcon;

	/**
	 * The name of the item.
	 */
	private final String name;

	/**
	 * The description of the item.
	 */
	private final String description;

	/**
	 * The current {@link PriceTrend} of the item.
	 */
	private final PriceTrend current;

	/**
	 * The {@link PriceTrend} of the item today.
	 */
	private final PriceTrend today;

	/**
	 * A flag indicating whether this item is only accessible to members.
	 */
	@SerializedName("members")
	private final boolean membersOnly;

	/**
	 * The {@link PriceChange}s over the last 30 days.
	 */
	private final PriceChange day30;

	/**
	 * The {@link PriceChange}s over the last 90 days.
	 */
	private final PriceChange day90;

	/**
	 * The {@link PriceChange}s over the last 180 days.
	 */
	private final PriceChange day180;

	/**
	 * Creates a new {@link Item}.
	 * @param icon The url to the item's inventory sprite.
	 * @param iconLarge The url to the item's large sprite.
	 * @param id The id of the item.
	 * @param type The type of item.
	 * @param typeIcon The url to the type of item.
	 * @param name The name of the item.
	 * @param description The description of the item.
	 * @param current The current {@link PriceTrend} of the item.
	 * @param today The {@link PriceTrend} of the item today.
	 * @param membersOnly The members only flag.
	 * @param day30 The {@link PriceChange}s over the last 30 days.
	 * @param day90 The {@link PriceChange}s over the last 90 days.
	 * @param day180 The {@link PriceChange}s over the last 180 days
	 */
	private Item(String icon, String iconLarge, int id, String type, String typeIcon, String name, String description, PriceTrend current, PriceTrend today, boolean membersOnly, PriceChange day30, PriceChange day90, PriceChange day180) {
		this.icon = Preconditions.checkNotNull(icon);
		this.iconLarge = Preconditions.checkNotNull(iconLarge);
		Preconditions.checkArgument(id >= 0, "Id must be non-negative.");
		this.id = id;
		this.type = Preconditions.checkNotNull(type);
		this.typeIcon = Preconditions.checkNotNull(typeIcon);
		this.name = Preconditions.checkNotNull(name);
		this.description = Preconditions.checkNotNull(description);
		this.current = Preconditions.checkNotNull(current);
		this.today = Preconditions.checkNotNull(today);
		this.membersOnly = membersOnly;
		this.day30 = day30;
		this.day90 = day90;
		this.day180 = day180;
	}

	/**
	 * Gets the URL to the item's inventory sprite.
	 * @return The URL to the item's inventory sprite.
	 */
	public String getIcon() {
		return icon;
	}

	/**
	 * Gets the URL to the item's large sprite.
	 * @return The URL to the item's large sprite.
	 */
	public String getIconlarge() {
		return iconLarge;
	}

	/**
	 * Gets the id of the item.
	 * @return The id of the item.
	 */
	public int getId() {
		return id;
	}

	/**
	 * Gets the type of item.
	 * @return The type of item.
	 */
	public String getType() {
		return type;
	}

	/**
	 * Gets the URL to the type of item.
	 * @return The URL to the type of item.
	 */
	public String getTypeIcon() {
		return typeIcon;
	}

	/**
	 * Gets the name of the item.
	 * @return The name of the item.
	 */
	public String getName() {
		return name;
	}

	/**
	 * Gets the description of the item.
	 * @return The description of the item.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Gets the current {@link PriceTrend} of the item.
	 * @return The current {@link PriceTrend} of the item.
	 */
	public PriceTrend getCurrentPrice() {
		return current;
	}

	/**
	 * Gets the {@link PriceTrend} of the item today.
	 * @return The {@link PriceTrend} of the item today.
	 */
	public PriceTrend getTodaysPrice() {
		return today;
	}

	/**
	 * Checks whether this item is only accessible to members.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isMembersOnly() {
		return membersOnly;
	}

	/**
	 * Gets an {@link Optional} containing the item's {@link PriceChange} over the last 30 days.
	 * @return An {@link Optional} containing the item's {@link PriceChange} over the last 30 days, or {@link Optional#empty()} if there is no {@link PriceChange} for the last 30 days.
	 */
	public Optional<PriceChange> getDay30() {
		return Optional.ofNullable(day30);
	}

	/**
	 * Gets an {@link Optional} containing the item's {@link PriceChange} over the last 90 days.
	 * @return An {@link Optional} containing the item's {@link PriceChange} over the last 90 days, or {@link Optional#empty()} if there is no {@link PriceChange} for the last 90 days.
	 */
	public Optional<PriceChange> getDay90() {
		return Optional.ofNullable(day90);
	}

	/**
	 * Gets an {@link Optional} containing the item's {@link PriceChange} over the last 180 days.
	 * @return An {@link Optional} containing the item's {@link PriceChange} over the last 180 days, or {@link Optional#empty()} if there is no {@link PriceChange} for the last 180 days.
	 */
	public Optional<PriceChange> getDay180() {
		return Optional.ofNullable(day180);
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		Item item = (Item) o;
		return id == item.id
			&& membersOnly == item.membersOnly
			&& Objects.equals(icon, item.icon)
			&& Objects.equals(iconLarge, item.iconLarge)
			&& Objects.equals(type, item.type)
			&& Objects.equals(typeIcon, item.typeIcon)
			&& Objects.equals(name, item.name)
			&& Objects.equals(description, item.description)
			&& Objects.equals(current, item.current)
			&& Objects.equals(today, item.today)
			&& Objects.equals(day30, item.day30)
			&& Objects.equals(day90, item.day90)
			&& Objects.equals(day180, item.day180);
	}

	@Override
	public int hashCode() {
		return Objects.hash(icon, iconLarge, id, type, typeIcon, name, description, current, today, membersOnly, day30, day90, day180);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("icon", icon)
			.add("iconLarge", iconLarge)
			.add("id", id)
			.add("type", type)
			.add("typeIcon", typeIcon)
			.add("name", name)
			.add("description", description)
			.add("currentPrice", current)
			.add("todaysPrice", today)
			.add("membersOnly", membersOnly)
			.add("day30", getDay30().toString())
			.add("day90", getDay90())
			.add("day180", getDay180())
			.toString();
	}
}
