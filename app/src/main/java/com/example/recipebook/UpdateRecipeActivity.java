package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.ContentValues;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

public class UpdateRecipeActivity extends AppCompatActivity {

    // declare global variables
    private static String ID;
    private static String TITLE;
    private static String CONTENT;
    private static String RATING;
    private static boolean isUpdating = false;
    DatabaseHandler databaseHandler;
    TextView recipeIDUpdate;
    EditText recipeTitleUpdate, recipeContentUpdate;
    Button backBtn, editBtn,deleteBtn;
    RatingBar ratingBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_update_recipe);
        // initialize global variables
        databaseHandler = new DatabaseHandler(this, null, null,
                Contract.DATABASE_VERSION);
        recipeIDUpdate = findViewById(R.id.recipeIDUpdate);
        recipeTitleUpdate = findViewById(R.id.recipeTitleUpdate);
        recipeContentUpdate = findViewById(R.id.recipeContentUpdate);
        backBtn = findViewById(R.id.backBtn);
        editBtn = findViewById(R.id.editBtn);
        deleteBtn = findViewById(R.id.deleteBtn);
        ratingBar = findViewById(R.id.ratingBar);


        // get recipe details from ListView in Main
        Intent intent = getIntent();
        ID = intent.getStringExtra("id");
        TITLE = intent.getStringExtra("title");
        CONTENT = intent.getStringExtra("content");
        RATING = intent.getStringExtra("rating");


        //UI Setting
        recipeTitleUpdate.setEnabled(false);
        recipeContentUpdate.setEnabled(false);
        ratingBar.setEnabled(false);

        //Set Values in UI
        recipeIDUpdate.setText(ID);
        recipeTitleUpdate.setText(TITLE);
        recipeContentUpdate.setText(CONTENT);
        if(RATING != null){
            ratingBar.setRating(Float.parseFloat(RATING));
        }

        deleteBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // delete current recipe
                databaseHandler.deleteRecipe(ID);

                // end activity and return result
                Intent resultIntent = new Intent();
                setResult(Activity.RESULT_OK, resultIntent);
                finish();

                Toast.makeText(getApplicationContext(), "Entry " + TITLE + " was deleted",
                        Toast.LENGTH_LONG).show();
            }
        });

        /*
        * Enables editing respective text fields
        * */
        editBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isUpdating) { // if user is done editing
                    // validate inputs before saving
                    if (recipeTitleUpdate.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Title field cannot be empty",
                                Toast.LENGTH_LONG).show();
                    } else if (recipeContentUpdate.getText().toString().isEmpty()) {
                        Toast.makeText(getApplicationContext(), "Description field cannot be empty",
                                Toast.LENGTH_LONG).show();
                    } else { // if all is valid
                        // set UI elements to not editable
                        isUpdating = false;
                        recipeTitleUpdate.setEnabled(false);
                        recipeContentUpdate.setEnabled(false);
                        ratingBar.setEnabled(false);
                        editBtn.setText("Edit");

                        // update global variable values
                        TITLE = recipeTitleUpdate.getText().toString();
                        CONTENT = recipeContentUpdate.getText().toString();
                        RATING = String.valueOf(ratingBar.getRating());

                        // update current recipe's details
                        ContentValues values = new ContentValues();
                        values.put(Contract.COLUMN_ID, ID);
                        values.put(Contract.COLUMN_TITLE, TITLE);
                        values.put(Contract.COLUMN_CONTENT, CONTENT);
                        values.put(Contract.COLUMN_RATING, RATING);
                        getContentResolver().update(Contract.CONTENT_URI, values,
                                "id = " + ID, null);

                        Toast.makeText(getApplicationContext(), "Changes saved successfully",
                                Toast.LENGTH_LONG).show();

                        // end activity and return results
                        Intent resultIntent = new Intent();
                        setResult(Activity.RESULT_OK, resultIntent);
                        finish();
                    }

                } else { // set UI items to editable
                    isUpdating = true;
                    recipeTitleUpdate.setEnabled(true);
                    recipeContentUpdate.setEnabled(true);
                    ratingBar.setEnabled(true);
                    editBtn.setText("Save");
                }
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // handle UI changes based on current state
                if (isUpdating) { // id user is done editing
                    isUpdating = false;
                    backBtn.setText("Back");
                } else {
                    isUpdating = true;
                    backBtn.setText("Cancel");
                    Intent resultIntent = new Intent();
                    setResult(Activity.RESULT_OK, resultIntent);
                    finish();
                }
            }
        });

    }

    // disable built-in back button
    @Override
    public void onBackPressed() {
        Toast.makeText(this,"Please Use Back Button",Toast.LENGTH_SHORT).show();
    }
}
