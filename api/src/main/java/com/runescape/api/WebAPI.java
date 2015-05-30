package com.runescape.api;

import com.runescape.api.bestiary.Bestiary;
import com.runescape.api.ge.GrandExchange;
import com.runescape.api.hiscores.Hiscores;

/**
 * Represents an instance of the RuneScape web-services API.
 */
public final class WebAPI {
	/**
	 * The {@link Bestiary}.
	 */
	private final Bestiary bestiary;

	/**
	 * The {@link GrandExchange}.
	 */
	private final GrandExchange grandExchange;

	/**
	 * The {@link Hiscores}.
	 */
	private final Hiscores hiscores;

	/**
	 * Creates a new {@link WebAPI}.
	 */
	public WebAPI() {
		Client client = new HttpClient();
		this.bestiary = new Bestiary(client);
		this.grandExchange = new GrandExchange(client);
		this.hiscores = new Hiscores(client);
	}

	/**
	 * Gets the {@link Bestiary}.
	 * @return The {@link Bestiary}.
	 */
	public Bestiary getBestiary() {
		return bestiary;
	}

	/**
	 * Gets the {@link GrandExchange}.
	 * @return The {@link GrandExchange}.
	 */
	public GrandExchange getGrandExchange() {
		return grandExchange;
	}

	/**
	 * Gets the {@link Hiscores}.
	 * @return The {@link Hiscores}.
	 */
	public Hiscores getHiscores() {
		return hiscores;
	}
}
