package org.viktorot.udacity_movies.db;

import android.content.ContentResolver;
import android.content.ContentUris;
import android.net.Uri;
import android.provider.BaseColumns;

public class MoviesContract {
    public static final String CONTENT_AUTHORITY = "org.viktorot.udacity_movies";

    public static final Uri BASE_CONTENT_URI = Uri.parse("content://" + CONTENT_AUTHORITY);

    public static final class FavouriteEntry implements BaseColumns {
        // table name
        public static final String TABLE_NAME = "favourites";
        // columns
        public static final String _ID = "_id";
        public static final String COLUMN_TITLE = "title";
        public static final String COLUMN_ORIGINAL_TITLE = "original_title";
        public static final String COLUMN_DESCRIPTION = "description";
        public static final String COLUMN_SCORE = "score";
        public static final String COLUMN_FAVOURITE = "favourite";
        public static final String COLUMN_IMAGE_URL = "image_url";
        public static final String COLUMN_TIMESTAMP = "timestamp";

        // create content uri
        public static final Uri CONTENT_URI = BASE_CONTENT_URI.buildUpon()
                .appendPath(TABLE_NAME).build();
        // create cursor of base type directory for multiple entries
        public static final String CONTENT_DIR_TYPE =
                ContentResolver.CURSOR_DIR_BASE_TYPE + "/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;
        // create cursor of base type item for single entry
        public static final String CONTENT_ITEM_TYPE =
                ContentResolver.CURSOR_ITEM_BASE_TYPE +"/" + CONTENT_AUTHORITY + "/" + TABLE_NAME;

        // for building URIs on insertion
        public static Uri buildFavouritesUri(long id){
            return ContentUris.withAppendedId(CONTENT_URI, id);
        }
    }
}
