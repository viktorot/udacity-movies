package org.viktorot.udacity_movies.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;

import org.viktorot.udacity_movies.models.Movie;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Completable;
import io.reactivex.Scheduler;
import io.reactivex.Single;
import io.reactivex.schedulers.Schedulers;

public class MovieDb {

    private MovieDb() { }

    public static Single<List<Movie>> getFavouries(ContentResolver resolver) {
        return Single.fromCallable(() -> {
            String where = MoviesContract.FavouriteEntry.COLUMN_FAVOURITE + " = ?";

            Cursor cursor = resolver.query(MoviesContract.FavouriteEntry.CONTENT_URI,
                    null, where, new String[] { "1" }, null);

            List<Movie> movies = new ArrayList<>();

            if (cursor == null) {
                return movies;
            }

            if (!cursor.moveToFirst()) {
                return movies;
            }

            do {
                Movie movie = new Movie();
                movie.id = (int) cursor.getLong(cursor.getColumnIndex(MoviesContract.FavouriteEntry._ID));
                movie.title = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_TITLE));
                movie.originalTitle = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE));
                movie.overview = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_DESCRIPTION));
                movie.posterUrl = cursor.getString(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_IMAGE_URL));
                movie.score = cursor.getLong(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_SCORE));

                movies.add(movie);
            } while (cursor.moveToNext());

            cursor.close();

            return movies;
        }).subscribeOn(Schedulers.io());
    }

    public static Completable setFavourite(ContentResolver resolver, Movie movie, boolean fav) {
        return Completable.fromCallable(() -> {
            ContentValues values = new ContentValues();
            values.put(MoviesContract.FavouriteEntry._ID, movie.id);
            values.put(MoviesContract.FavouriteEntry.COLUMN_TITLE, movie.title);
            values.put(MoviesContract.FavouriteEntry.COLUMN_ORIGINAL_TITLE, movie.originalTitle);
            values.put(MoviesContract.FavouriteEntry.COLUMN_SCORE, movie.score);
            values.put(MoviesContract.FavouriteEntry.COLUMN_FAVOURITE, fav ? 1 : 0);
            values.put(MoviesContract.FavouriteEntry.COLUMN_DESCRIPTION, movie.overview);
            values.put(MoviesContract.FavouriteEntry.COLUMN_IMAGE_URL, movie.posterUrl);
            values.put(MoviesContract.FavouriteEntry.COLUMN_TIMESTAMP, 100);

            return resolver.insert(MoviesContract.FavouriteEntry.CONTENT_URI, values);
        }).subscribeOn(Schedulers.io());
    }

    public static Single<Boolean> isFavourite(ContentResolver resolver, long id) {
        return Single.fromCallable(() -> {
            String where = MoviesContract.FavouriteEntry._ID + " = ?";

            Cursor cursor = resolver.query(MoviesContract.FavouriteEntry.CONTENT_URI,
                    null, where, new String[]{String.valueOf(id)}, null);

            if (cursor == null) {
                return false;
            }

            if (!cursor.moveToFirst()) {
                return false;
            }

            long fav = cursor.getLong(cursor.getColumnIndex(MoviesContract.FavouriteEntry.COLUMN_FAVOURITE));
            cursor.close();

            return fav == 1;
        }).subscribeOn(Schedulers.io());
    }
}
