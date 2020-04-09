package com.example.recipebook;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;


import java.util.ArrayList;
import java.util.List;

/*
* This class to handle database activities add,insert, delete
* */
public class DatabaseHandler extends SQLiteOpenHelper {

    private ContentResolver contentResolver;
    private static final int DATABASE_VERSION = Contract.DATABASE_VERSION;
    private static final String DATABASE_NAME = Contract.DATABASE_NAME;



    public DatabaseHandler(@Nullable Context context, @Nullable String name, @Nullable SQLiteDatabase.CursorFactory factory, int version)
    {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
        contentResolver = context.getContentResolver();
    }

    //Creates Table when app created
    @Override
    public void onCreate(SQLiteDatabase db)
    {
        String CREATE_RECIPES_TABLE = "CREATE TABLE " + Contract.RECIPES_TABLE + "(" + Contract.COLUMN_ID +
                " INTEGER PRIMARY KEY," + Contract.COLUMN_TITLE + " TEXT," + Contract.COLUMN_CONTENT +
                " TEXT, " + Contract.COLUMN_RATING +" FLOAT " + ")";

        db.execSQL(CREATE_RECIPES_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        db.execSQL("DROP TABLE IF EXISTS " + Contract.RECIPES_TABLE);
        onCreate(db);
    }

    //Add new recipe to db
    public void addNewRecipe(Recipe recipe){
        ContentValues contentValues = new ContentValues();

        contentValues.put(Contract.COLUMN_ID, recipe.getRecipeId());
        contentValues.put(Contract.COLUMN_TITLE, recipe.getRecipeTitle());
        contentValues.put(Contract.COLUMN_CONTENT, recipe.getRecipeContent());
        contentValues.put(Contract.COLUMN_RATING, recipe.getRecipeRating());

        contentResolver.insert(Contract.CONTENT_URI, contentValues);
    }
    //Search for recipe by title in db
    public List<Recipe> findRecipe(String recipeTitle) {
        // create empty list of recipes
        List<Recipe> recipeList = new ArrayList<Recipe>();
        // retrieve data from database to check
        Cursor cursor = contentResolver.query(Contract.CONTENT_URI,
                null, null, null, null);
        while (cursor.moveToNext()) { // loop through data to find matches
            // create Recipe instance from retrieved row
            Recipe recipe = new Recipe();
            recipe.setRecipeId(Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_ID))));
            recipe.setRecipeTitle(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_TITLE)));
            recipe.setRecipeContent(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_CONTENT)));
            recipe.setRecipeRating(Float.parseFloat(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_RATING))));

            // Adds to list any title contians keyword/letters
            if ((recipe.getRecipeTitle()).toLowerCase().contains(recipeTitle.toLowerCase())) {
                recipeList.add(recipe);
            }
        }
        return recipeList;
    }
    //delete recipe from db by ID
    public boolean deleteRecipe(String recipeID) {
        boolean result = false;
        String selection = Contract.COLUMN_ID + " = \"" + recipeID + "\"";
        int rowsDeleted = contentResolver.delete(Contract.CONTENT_URI,
                selection, null);
        if (rowsDeleted > 0) { result = true; }
        return result;
    }
}
