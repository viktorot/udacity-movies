package org.viktorot.udacity_movies.parsers;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.models.Review;
import org.viktorot.udacity_movies.models.Trailer;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class MovieParser {

    private static String KEY_RESULTS = "results";
    private static String KEY_ID = "id";
    private static String KEY_TITLE = "title";
    private static String KEY_ORIGINAL_TITLE = "original_title";
    private static String KEY_OVERVIEW = "overview";
    private static String KEY_BACKDROP = "backdrop_path";
    private static String KEY_POSTER = "poster_path";
    private static String KEY_SCORE = "vote_average";
    private static String KEY_RELEASE_DATE = "release_date";

    private static String KEY_TRAILER_NAME = "name";
    private static String KEY_TRAILER_KEY = "key";
    private static String KEY_TRAILER_TYPE = "type";

    private static String KEY_REVIEW_AUTHOR = "author";
    private static String KEY_REVIEW_CONTENT = "content";


    public static List<Movie> parseMoviesList(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray results = jsonObject.optJSONArray(KEY_RESULTS);
            if (results == null) {
                return Collections.emptyList();
            } else {
                List<Movie> movies = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    movies.add(parseMovie(obj));
                }

                return movies;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Movie parseMovie(JSONObject jsonObject) {
        Movie movie = new Movie();

        movie.id = jsonObject.optInt(KEY_ID, -1);
        movie.title = jsonObject.optString(KEY_TITLE, "");
        movie.originalTitle = jsonObject.optString(KEY_ORIGINAL_TITLE, "");
        movie.overview = jsonObject.optString(KEY_OVERVIEW, "");
        movie.backdropUrl = jsonObject.optString(KEY_BACKDROP, null);
        movie.posterUrl = jsonObject.optString(KEY_POSTER, null);
        movie.score = jsonObject.optLong(KEY_SCORE, 0);
        movie.releaseDate = jsonObject.optString(KEY_RELEASE_DATE, "");

        return movie;
    }


    public static List<Trailer> parseTrailerList(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray results = jsonObject.optJSONArray(KEY_RESULTS);
            if (results == null) {
                return Collections.emptyList();
            } else {
                List<Trailer> trailers = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    trailers.add(parseTrailer(obj));
                }

                return trailers;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Trailer parseTrailer(JSONObject jsonObject) {
        Trailer trailer = new Trailer();
        trailer.name = jsonObject.optString(KEY_TRAILER_NAME, null);
        trailer.key = jsonObject.optString(KEY_TRAILER_KEY, null);
        trailer.type = jsonObject.optString(KEY_TRAILER_TYPE, null);

        return trailer;
    }

    public static List<Review> parseReviewList(String data) {
        try {
            JSONObject jsonObject = new JSONObject(data);
            JSONArray results = jsonObject.optJSONArray(KEY_RESULTS);
            if (results == null) {
                return Collections.emptyList();
            } else {
                List<Review> reviews = new ArrayList<>();

                for (int i = 0; i < results.length(); i++) {
                    JSONObject obj = results.getJSONObject(i);
                    reviews.add(parseReview(obj));
                }

                return reviews;
            }

        } catch (JSONException e) {
            e.printStackTrace();
            return null;
        }
    }

    private static Review parseReview(JSONObject jsonObject) {
        Review review = new Review();
        review.author = jsonObject.optString(KEY_REVIEW_AUTHOR, null);
        review.content = jsonObject.optString(KEY_REVIEW_CONTENT, null);

        return review;
    }

}
