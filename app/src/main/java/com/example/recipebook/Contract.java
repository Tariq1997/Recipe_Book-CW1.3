package com.example.recipebook;

import android.net.Uri;

public class Contract
{
    // communication contract shared among classes

    public static final String AUTHORITY = "com.example.recipebook.provider.RecipeContentProvider";
    public static final String RECIPES_TABLE = "recipes";
    public static final Uri CONTENT_URI = Uri.parse("content://" + AUTHORITY + "/" + RECIPES_TABLE);
    public static final String COLUMN_ID = "id";
    public static final String COLUMN_TITLE = "title";
    public static final String COLUMN_CONTENT = "content";
    public static final int DATABASE_VERSION = 1;
    public static final String COLUMN_RATING = "rating" ;
    public static final String DATABASE_NAME = "recipeDB.db";
}
