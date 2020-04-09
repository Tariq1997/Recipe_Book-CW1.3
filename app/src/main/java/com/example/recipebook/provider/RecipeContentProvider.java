package com.example.recipebook.provider;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.text.TextUtils;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.recipebook.Contract;
import com.example.recipebook.DatabaseHandler;

/* THIS CLASS WAS DERIVED FROM LAB 5 (COMP3040) */

public class RecipeContentProvider extends ContentProvider {

    private static final String AUTHORITY = Contract.AUTHORITY;
    private static final String RECIPES_TABLE = Contract.RECIPES_TABLE;
    public static final int RECIPES = 1;
    public static final int RECIPES_ID = 2;
    private static DatabaseHandler databaseHandler;

    private static final UriMatcher sURIMatcher = new UriMatcher(UriMatcher.NO_MATCH);
    static {
        sURIMatcher.addURI(AUTHORITY, RECIPES_TABLE, RECIPES);
        sURIMatcher.addURI(AUTHORITY, RECIPES_TABLE + "/#", RECIPES_ID);
    }
    @Override
    public boolean onCreate() {

        databaseHandler = new DatabaseHandler(getContext(), null, null, 1);
        return false;
    }


    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(Contract.RECIPES_TABLE);
        int uriType = sURIMatcher.match(uri);
        switch (uriType) {
            case RECIPES_ID:

                queryBuilder.appendWhere(Contract.COLUMN_ID + "="
                        + uri.getLastPathSegment());
                break;
            case RECIPES:
                break;
            default:
                throw new IllegalArgumentException("Unknown URI");
        }
        Cursor cursor = queryBuilder.query(databaseHandler.getReadableDatabase(), projection,
                selection, selectionArgs, null, null, sortOrder);
        cursor.setNotificationUri(getContext().getContentResolver(), uri);
        return cursor;
    }

    @Override
    public String getType(@NonNull Uri uri) {
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHandler.getWritableDatabase();
        long id = 0;
        switch (uriType) {
            case RECIPES:
                id = sqlDB.insert(Contract.RECIPES_TABLE, null, values);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return Uri.parse(RECIPES_TABLE + "/" + id);
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqlDB = databaseHandler.getWritableDatabase();
        int rowsDeleted = 0;
        switch (uriType) {
            case RECIPES:
                rowsDeleted = sqlDB.delete(Contract.RECIPES_TABLE,
                        selection, selectionArgs);
                break;
            case RECIPES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsDeleted = sqlDB.delete(Contract.RECIPES_TABLE,
                            Contract.COLUMN_ID + "=" + id, null);
                } else {
                    rowsDeleted = sqlDB.delete(Contract.RECIPES_TABLE,
                            Contract.COLUMN_ID + "=" + id
                                    + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsDeleted;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        int uriType = sURIMatcher.match(uri);
        SQLiteDatabase sqLiteDatabase = databaseHandler.getWritableDatabase();
        int rowsUpdated = 0;
        switch (uriType) {
            case RECIPES:
                rowsUpdated = sqLiteDatabase.update(Contract.RECIPES_TABLE, values,
                        selection, selectionArgs);
                break;
            case RECIPES_ID:
                String id = uri.getLastPathSegment();
                if (TextUtils.isEmpty(selection)) {
                    rowsUpdated = sqLiteDatabase.update(Contract.RECIPES_TABLE, values,
                            Contract.COLUMN_ID + "=" + id, null);
                } else {
                    rowsUpdated = sqLiteDatabase.update(Contract.RECIPES_TABLE, values,
                            Contract.COLUMN_ID + "=" + id
                                    + " and " + selection, selectionArgs);
                }
                break;
            default:
                throw new IllegalArgumentException("Unknown URI: " + uri);
        }
        getContext().getContentResolver().notifyChange(uri, null);
        return rowsUpdated;
    }
}
