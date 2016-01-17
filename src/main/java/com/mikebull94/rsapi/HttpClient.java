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
import org.apache.http.impl.client.HttpClientBuilder;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
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
	 * Creates an {@link InputStream} from a specified URL.
	 * @param url The URL to open.
	 * @return The {@link InputStream}.
	 * @throws IOException If an I/O error occurs.
	 */
	private static InputStream streamFrom(String url) throws IOException {
		Preconditions.checkNotNull(url);
		HttpUriRequest request = new HttpGet(url);
		request.addHeader("accept", "application/json");

		try (CloseableHttpClient client = HttpClientBuilder.create().build();
			 CloseableHttpResponse response = client.execute(request)) {
			return response.getEntity().getContent();
		}
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

		try (Reader reader = new InputStreamReader(streamFrom(url), StandardCharsets.UTF_8)) {
			return Optional.ofNullable(gson.fromJson(reader, typeOfT));
		} catch (JsonSyntaxException | JsonIOException ignored) {
			return Optional.empty();
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

		try (Reader reader = new InputStreamReader(streamFrom(url), StandardCharsets.UTF_8)) {
			return Optional.ofNullable(gson.fromJson(reader, classOfT));
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

		StringBuilder builder = new StringBuilder();

		try (BufferedReader reader = new BufferedReader(new InputStreamReader(streamFrom(url), StandardCharsets.ISO_8859_1))) {
			reader.lines().forEach(s -> builder.append(s).append("\n"));
		}

		try (CSVParser parser = CSVParser.parse(builder.toString(), CSV_FORMAT)) {
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
