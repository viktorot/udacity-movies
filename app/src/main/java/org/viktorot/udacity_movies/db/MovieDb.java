package org.viktorot.udacity_movies.db;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.database.Cursor;
import android.util.Log;

import org.viktorot.udacity_movies.models.Movie;

import io.reactivex.Completable;
import io.reactivex.Single;

public class MovieDb {

    public static Single<Boolean> getFavouries(ContentResolver resolver) {
        return Single.fromCallable(() -> {
            Cursor cursor = resolver.query(MoviesContract.FavouriteEntry.CONTENT_URI,
                    null, null, null, null);

            if (cursor == null) {
                return false;
            }

            if (!cursor.moveToFirst()) {
                return false;
            }

            do {
                long id = cursor.getLong(cursor.getColumnIndex(MoviesContract.FavouriteEntry._ID));


                Log.d("---", id + "");
            } while (cursor.moveToNext());

            cursor.close();

            return true;
        });
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
            values.put(MoviesContract.FavouriteEntry.COLUMN_IMAGE_URL, movie.backdropUrl);
            values.put(MoviesContract.FavouriteEntry.COLUMN_TIMESTAMP, 100);

            return resolver.insert(MoviesContract.FavouriteEntry.CONTENT_URI, values);
        });
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
        });
    }
}
