package com.runescape.api;

import com.google.common.collect.ImmutableList;
import org.apache.commons.csv.CSVFormat;
import org.apache.commons.csv.CSVRecord;

import java.io.IOException;
import java.lang.reflect.Type;
import java.util.Optional;

/**
 * Represents a client that can deserialize JSON and CSV resources into Java objects.
 */
public interface Client {
	/**
	 * The comma-separated CSV format.
	 */
	CSVFormat CSV_FORMAT = CSVFormat.DEFAULT.withDelimiter(',');

	/**
	 * Deserializes a JSON file from a specified URL into an object of the specified type.
	 * @param url The URL to deserialize from.
	 * @param typeOfT The specific genericized type of src.
	 * @param <T> The type of the desired object
	 * @return An {@link Optional} of object of type T from the json, or {@code Optional.empty()} if the URL could not be deserialized.
	 */
	<T> Optional<T> fromJson(String url, Type typeOfT) throws IOException;

	/**
	 * Deserializes a JSON file from a specified URL into an object of the specified type.
	 * @param url The URL to deserialize from.
	 * @param classOfT The class of T.
	 * @param <T> The type of the desired object
	 * @return An optional of object of type T from the json, or {@code Optional.empty()} if the URL could not be deserialized.
	 */
	<T> Optional<T> fromJson(String url, Class<T> classOfT) throws IOException;

	/**
	 * Deserializes a CSV file from a specified URL into an {@link ImmutableList} of {@link CSVRecord}s.
	 * @param url The URL to deserialize from.
	 * @return An {@link ImmutableList} of {@link CSVRecord}s.
	 */
	ImmutableList<CSVRecord> fromCSV(String url) throws IOException;
}
