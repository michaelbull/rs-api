package com.runescape.api;

import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

/**
 * A web-client that connects to the RuneScape web-services API.
 */
public final class Client {
	/**
	 * The URL to the RuneScape public web-services.
	 */
	public static final String WEB_SERVICES_URL = "http://services.runescape.com";

	/**
	 * The comma-separated CSV format.
	 */
	private static final CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withDelimiter(',');

	/**
	 * The {@link Gson} instance.
	 */
	private static final Gson GSON = new Gson();

	/**
	 * A client for HTTP request execution.
	 */
	private final HttpClient client = HttpClientBuilder.create().build();

	/**
	 * Opens an {@link InputStream} from a URL.
	 * @param url The URL to open.
	 * @return The {@link InputStream}.
	 * @throws IOException If an I/O error occurs.
	 */
	private InputStream streamFrom(String url) throws IOException {
		Preconditions.checkNotNull(url);
		HttpGet request = new HttpGet(url);
		request.addHeader("accept", "application/json");
		return client.execute(request).getEntity().getContent();
	}

	/**
	 * Deserializes a JSON file from a specified URL into an object of the specified type.
	 * @param url The URL to deserialize from.
	 * @param typeOfT The specific genericized type of src.
	 * @param <T> The type of the desired object
	 * @return An {@link Optional} of object of type T from the json, or {@code Optional.empty()} if the URL could not be deserialized.
	 * @throws IOException If an I/O error occurs.
	 */
	public <T> Optional<T> fromJson(String url, Type typeOfT) throws IOException {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(typeOfT);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(streamFrom(url), StandardCharsets.UTF_8))) {
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
	public <T> Optional<T> fromJson(String url, Class<T> classOfT) throws IOException {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(classOfT);
		try (BufferedReader reader = new BufferedReader(new InputStreamReader(streamFrom(url), StandardCharsets.UTF_8))) {
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
	public ImmutableList<CSVRecord> fromCSV(String url) throws IOException {
		Preconditions.checkNotNull(url);
		try (CSVParser parser = new CSVParser(new InputStreamReader(streamFrom(url), StandardCharsets.ISO_8859_1), CSV_FORMAT)) {
			return ImmutableList.copyOf(parser.getRecords());
		}
	}
}
