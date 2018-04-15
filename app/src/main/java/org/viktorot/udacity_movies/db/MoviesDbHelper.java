package org.viktorot.udacity_movies.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class MoviesDbHelper extends SQLiteOpenHelper {

    private static final String DB_NAME = "movies.db";
    private static final int VERSION = 1;

    public MoviesDbHelper(Context context) {
        super(context, DB_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        final String SQL_CREATE_FAV = "CREATE TABLE " +
                MoviesContract.FavouriteEntry.TABLE_NAME + "( " +
                MoviesContract.FavouriteEntry._ID + " TEXT PRIMARY KEY NOT NULL, " +
                MoviesContract.FavouriteEntry.COLUMN_NAME + " TEXT NOT NULL, " +
                MoviesContract.FavouriteEntry.COLUMN_DESCRIPTION + " TEXT NOT NULL, " +
                MoviesContract.FavouriteEntry.COLUMN_IMAGE_URL + " TEXT NOT NULL " +
                ");";

        sqLiteDatabase.execSQL(SQL_CREATE_FAV);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}
