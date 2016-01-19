# RuneScape API

[![Build Status][build-status-img]][build-status]

## Introduction

`rs-api` is an open-source implementation of a web-service client, written in [Java][java], that allows interaction with the various [APIs][api] available for the popular [MMORPG][mmorpg]; [RuneScape][rs].

Javadoc is available [here][javadoc].

## Usage

The implementation offers functionality to interact with the following three public web-services:

* [Bestiary][bestiary]
* [Grand Exchange][ge]
* [Hiscores][hiscores]

[CSV][csv] and [JSON][json] results are parsed by the API and returned as interactable Java [`Object`][object]s.

To start using `rs-api`, simply instantiate a [HTTP][http] based [`RuneScapeAPI`][rsapi] class as follows:

```java
RuneScapeAPI api = RuneScapeAPI.createHttp();
```

**Note:** The API can run using a [`HttpClient`][httpclient] on the live RuneScape web-service, or a user supplied [`Client`][client] (e.g. a [`FakeClient`][fakeclient] for mocked unit testing).

At which point you may now access and query the [`Bestiary`][bestiary-api], [`GrandExchange`][ge-api], and [`Hiscores`][hiscores-api] API objects:

```java
	Bestiary bestiary = api.bestiary();
	GrandExchange grandExchange = api.grandExchange();
	Hiscores hiscores = api.hiscores();
```

## Examples

### Search in Bestiary

The [`Bestiary`][bestiary-api] API facilitates searching for a beast given a number of search filters (e.g. name, weakness, [Slayer][slayer] category).

Numerous filters can be applied to the [`Search`][bestiary-search] by chaining them together, and are [lazily evaluated][lazy-evaluation] when calling the terminal `results` method.

```java
Map<Integer, String> beasts = bestiary.search()
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

Calling the `graphingData` method with an [`Item`][item]'s ID as the parameter (e.g. 4151 for an [Abyssal whip][abyssal-whip]) in the [`GrandExchange`][ge-api] API will return the price information that may be used for graphical representation of an [`Item`][item]'s price history.

```java
Optional<GraphingData> optional = grandExchange.graphingData(4151);

optional.ifPresent(graphingData -> {
	LocalDate christmas = LocalDate.of(2014, Month.DECEMBER, 25);
	System.out.println("Daily price on Christmas 2014: " + graphingData.getDailyValue(christmas).get());
});
```

Outputs:

```
Daily price on Christmas 2014: 117549
```

### Player Hiscore Rank

Calling the `playerInformation` method with a [`Player`][player]'s name as a parameter (e.g. Drumgun) and a Hiscore [`Table`][table] as a parameter (e.g. `Table.DEFAULT`) in the [`Hiscores`][hiscores-api] API will return the hiscore information for the [`Player`][player], including [`Skill`][skill] levels and [`Activity`][activity] ranks.

```java
Optional<Player> optional = hiscores.playerInformation("Drumgun", Table.DEFAULT);

optional.ifPresent(player -> System.out.println("Overall rank: " + player.getSkills().get("Overall")));
```

Outputs:

```
Overall rank: Skill{rank=1, level=2595, experience=5200000000}
```

### Clan Information

Calling the `clanInformation` method with a clan's name (e.g. Sapphite Knights) as the parameter in the [`Hiscores`][hiscores-api] API will return an [`ImmutableList`][immutablelist] of [`ClanMate`][clanmate]s.

```java
List<ClanMate> clanMates = hiscores.clanInformation("Sapphite Knights");

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

[Gradle][gradle] is used as the build system. The [Gradle Wrapper][gradle-wrapper] is included in the distribution and as such, installation of Gradle by the user is not required.

Run `gradlew` in the project's root directory to build the application and run the unit tests.

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

[build-status-img]: https://travis-ci.org/MikeBull94/rs-api.svg?branch=master
[build-status]: https://travis-ci.org/MikeBull94/rs-api
[java]: https://www.java.com/
[mmorpg]: http://en.wikipedia.org/wiki/Massively_multiplayer_online_role-playing_game
[api]: http://en.wikipedia.org/wiki/Application_programming_interface
[rs]: http://runescape.com/
[javadoc]: http://mikebull94.github.io/rs-api/docs/javadoc/
[bestiary]: http://services.runescape.com/m=rswiki/en/Bestiary_APIs
[ge]: http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs
[hiscores]: http://services.runescape.com/m=rswiki/en/Hiscores_APIs
[csv]: http://en.wikipedia.org/wiki/Comma-separated_values
[json]: http://en.wikipedia.org/wiki/JSON
[object]: https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html
[http]: http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol
[rsapi]: /src/main/java/com/mikebull94/rsapi/RuneScapeAPI.java
[httpclient]: /src/main/java/com/mikebull94/rsapi/HttpClient.java
[fakeclient]: /src/test/java/com/mikebull94/rsapi/bestiary/BestiaryTest.java#L144
[client]: /src/main/java/com/mikebull94/rsapi/Client.java
[bestiary-api]: /src/main/java/com/mikebull94/rsapi/bestiary/Bestiary.java
[ge-api]: /src/main/java/com/mikebull94/rsapi/grandexchange/GrandExchange.java
[hiscores-api]: /src/main/java/com/mikebull94/rsapi/hiscores/Hiscores.java
[slayer]: http://runescape.wikia.com/wiki/Slayer
[bestiary-search]: /src/main/java/com/mikebull94/rsapi/bestiary/Search.java
[lazy-evaluation]: http://en.wikipedia.org/wiki/Lazy_evaluation
[abyssal-whip]: http://services.runescape.com/m=itemdb_rs/Abyssal_whip/viewitem.ws?obj=4151
[item]: /src/main/java/com/mikebull94/rsapi/grandexchange/Item.java
[player]: /src/main/java/com/mikebull94/rsapi/hiscores/Player.java
[immutablemap]: http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableMap.html
[table]: /src/main/java/com/mikebull94/rsapi/hiscores/Table.java
[skill]: /src/main/java/com/mikebull94/rsapi/hiscores/Skill.java
[activity]: /src/main/java/com/mikebull94/rsapi/hiscores/HiscoreActivity.java
[immutablelist]: http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableList.html
[clanmate]: /src/main/java/com/mikebull94/rsapi/hiscores/ClanMate.java
[gradle]: http://www.gradle.org/
[gradle-wrapper]: https://docs.gradle.org/current/userguide/gradle_wrapper.html
[commons]: https://commons.apache.org/
[httpcomponents]: https://hc.apache.org/
[gson]: https://code.google.com/p/google-gson/
[guava]: https://code.google.com/p/guava-libraries/
[junit]: http://junit.org/
[slf4j]: http://www.slf4j.org/
[isc]: https://www.isc.org/downloads/software-support-policy/isc-license/
