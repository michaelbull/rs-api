# RuneScape API

## Introduction

`rs-api` is an open-source implementation of a web-service client that allows interaction with the various [RuneScape][rs] APIs, written in [Java][java].

## Usage

The implementation offers functionality to interact with the following three public web-services:

* [Bestiary][bestiary]
* [Grand Exchange][ge]
* [Hiscores][hiscores]

[CSV][csv] and [JSON][json] results are parsed by the API and returned as interactable [Java][java] objects.

To start using `rs-api`, simply instantiate the [RuneScapeAPI][rsapi] class as follows:

```java
RuneScapeAPI api = new RuneScapeAPI();
```

At which point you may now access and query the Bestiary, Grand Exchange, and Hiscores APIs:

```java
	Bestiary bestiary = api.getBestiary();
	GrandExchange grandExchange = api.getGrandExchange();
	Hiscores hiscores = api.getHiscores();
```

## Examples

### Search in Bestiary

The [Bestiary API][bestiary-api] facilitates searching for a beast given a number of search filters (e.g. name, weakness, Slayer category). Numerous filters can be applied to the search by chaining them together, however the retrieval of the filters' results is not executed until the list of filters is constructed and the search is executed - at which point all of the filters will be applied sequentially.

```java
ImmutableMap<Integer, String> beasts = bestiary.search()
	.filterByNameTerms("dragon")
	.filterByArea("Taverley Dungeon")
	.filterByLevel(100, 140)
	.results();

System.out.println("Results:");
for (Map.Entry<Integer, String> entry : beasts.entrySet()) {
	System.out.println("\t[" + String.format("%04d", entry.getKey()) + "] " + entry.getValue());
}
```

Outputs:

```
Results:
	[0054] Black dragon (100)
	[4673] Black dragon (100)
```

### Item Price on Day

Calling the `graphingData` method with an item's ID as the parameter (e.g. 4151 for an Abyssal whip) will return the price information that may be used for graphical representation of an item's price history.

```java
Optional<GraphingData> optional = grandExchange.graphingData(4151);

optional.ifPresent(graphingData -> {
	LocalDateTime christmas = LocalDateTime.of(2014, Month.DECEMBER, 25, 0, 0);
	System.out.println("Daily price on Christmas 2014: " + graphingData.getDailyValue(christmas).get());
});
```

Outputs:

```
Daily price on Christmas 2014: 117549
```

### Player Hiscore Rank

Calling the `playerInformation` method with a player's name as a parameter (e.g. Drumgun) and a Hiscore [Table][table] as a parameter (e.g. `Table.DEFAULT`) in the [Hiscores API][hiscores-api] will return the hiscore information for the [Player][player], including skill levels and activity ranks.

```java
Optional<Player> optional = hiscores.playerInformation("Drumgun", Table.DEFAULT);

optional.ifPresent(player -> System.out.println("Overall rank: " + player.getSkills().get("Overall")));
```

Outputs:

```
Overall rank: Skill{rank=1, level=2595, experience=5200000000}
```

### Clan Information

Calling the `clanInformation` method with a clan's name (e.g. Sapphite Knights) as the parameter in the [Hiscores API][hiscores-api] will return an [ImmutableList][immutablelist] of [ClanMate][clanmate]s.

```java
ImmutableList<ClanMate> clanMates = hiscores.clanInformation("Sapphite Knights");

System.out.println("Clan Mates:");
clanMates.forEach(System.out::println);
```

Outputs:

```
Clan Mates:
ClanMate{name=Rosaline, rank=Owner, experience=463143387, kills=0}
ClanMate{name=Corp Sloter, rank=Deputy Owner, experience=678318180, kills=54}
ClanMate{name=GorgeousBrat, rank=Deputy Owner, experience=166603367, kills=0}
ClanMate{name=Chris Return, rank=Overseer, experience=306089480, kills=0}
ClanMate{name=Sauf, rank=Overseer, experience=346299506, kills=3}
...
```

## Building

[Gradle][gradle] is used as the build system. Java 8 ([Oracle Java][oracle]) or [OpenJDK][openjdk] on Linux is required.

Run `gradle` to build the application.

## Dependencies

The following Java libraries are used by the API:

* [Apache Commons][commons]
* [Apache HttpComponents][httpcomponents]
* [Google Gson][gson]
* [Google Guava][guava]
* [JUnit][junit]
* [SLF4J][slf4j]

## License

`rs-api` is available under the terms of the [ISC license][isc], which is similar to the 2-clause BSD license. See the `LICENSE` file for the copyright information and licensing terms.

[java]: https://www.java.com/
[rs]: http://runescape.com/
[bestiary]: http://services.runescape.com/m=rswiki/en/Bestiary_APIs
[ge]: http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs
[hiscores]: http://services.runescape.com/m=rswiki/en/Hiscores_APIs
[csv]: http://en.wikipedia.org/wiki/Comma-separated_values
[json]: http://en.wikipedia.org/wiki/JSON
[rsapi]: /api/src/main/java/com/runescape/api/RuneScapeAPI.java
[bestiary-api]: /bestiary/src/main/java/com/runescape/api/bestiary/Bestiary.java
[immutablemap]: http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableMap.html
[hiscores-api]: /hiscores/src/main/java/com/runescape/api/hiscores/Hiscores.java
[table]: /hiscores/src/main/java/com/runescape/api/hiscores/model/Table.java
[player]: /hiscores/src/main/java/com/runescape/api/hiscores/model/Player.java
[immutablelist]: http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableList.html
[clanmate]: /hiscores/src/main/java/com/runescape/api/hiscores/model/ClanMate.java
[gradle]: http://www.gradle.org/
[oracle]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[openjdk]: http://openjdk.java.net/
[commons]: https://commons.apache.org/
[httpcomponents]: https://hc.apache.org/
[gson]: https://code.google.com/p/google-gson/
[guava]: https://code.google.com/p/guava-libraries/
[junit]: http://junit.org/
[slf4j]: http://www.slf4j.org/
[isc]: https://www.isc.org/downloads/software-support-policy/isc-license/