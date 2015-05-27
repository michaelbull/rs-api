package com.runescape.api.ge.model;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.runescape.api.ge.GrandExchange;

import java.util.Optional;

/**
 * Represents an item found on the RuneScape {@link GrandExchange}.
 */
public final class Item {
	/**
	 * The URL to the item's inventory sprite.
	 */
	private final String icon;

	/**
	 * The URL to the item's large sprite.
	 */
	private final String icon_large;

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
	 * The current {@link Price} of the item.
	 */
	private final Price current;

	/**
	 * The {@link Price} of the item today.
	 */
	private final Price today;

	/**
	 * A flag indicating whether this item is only accessible to members.
	 */
	private final boolean members;

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
	 * @param icon_large The url to the item's large sprite.
	 * @param id The id of the item.
	 * @param type The type of item.
	 * @param typeIcon The url to the type of item.
	 * @param name The name of the item.
	 * @param description The description of the item.
	 * @param current The current {@link Price} of the item.
	 * @param today The {@link Price} of the item today.
	 * @param members The members only flag.
	 * @param day30 The {@link PriceChange}s over the last 30 days.
	 * @param day90 The {@link PriceChange}s over the last 90 days.
	 * @param day180 The {@link PriceChange}s over the last 180 days
	 */
	public Item(String icon, String icon_large, int id, String type, String typeIcon, String name, String description, Price current, Price today, boolean members, PriceChange day30, PriceChange day90, PriceChange day180) {
		this.icon = Preconditions.checkNotNull(icon);
		this.icon_large = Preconditions.checkNotNull(icon_large);
		this.id = Preconditions.checkNotNull(id);
		this.type = Preconditions.checkNotNull(type);
		this.typeIcon = Preconditions.checkNotNull(typeIcon);
		this.name = Preconditions.checkNotNull(name);
		this.description = Preconditions.checkNotNull(description);
		this.current = Preconditions.checkNotNull(current);
		this.today = Preconditions.checkNotNull(today);
		this.members = Preconditions.checkNotNull(members);
		this.day30 = Preconditions.checkNotNull(day30);
		this.day90 = Preconditions.checkNotNull(day90);
		this.day180 = Preconditions.checkNotNull(day180);
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
		return icon_large;
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
	 * Gets the current {@link Price} of the item.
	 * @return The current {@link Price} of the item.
	 */
	public Price getCurrentPrice() {
		return current;
	}

	/**
	 * Gets the {@link Price} of the item today.
	 * @return The {@link Price} of the item today.
	 */
	public Price getTodaysPrice() {
		return today;
	}

	/**
	 * Checks whether this item is only accessible to members.
	 * @return {@code true} if so, {@code false} otherwise.
	 */
	public boolean isMembersOnly() {
		return members;
	}

	/**
	 * Gets an {@link Optional} of the item's {@link PriceChange} over the last 30 days.
	 * @return An {@link Optional} of the item's {@link PriceChange} over the last 30 days, or {@code Optional.empty()} if there is no {@link PriceChange} for the last 30 days.
	 */
	public Optional<PriceChange> getDay30() {
		return Optional.ofNullable(day30);
	}

	/**
	 * Gets an {@link Optional} of the item's {@link PriceChange} over the last 90 days.
	 * @return An {@link Optional} of the item's {@link PriceChange} over the last 90 days, or {@code Optional.empty()} if there is no {@link PriceChange} for the last 90 days.
	 */
	public Optional<PriceChange> getDay90() {
		return Optional.ofNullable(day90);
	}


	/**
	 * Gets an {@link Optional} of the item's {@link PriceChange} over the last 180 days.
	 * @return An {@link Optional} of the item's {@link PriceChange} over the last 180 days, or {@code Optional.empty()} if there is no {@link PriceChange} for the last 180 days.
	 */
	public Optional<PriceChange> getDay180() {
		return Optional.ofNullable(day180);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.add("icon", icon)
			.add("iconLarge", icon_large)
			.add("id", id)
			.add("type", type)
			.add("typeIcon", typeIcon)
			.add("name", name)
			.add("description", description)
			.add("currentPrice", current)
			.add("todaysPrice", today)
			.add("membersOnly", members)
			.add("day30", getDay30().toString())
			.add("day90", getDay90())
			.add("day180", getDay180())
			.toString();
	}
}
