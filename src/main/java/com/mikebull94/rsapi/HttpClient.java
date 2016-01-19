package com.mikebull94.rsapi;

import com.google.common.base.MoreObjects;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableList;
import com.google.gson.Gson;
import com.google.gson.JsonIOException;
import com.google.gson.JsonSyntaxException;
import org.apache.commons.csv.CSVParser;
import org.apache.commons.csv.CSVRecord;
import org.apache.http.client.methods.CloseableHttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpUriRequest;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Objects;
import java.util.Optional;

/**
 * A {@link Client} that wraps a {@link org.apache.http.client.HttpClient} to interact with the RuneScape web-services API.
 */
public final class HttpClient implements Client {
	/**
	 * The URL to the RuneScape public web-services.
	 */
	public static final String WEB_SERVICES_URL = "http://services.runescape.com";

	/**
	 * The {@link Gson} instance.
	 */
	private final Gson gson = new Gson();

	/**
	 * Reads a {@link String} from a specified URL.
	 * @param url The URL to request from.
	 * @return The {@link String}.
	 * @throws IOException If an I/O error occurs.
	 */
	private static String stringFrom(String url) throws IOException {
		Preconditions.checkNotNull(url);
		HttpUriRequest request = new HttpGet(url);
		request.addHeader("accept", "application/json");
		request.addHeader("accept", "text/csv");

		try (CloseableHttpClient client = HttpClients.createDefault();
			 CloseableHttpResponse response = client.execute(request)) {
			return EntityUtils.toString(response.getEntity());
		}
	}

	/**
	 * Deserializes a JSON file from a specified URL into an object of the specified type.
	 * @param url The URL to deserialize from.
	 * @param typeOfT The specific genericized type of src.
	 * @param <T> The type of the desired object
	 * @return An {@link Optional} containing object of type T from the json, or {@link Optional#empty()} if the URL could not be deserialized.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public <T> Optional<T> fromJson(String url, Type typeOfT) throws IOException {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(typeOfT);

		try {
			return Optional.ofNullable(gson.fromJson(stringFrom(url), typeOfT));
		} catch (JsonSyntaxException | JsonIOException ignored) {
			return Optional.empty();
		}
	}

	/**
	 * Deserializes a JSON file from a specified URL into an object of the specified type.
	 * @param url The URL to deserialize from.
	 * @param classOfT The class of T.
	 * @param <T> The type of the desired object
	 * @return An optional of object of type T from the json, or {@link Optional#empty()} if the URL could not be deserialized.
	 * @throws IOException If an I/O error occurs.
	 */
	@Override
	public <T> Optional<T> fromJson(String url, Class<T> classOfT) throws IOException {
		Preconditions.checkNotNull(url);
		Preconditions.checkNotNull(classOfT);

		try {
			return Optional.ofNullable(gson.fromJson(stringFrom(url), classOfT));
		} catch (JsonSyntaxException | JsonIOException ignored) {
			return Optional.empty();
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

		try (CSVParser parser = CSVParser.parse(stringFrom(url), CSV_FORMAT)) {
			return ImmutableList.copyOf(parser.getRecords());
		}
	}

	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}
		HttpClient that = (HttpClient) o;
		return Objects.equals(gson, that.gson);
	}

	@Override
	public int hashCode() {
		return Objects.hash(gson);
	}

	@Override
	public String toString() {
		return MoreObjects.toStringHelper(this)
			.toString();
	}
}
