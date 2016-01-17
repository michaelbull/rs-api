package com.mikebull94.rsapi;

import com.mikebull94.rsapi.bestiary.Beast;
import com.mikebull94.rsapi.bestiary.Bestiary;
import com.mikebull94.rsapi.grandexchange.Category;
import com.mikebull94.rsapi.grandexchange.CategoryPrices;
import com.mikebull94.rsapi.grandexchange.GrandExchange;
import com.mikebull94.rsapi.grandexchange.GraphingData;
import com.mikebull94.rsapi.grandexchange.Item;
import com.mikebull94.rsapi.grandexchange.ItemPriceInformation;
import com.mikebull94.rsapi.hiscores.ClanMate;
import com.mikebull94.rsapi.hiscores.HiscoreActivity;
import com.mikebull94.rsapi.hiscores.HiscoreTable;
import com.mikebull94.rsapi.hiscores.Hiscores;
import com.mikebull94.rsapi.hiscores.Player;
import com.mikebull94.rsapi.hiscores.Skill;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Scanner;

/**
 * An example program that interacts with the various RuneScape {@link RuneScapeAPI}s.
 */
public final class Example {
	/**
	 * The logger for this class.
	 */
	private static final Logger logger = LoggerFactory.getLogger(Example.class);

	/**
	 * The program's entry point.
	 * @param args The program's arguments.
	 */
	public static void main(String... args) {
		try (Scanner in = new Scanner(System.in, "UTF-8")) {
			RuneScapeAPI api = RuneScapeAPI.createHttp();
			Bestiary bestiary = api.bestiary();
			GrandExchange ge = api.grandExchange();
			Hiscores hiscores = api.hiscores();

			while (in.hasNextLine()) {
				String command = in.nextLine();

				if (command.equalsIgnoreCase("q")) {
					break;
				}

				Map<Integer, String> beastSearchResults;
				Map<String, Integer> slayerCategories;
				String geCategory;
				int itemId;

				switch (command) {
					case "beast data":
						System.out.print("Enter beasts id: ");
						int beastId = in.nextInt();
						Optional<Beast> beastOptional = bestiary.beastData(beastId);

						if (!beastOptional.isPresent()) {
							System.out.println("No beast found.");
							break;
						}

						beastOptional.ifPresent(System.out::println);
						break;

					case "beast search terms":
						System.out.print("Enter search terms, separated by a space: ");

						String input = in.nextLine();
						String[] terms = input.split("\\s+");

						beastSearchResults = bestiary.searchByTerms(terms);

						System.out.print("Beasts for search terms");
						for (String term : terms) {
							System.out.print(" \"" + term + "\"");
						}
						System.out.println();
						for (Map.Entry<Integer, String> entry : beastSearchResults.entrySet()) {
							System.out.println("\t[" + String.format("%05d", entry.getKey()) + "] " + entry.getValue());
						}
						break;

					case "beast search letter":
						System.out.print("Enter letter to search by: ");
						char c = Character.toUpperCase(in.next().charAt(0));

						beastSearchResults = bestiary.searchByFirstLetter(c);

						System.out.println("Beasts with names starting with \'" + c + "\':");
						for (Map.Entry<Integer, String> entry : beastSearchResults.entrySet()) {
							System.out.println("\t[" + String.format("%05d", entry.getKey()) + "] " + entry.getValue());
						}
						break;

					case "area names":
						System.out.println("Area names:");
						for (String areaName : bestiary.areaNames()) {
							System.out.println("\t" + areaName);
						}
						break;

					case "beasts in area":
						System.out.print("Enter area name: ");
						String areaName = in.nextLine();
						beastSearchResults = bestiary.beastsInArea(areaName);

						System.out.println("Beasts in area \"" + areaName + "\":");
						for (Map.Entry<Integer, String> entry : beastSearchResults.entrySet()) {
							System.out.println("\t[" + String.format("%05d", entry.getKey()) + "] " + entry.getValue());
						}
						break;

					case "slayer categories":
						slayerCategories = bestiary.slayerCategories();

						System.out.println("Slayer categories:");
						for (Map.Entry<String, Integer> entry : slayerCategories.entrySet()) {
							System.out.println("\t[" + String.format("%03d", entry.getValue()) + "] " + entry.getKey());
						}
						break;

					case "beasts in category":
						System.out.print("Enter category name: ");
						String slayerCategory = in.nextLine();

						beastSearchResults = bestiary.beastsInSlayerCategory(slayerCategory);

						System.out.println("Beasts in Slayer category \"" + slayerCategory + "\":");
						for (Map.Entry<Integer, String> entry : beastSearchResults.entrySet()) {
							System.out.println("\t[" + String.format("%05d", entry.getKey()) + "] " + entry.getValue());
						}
						break;

					case "weaknesses":
						Map<String, Integer> weaknesses = bestiary.weaknesses();

						System.out.println("Weaknesses:");
						for (Map.Entry<String, Integer> entry : weaknesses.entrySet()) {
							System.out.println("\t[" + String.format("%02d", entry.getValue()) + "] " + entry.getKey());
						}
						break;

					case "beasts weak to":
						System.out.print("Enter weakness name: ");
						String weaknessName = in.nextLine();

						beastSearchResults = bestiary.beastsWeakTo(weaknessName);

						System.out.println("Beasts weak to \"" + weaknessName + "\":");
						for (Map.Entry<Integer, String> entry : beastSearchResults.entrySet()) {
							System.out.println("\t[" + String.format("%05d", entry.getKey()) + "] " + entry.getValue());
						}
						break;

					case "beasts level":
						System.out.print("Enter lowest combat level: ");
						int lowerBound = in.nextInt();

						System.out.print("Enter highest combat level: ");
						int upperBound = in.nextInt();

						beastSearchResults = bestiary.beastsInLevelGroup(lowerBound, upperBound);

						System.out.println("Beasts in level group [" + lowerBound + "-" + upperBound + "]:");
						for (Map.Entry<Integer, String> entry : beastSearchResults.entrySet()) {
							System.out.println("\t[" + String.format("%05d", entry.getKey()) + "] " + entry.getValue());
						}
						break;

					case "ge category":
						System.out.print("Enter category name: ");
						geCategory = in.nextLine();

						Optional<Category> categoryOptional = ge.category(geCategory);

						if (!categoryOptional.isPresent()) {
							System.out.println("No category found.");
							break;
						}

						categoryOptional.ifPresent(System.out::println);
						break;

					case "category prices":
						System.out.print("Enter category name: ");
						geCategory = in.nextLine();

						System.out.print("Enter search prefix: ");
						String searchPrefix = in.nextLine();

						System.out.print("Enter page number: ");
						int pageNumber = in.nextInt();

						Optional<CategoryPrices> categoryPricesOptional = ge.categoryPrices(geCategory, searchPrefix, pageNumber);

						if (!categoryPricesOptional.isPresent()) {
							System.out.println("No category prices found.");
							break;
						}

						categoryPricesOptional.ifPresent(categoryPrices -> {
							System.out.println("Total items: " + categoryPrices.getTotal());
							for (Item item : categoryPrices.getItems()) {
								System.out.println("\t" + item.getName());
							}
						});
						break;

					case "graph":
						System.out.print("Enter item id: ");
						itemId = in.nextInt();

						Optional<GraphingData> graphingDataOptional = ge.graphingData(itemId);

						if (!graphingDataOptional.isPresent()) {
							System.out.println("No item found.");
							break;
						}

						graphingDataOptional.ifPresent(graphingData -> {
							System.out.println("Daily:");
							for (Map.Entry<LocalDate, Integer> entry : graphingData.getDailyPrices().entrySet()) {
								System.out.println("\t[" + entry.getKey() + "] " + entry.getValue());
							}
							System.out.println();
							System.out.println("Average:");
							for (Map.Entry<LocalDate, Integer> entry : graphingData.getAveragePrices().entrySet()) {
								System.out.println("\t[" + entry.getKey() + "] " + entry.getValue());
							}
						});
						break;

					case "history":
						System.out.print("Enter item id: ");
						itemId = in.nextInt();

						Optional<ItemPriceInformation> itemPriceInformationOptional = ge.itemPriceInformation(itemId);

						if (!itemPriceInformationOptional.isPresent()) {
							System.out.println("No item found.");
							break;
						}

						itemPriceInformationOptional.ifPresent(itemPriceInformation -> {
							System.out.println("30 days:  " + itemPriceInformation.getItem().getDay30());
							System.out.println("90 days:  " + itemPriceInformation.getItem().getDay90());
							System.out.println("180 days: " + itemPriceInformation.getItem().getDay180());
						});
						break;

					case "player":
						System.out.print("Enter player's display name: ");
						String displayName = in.nextLine();

						System.out.print("Enter hiscore table to use: ");
						HiscoreTable table = HiscoreTable.from(in.nextLine());

						Optional<Player> playerOptional = hiscores.playerInformation(displayName, table);

						if (!playerOptional.isPresent()) {
							System.out.println("No player named \"" + displayName + "\" found on Table " + table.name() + ".");
							break;
						}

						playerOptional.ifPresent(player -> {
							System.out.println();
							System.out.println("Skills:");
							for (Map.Entry<String, Skill> entry : player.getSkills().entrySet()) {
								String skillName = entry.getKey();
								Skill skill = entry.getValue();
								System.out.println("\t[" + skillName + "] " + skill);
							}
							System.out.println();
							System.out.println("Activities:");
							for (Map.Entry<String, HiscoreActivity> entry : player.getActivities().entrySet()) {
								String activityName = entry.getKey();
								HiscoreActivity activity = entry.getValue();
								System.out.println("\t[" + activityName + "] " + activity);
							}
							System.out.println();
						});
						break;

					case "clan":
						System.out.print("Enter clan's display name: ");
						String clanName = in.nextLine();

						List<ClanMate> clanMates = hiscores.clanInformation(clanName);

						System.out.println("Clan Mates:");
						clanMates.forEach(System.out::println);
						break;
				}
			}
		} catch (IOException e) {
			logger.error("Uncaught exception during startup.", e);
		}
	}

	private Example() {
		/* empty */
	}
}
