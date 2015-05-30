package com.runescape.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * A {@link Client} that facilitates interaction with the RuneScape web-services API over HTTP.
 */
public final class HttpClient implements Client {
	/**
	 * The URL to the RuneScape public web-services.
	 */
	public static final String WEB_SERVICES_URL = "http://services.runescape.com";

	/**
	 * The {@link Gson} instance.
	 */
	private static final Gson GSON = new Gson();

	/**
	 * Creates an {@link InputStreamReader} from a specified URL.
	 * @param url The URL to open.
	 * @param charset The {@link Charset} to use.
	 * @return The {@link InputStreamReader}.
	 * @throws IOException
	 */
	private InputStreamReader readerFrom(String url, Charset charset) throws IOException {
		Preconditions.checkNotNull(url);
		return new InputStreamReader(new URL(url).openStream(), charset);
	}

	/**
	 * Deserializes a JSON file from a specified URL into an object of the specified type.
	 * @param url The URL to deserialize from.
	 * @param typeOfT The specific genericized type of src.
	 * @param <T> The type of the desired object
	 * @return An {@link Optional} of object of type T from the json, or {@code Optional.empty()} if the URL could not be deserialized.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public <T> Optional<T> fromJson(String url, Type typeOfT) throws IOException {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(typeOfT);
		try (Reader reader = readerFrom(url, StandardCharsets.UTF_8)) {
			try {
				return Optional.ofNullable(GSON.fromJson(reader, typeOfT));
			} catch (JsonSyntaxException | JsonIOException exception) {
				return Optional.empty();
			}
		}
	}

	/**
	 * Deserializes a JSON file from a specified URL into an object of the specified type.
	 * @param url The URL to deserialize from.
	 * @param classOfT The class of T.
	 * @param <T> The type of the desired object
	 * @return An optional of object of type T from the json, or {@code Optional.empty()} if the URL could not be deserialized.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public <T> Optional<T> fromJson(String url, Class<T> classOfT) throws IOException {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(classOfT);
		try (Reader reader = readerFrom(url, StandardCharsets.UTF_8)) {
			try {
				return Optional.ofNullable(GSON.fromJson(reader, classOfT));
			} catch (JsonSyntaxException | JsonIOException exception) {
				return Optional.empty();
			}
		}
	}

	/**
	 * Deserializes a CSV file from a specified URL into an {@link ImmutableList} of {@link CSVRecord}s.
	 * @param url The URL to deserialize from.
	 * @return An {@link ImmutableList} of {@link CSVRecord}s.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public ImmutableList<CSVRecord> fromCSV(String url) throws IOException {
		Preconditions.checkNotNull(url);
		try (CSVParser parser = CSVParser.parse(new URL(url), StandardCharsets.ISO_8859_1, CSV_FORMAT)) {
			return ImmutableList.copyOf(parser.getRecords());
		}
	}
}
