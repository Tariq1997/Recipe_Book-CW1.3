package com.example.recipebook;

import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.text.InputType;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

public class MainActivity extends AppCompatActivity
{
    private static boolean isSearching =false;
    ListView recipeListViewMain;
    Button addBtn,findBtn,sortBtn;
    private static final int NEW_RECIPE_REQUEST = 0;
    private static final int VIEW_RECIPE_REQUEST = 1;
    private static final int SORT_RECIPE_RATING = 0;
    private static final int SORT_RECIPE_TITLE = 1;
    private static final int SORT_RECIPE_ID = 2;
    private static DatabaseHandler databaseHandler;
    private static List<Recipe> dataList;
    private static RecipeAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //Initializing Variables
        databaseHandler = new DatabaseHandler(this, null, null,
                Contract.DATABASE_VERSION);
        isSearching = false;
        addBtn = findViewById(R.id.addRecipeBtn);
        findBtn = findViewById(R.id.findBtnMain);
        sortBtn = findViewById(R.id.sortBtnMain);
        recipeListViewMain = findViewById(R.id.recipeListViewMain);


        recipeListViewMain.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Recipe recipe = dataList.get(position); // get item recipe

                // start UpdateRecipeActivity with current recipe's details
                Intent intent = new Intent(getApplicationContext(), UpdateRecipeActivity.class);
                intent.putExtra("id", Integer.toString(recipe.getRecipeId()));
                intent.putExtra("title", recipe.getRecipeTitle());
                intent.putExtra("content", recipe.getRecipeContent());
                intent.putExtra("rating", Float.toString(recipe.getRecipeRating()));
                startActivityForResult(intent, VIEW_RECIPE_REQUEST);
            }
        });



        /*
        *Add New Recipe Button
        * */
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(isSearching){
                    isSearching = false;
                    addBtn.setText("New Recipe");
                    displayDatabaseContent(SORT_RECIPE_ID);
                }else{
                    Intent intent = new Intent(MainActivity.this,AddNewRecipeActiviy.class);
                    startActivityForResult(intent,NEW_RECIPE_REQUEST);
                }
            }
        });

        /*
        * Popup Dialog for searching by title
        * */
        findBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // create search dialog box
                AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Search by title");
                final EditText input = new EditText(MainActivity.this);
                input.setInputType(InputType.TYPE_CLASS_TEXT);
                dialog.setView(input);

                dialog.setPositiveButton("Search", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        isSearching = true;
                        addBtn.setText("Cancel");
                        // reset list data by refilling the list adapter
                        List<Recipe> foundMatches = databaseHandler.findRecipe(input.getText().toString());
                        adapter.clear();
                        adapter.addAll(foundMatches);
                        adapter.notifyDataSetChanged();

                    }
                });
                dialog.show();
            }
        });

        /*
        * OnClick Sort popup dialog to specify sorting category
        * */
        sortBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final AlertDialog.Builder dialog = new AlertDialog.Builder(MainActivity.this);
                dialog.setTitle("Sort");
                CharSequence options[]  = new CharSequence[]
                        {
                                "Rating",
                                "Title",
                                "ID"
                        };

                dialog.setItems(options, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if(i==SORT_RECIPE_RATING)
                        {
                            Toast.makeText(MainActivity.this,"Sort by Rating",Toast.LENGTH_LONG).show();

                            displayDatabaseContent(i);
                        }else if(i==SORT_RECIPE_TITLE){

                            Toast.makeText(MainActivity.this,"Sort by Title",Toast.LENGTH_LONG).show();
                            displayDatabaseContent(i);

                        }else if(i==SORT_RECIPE_ID){

                            Toast.makeText(MainActivity.this,"Sort by ID",Toast.LENGTH_LONG).show();
                            displayDatabaseContent(i);

                        }
                    }
                });
                dialog.show();
            }
        });
        displayDatabaseContent(SORT_RECIPE_ID);



    }

    /*
    * Gets content from database and displays it on Screen According to Specified Order
    * */
    private void displayDatabaseContent(int sortRecipeBy)
    {
        //Specify columns for query
        String[] mProjection = new String[] {
                Contract.COLUMN_ID,
                Contract.COLUMN_TITLE,
                Contract.COLUMN_CONTENT,
                Contract.COLUMN_RATING
        };


        //provide access to recipe content provider
        Cursor cursor = getContentResolver().query(Contract.CONTENT_URI, mProjection,
                null, null, null);
        //Intializing Arraylist to store Recipe Details
        dataList = new ArrayList<Recipe>();
        // loop through Content Resolver
        while (cursor.moveToNext()) {
            // create  recipe object from recipe details
            Recipe recipe = new Recipe(
                    Integer.parseInt(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_ID))),
                    cursor.getString(cursor.getColumnIndex(Contract.COLUMN_TITLE)),
                    cursor.getString(cursor.getColumnIndex(Contract.COLUMN_CONTENT)),
                    Float.parseFloat(cursor.getString(cursor.getColumnIndex(Contract.COLUMN_RATING))));
            dataList.add(recipe);

        }

        //Determine sorting factor
        switch(sortRecipeBy)
        {
            //Sort Recipe Descending order by Rating
            case SORT_RECIPE_RATING:
                Collections.sort(dataList, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe t1) {
                        return Float.toString(t1.getRecipeRating()).compareTo(Float.toString(recipe.getRecipeRating()));
                    }
                });
                break;
            //Sort Recipe Ascending order by Title
            case SORT_RECIPE_TITLE:

                Collections.sort(dataList, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe t1) {
                        return recipe.getRecipeTitle().compareToIgnoreCase(t1.getRecipeTitle());
                    }
                });
                break;
            //Sort Recipe Ascending order by ID
            case SORT_RECIPE_ID:

                Collections.sort(dataList, new Comparator<Recipe>() {
                    @Override
                    public int compare(Recipe recipe, Recipe t1) {
                        return Integer.toString(recipe.getRecipeId()).compareTo(Integer.toString(t1.getRecipeId()));
                    }
                });
                break;
        }
        // set adapter data and list adapter
        adapter = new RecipeAdapter(MainActivity.this, dataList);
        recipeListViewMain.setAdapter(adapter);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (resultCode == RESULT_OK) {
            if (requestCode == VIEW_RECIPE_REQUEST || requestCode == NEW_RECIPE_REQUEST) {
                addBtn.setText("Add New"); // reset button to original look
                displayDatabaseContent(SORT_RECIPE_ID); // update list at every return to the activity
            }
        }
    }


}
