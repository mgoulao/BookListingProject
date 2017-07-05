package com.mgoulao.booklistingproject;

import android.util.Log;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;

/**
 * Created by msilv on 7/4/2017.
 */

public class QueryUtils {

    private static final String MG_TAG = "MG";
    private static final String ITEMS = "items";
    private static final String VOLUME_INFO = "volumeInfo";
    private static final String TITLE = "title";
    private static final String AUTHORS = "authors";
    private static final String RATING = "averageRating";
    private static final String IMAGE_LINKS = "imageLinks";
    private static final String SMALL_THUMBNAILS = "smallThumbnail";

    /**
     * Create a private constructor because no one should ever create a {@link QueryUtils} object.
     * This class is only meant to hold static variables and methods, which can be accessed
     * directly from the class name QueryUtils (and an object instance of QueryUtils is not needed).
     */
    private QueryUtils() {
    }

    public static ArrayList<Book> fetchBookData(String requestUrl) {
        // Create URL object
        URL url = createUrl(requestUrl);

        // Perform HTTP request to the URL and receive a JSON response back
        String jsonResponse = null;
        try {
            jsonResponse = makeHttpRequest(url);
        } catch (IOException e) {
            Log.e(MG_TAG, "Error closing input stream", e);
        }

        // Return the {@link Event}, value extracted from the JSON response
        return extractBooks(jsonResponse);
    }

    /**
     * Return a list of {@link Book} objects that has been built up from
     * parsing a JSON response.
     */
    public static ArrayList<Book> extractBooks(String jsonResponse) {

        // Create an empty ArrayList that we can start adding books to
        ArrayList<Book> booksList = new ArrayList<>();

        // Try to parse the jsonResponse. If there's a problem with the way the JSON
        // is formatted, a JSONException exception object will be thrown.
        // Catch the exception so the app doesn't crash, and print the error message to the logs.
        try {

            // Parse the response given by the jsonResponse and
            // build up a list of Book objects with the corresponding data.
            JSONObject jsonBook = new JSONObject(jsonResponse);

            // Getting JSON Array node
            JSONArray items = jsonBook.getJSONArray("items");

            // looping through all the books
            for (int z = 0; z < items.length(); z++) {
                Log.d(MG_TAG, "z: " + z);
                String title = "";
                String authors = "";
                String thumbnail = "";
                Double rating = 0.0;

                JSONObject book = items.getJSONObject(z);

                // get current book volume info
                JSONObject volumeInfo = book.getJSONObject(VOLUME_INFO);
                title = volumeInfo.getString(TITLE);

                // get all the authors from the book volume info
                if (volumeInfo.has(AUTHORS)) {
                    JSONArray authorsArray = volumeInfo.getJSONArray(AUTHORS);
                    // looping through all the authors
                    for (int i = 0; i < authorsArray.length(); i++) {
                        if (i == 0) {
                            authors = authorsArray.getString(i);
                        } else {
                            authors += ", " + authorsArray.getString(i);
                        }
                    }
                }

                if (volumeInfo.has(RATING)) {
                    rating = volumeInfo.getDouble(RATING);
                }

                if (volumeInfo.has(IMAGE_LINKS)) {
                    JSONObject imageLinks = volumeInfo.getJSONObject(IMAGE_LINKS);
                    if (imageLinks.has(SMALL_THUMBNAILS)) {
                        thumbnail = imageLinks.getString(SMALL_THUMBNAILS);
                    }
                }

                booksList.add(new Book(thumbnail, title, authors, rating));
            }

        } catch (JSONException e) {
            // If an error is thrown when executing any of the above statements in the "try" block,
            // catch the exception here, so the app doesn't crash. Print a log message
            // with the message from the exception.
            Log.e("QueryUtils", "Problem parsing the earthquake JSON results", e);
        }

        // Return the list of books
        return booksList;
    }

    /**
     * Returns new URL object from the given string URL.
     */
    private static URL createUrl(String stringUrl) {
        URL url = null;
        try {
            url = new URL(stringUrl);
        } catch (MalformedURLException e) {
            Log.e(MG_TAG, "Error with creating URL ", e);
        }
        return url;
    }

    /**
     * Make an HTTP request to the given URL and return a String as the response.
     */
    private static String makeHttpRequest(URL url) throws IOException {
        String jsonResponse = "";

        // If the URL is null, then return early.
        if (url == null) {
            return jsonResponse;
        }

        HttpURLConnection urlConnection = null;
        InputStream inputStream = null;
        try {
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setReadTimeout(10000 /* milliseconds */);
            urlConnection.setConnectTimeout(15000 /* milliseconds */);
            urlConnection.setRequestMethod("GET");
            urlConnection.connect();

            // If the request was successful (response code 200),
            // then read the input stream and parse the response.
            if (urlConnection.getResponseCode() == 200) {
                inputStream = urlConnection.getInputStream();
                jsonResponse = readFromStream(inputStream);
            } else {
                Log.e(MG_TAG, "Error response code: " + urlConnection.getResponseCode());
            }
        } catch (IOException e) {
            Log.e(MG_TAG, "Problem retrieving the earthquake JSON results.", e);
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
            if (inputStream != null) {
                inputStream.close();
            }
        }
        return jsonResponse;
    }

    /**
     * Convert the {@link InputStream} into a String which contains the
     * whole JSON response from the server.
     */
    private static String readFromStream(InputStream inputStream) throws IOException {
        StringBuilder output = new StringBuilder();
        if (inputStream != null) {
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
            BufferedReader reader = new BufferedReader(inputStreamReader);
            String line = reader.readLine();
            while (line != null) {
                output.append(line);
                line = reader.readLine();
            }
        }
        return output.toString();
    }
}
