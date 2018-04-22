package org.viktorot.udacity_movies.repo;

import android.content.ContentResolver;

import org.viktorot.udacity_movies.BuildConfig;
import org.viktorot.udacity_movies.db.MovieDb;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.models.Sort;
import org.viktorot.udacity_movies.service.MovieService;

import java.util.List;

import io.reactivex.Single;

public class MoviesRepo {

    private static MoviesRepo INSTANCE = null;

    public static MoviesRepo getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new MoviesRepo();
        }
        return INSTANCE;
    }

    private MoviesRepo() {
        MovieService.create(BuildConfig.API_KEY);
    }

    public Single<List<Movie>> getMovies(ContentResolver resolver, Sort sort) {
        switch (sort) {
            case Popularity:
            case VoteCount:
                return MovieService.getInstance().getMovies(sort);
            case Favourites:
                return MovieDb.getFavouries(resolver);
            default:
                return null;
        }
    }
}
