# RuneScape API

[![Release](https://jitpack.io/v/michaelbull/rs-api.svg)](https://jitpack.io/#michaelbull/rs-api) [![Build Status](https://travis-ci.org/michaelbull/rs-api.svg?branch=master)](https://travis-ci.org/michaelbull/rs-api) [![License](https://img.shields.io/github/license/michaelbull/rs-api.svg)](https://github.com/michaelbull/rs-api/blob/master/LICENSE)

`rs-api` is an open-source implementation of a web-service client, written in [Java][java], that allows interaction with the various [APIs][api] available for the popular [MMORPG][mmorpg]; [RuneScape][rs].

Javadoc is available [here][javadoc].

## Installation

```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    compile 'com.github.michaelbull:rs-api:1.0.1'
}
```

## Usage

The implementation offers functionality to interact with the following three public web-services:

* [Bestiary][bestiary]
* [Grand Exchange][ge]
* [Hiscores][hiscores]

[CSV][csv] and [JSON][json] results are parsed by the API and returned as Java [`Object`][object]s.

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

## Contributing

Bug reports and pull requests are welcome on [GitHub][github].

## License

This project is available under the terms of the ISC license. See the
[`LICENSE`](LICENSE) file for the copyright information and licensing terms.

[java]: https://www.java.com/
[mmorpg]: http://en.wikipedia.org/wiki/Massively_multiplayer_online_role-playing_game
[api]: http://en.wikipedia.org/wiki/Application_programming_interface
[rs]: http://runescape.com/
[javadoc]: http://michaelbull.github.io/rs-api/1.0.1/docs/index.html
[bestiary]: http://services.runescape.com/m=rswiki/en/Bestiary_APIs
[ge]: http://services.runescape.com/m=rswiki/en/Grand_Exchange_APIs
[hiscores]: http://services.runescape.com/m=rswiki/en/Hiscores_APIs
[csv]: http://en.wikipedia.org/wiki/Comma-separated_values
[json]: http://en.wikipedia.org/wiki/JSON
[object]: https://docs.oracle.com/javase/7/docs/api/java/lang/Object.html
[http]: http://en.wikipedia.org/wiki/Hypertext_Transfer_Protocol
[rsapi]: /src/main/java/com/github/michaelbull/rs/RuneScapeAPI.java
[httpclient]: /src/main/java/com/github/michaelbull/rs/HttpClient.java
[fakeclient]: /src/test/java/com/github/michaelbull/rs/bestiary/BestiaryTest.java#L144
[client]: /src/main/java/com/github/michaelbull/rs/Client.java
[bestiary-api]: /src/main/java/com/github/michaelbull/rs/bestiary/Bestiary.java
[ge-api]: /src/main/java/com/github/michaelbull/rs/grandexchange/GrandExchange.java
[hiscores-api]: /src/main/java/com/github/michaelbull/rs/hiscores/Hiscores.java
[slayer]: http://runescape.wikia.com/wiki/Slayer
[bestiary-search]: /src/main/java/com/github/michaelbull/rs/bestiary/Search.java
[lazy-evaluation]: http://en.wikipedia.org/wiki/Lazy_evaluation
[abyssal-whip]: http://services.runescape.com/m=itemdb_rs/Abyssal_whip/viewitem.ws?obj=4151
[item]: /src/main/java/com/github/michaelbull/rs/grandexchange/Item.java
[player]: /src/main/java/com/github/michaelbull/rs/hiscores/Player.java
[immutablemap]: http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableMap.html
[table]: /src/main/java/com/github/michaelbull/rs/hiscores/Table.java
[skill]: /src/main/java/com/github/michaelbull/rs/hiscores/Skill.java
[activity]: /src/main/java/com/github/michaelbull/rs/hiscores/HiscoreActivity.java
[immutablelist]: http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableList.html
[clanmate]: /src/main/java/com/github/michaelbull/rs/hiscores/ClanMate.java
[gradle]: http://www.gradle.org/
[gradle-wrapper]: https://docs.gradle.org/current/userguide/gradle_wrapper.html
[github]: https://github.com/michaelbull/rs-api
