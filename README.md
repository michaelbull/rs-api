# RuneScape API

## Introduction

`rs-api` is an open-source implementation of a web-service client that allows interaction with the various [RuneScape][rs] APIs, written in [Java][java].

## Usage

The implementation offers functionality to interact with the following three public web-services:

* [Bestiary][bestiary]
* [Grand Exchange][ge]
* [Hiscores][hiscores]

[Comma-separated values][csv] and [JSON][json] results are parsed by the API and returned as interactable [Java][java] objects.

For example, calling the `beastsInLevelGroup` method with 250 and 300 as parameters in the [Bestiary API][bestiary-api] will return an [ImmutableMap][immutablemap] of beast IDs to beast names, containing only beasts that have a combat level between 250 and 300 inclusive:

| Beast ID |        Beast Name       |
|:--------:|:-----------------------:|
| 50       | King Black Dragon (276) |
| 15206    | TokHaar-Yt-MejKot (300) |

## Building

[Gradle][gradle] is used as the build system. Java 8 ([Oracle Java][oracle]) or [OpenJDK][openjdk] on Linux is required.

Run `gradle` to build the application.

## Dependencies

The following Java libraries are used by the API:

* [Apache Commons][commons]
* [Apache HttpComponents][httpcomponents]
* [Google Gson][gson]
* [Google Guava][guava]
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
[bestiary-api]: /bestiary/src/main/java/com/runescape/api/bestiary/Bestiary.java
[immutablemap]: http://docs.guava-libraries.googlecode.com/git/javadoc/com/google/common/collect/ImmutableMap.html
[gradle]: http://www.gradle.org/
[oracle]: http://www.oracle.com/technetwork/java/javase/downloads/index.html
[openjdk]: http://openjdk.java.net/
[commons]: https://commons.apache.org/
[httpcomponents]: https://hc.apache.org/
[gson]: https://code.google.com/p/google-gson/
[guava]: https://code.google.com/p/guava-libraries/
[slf4j]: http://www.slf4j.org/
[isc]: https://www.isc.org/downloads/software-support-policy/isc-license/