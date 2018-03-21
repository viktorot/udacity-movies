package org.viktorot.udacity_movies.service;

import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.models.Sort;
import org.viktorot.udacity_movies.parsers.MovieParser;

import java.util.List;

import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class MovieService {

    private static final String URL = "https://api.themoviedb.org/3/discover/movie?";
    private static final String MOVIE_URL = "https://api.themoviedb.org/3/movie/";

    private static final String ARG_SORT = "sort_by";
    private static final String ARG_API_KEY = "api_key";

    private static final String SORT_POPULARITY = "popularity.desc";
    private static final String SORT_VOTE = "vote_count.desc";

    private static final String IMAGE_URL = "http://image.tmdb.org/t/p/w185/";

    private final String API_KEY;
    private final OkHttpClient client;
    private static MovieService INSTANCE = null;

    public static void create(String apiKey) {
        INSTANCE = new MovieService(apiKey);
    }

    public static MovieService getInstance() {
        if (INSTANCE == null) {
            throw new IllegalStateException("service not created. call MovieService.create(key) before using it.");
        }
        return INSTANCE;
    }

    private MovieService(String apiKey) {
        this.API_KEY = apiKey;
        this.client = new OkHttpClient();
    }

    public Single<List<Movie>> getMovies(Sort sort) {
        final String sortParam;
        if (sort == Sort.Popularity) {
            sortParam = SORT_POPULARITY;
        } else {
            sortParam = SORT_VOTE;
        }

        return Single.fromCallable(() -> {
            Request request = new Request.Builder()
                    .url(URL + ARG_SORT + "=" + sortParam + "&" + ARG_API_KEY + "=" + API_KEY)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        })
                .map(MovieParser::parseMoviesList)
                .subscribeOn(Schedulers.io());
    }

    public Single<Movie> getMovie(int id) {
        return Single.fromCallable(() -> {
            Request request = new Request.Builder()
                    .url(MOVIE_URL + id + "?" + ARG_API_KEY + "=" + API_KEY)
                    .build();

            Response response = client.newCall(request).execute();
            return response.body().string();
        })
                .map(MovieParser::parseMovie)
                .subscribeOn(Schedulers.io());
    }

    public String getImageUrl(String path) {
        return IMAGE_URL + path;
    }
}
