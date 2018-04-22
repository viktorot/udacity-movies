package org.viktorot.udacity_movies.repo;

import android.content.ContentResolver;

import org.viktorot.udacity_movies.BuildConfig;
import org.viktorot.udacity_movies.db.MovieDb;
import org.viktorot.udacity_movies.models.Movie;
import org.viktorot.udacity_movies.models.Review;
import org.viktorot.udacity_movies.models.Sort;
import org.viktorot.udacity_movies.models.Trailer;
import org.viktorot.udacity_movies.service.MovieService;

import java.util.List;

import io.reactivex.Completable;
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

    public Single<List<Trailer>> getTrailers(int id) {
        return MovieService.getInstance().getTrailers(id);
    }

    public Single<List<Review>> getReviews(int id) {
        return MovieService.getInstance().getReviews(id);
    }

    public Completable setFavourite(ContentResolver resolver, Movie movie, boolean fav) {
        return MovieDb.setFavourite(resolver, movie, fav);
    }

    public Single<Boolean> isFavourite(ContentResolver resolver, long id) {
        return MovieDb.isFavourite(resolver, id);
    }
}
