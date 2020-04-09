package com.example.recipebook;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.util.Random;

public class AddNewRecipeActiviy extends AppCompatActivity {

    private EditText recipeTitle;
    private EditText recipeContent;
    private Button proceedBtn;
    private DatabaseHandler databaseHandler;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_new_recipe);


        recipeContent = findViewById(R.id.recipeContentNew);
        recipeTitle = findViewById(R.id.recipeTitleNew);
        proceedBtn = findViewById(R.id.proceedNewRecipeBtn);

        databaseHandler = new DatabaseHandler(this, null, null,
                Contract.DATABASE_VERSION);
        proceedBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Confirm text fields not empty
                if (recipeTitle.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Title field cannot be empty",
                            Toast.LENGTH_LONG).show();
                } else if (recipeContent.getText().toString().isEmpty()) {
                    Toast.makeText(getApplicationContext(), "Description field cannot be empty",
                            Toast.LENGTH_LONG).show();
                } else {

                    Random random = new Random();
                    Recipe recipe = new Recipe();
//
                    recipe.setRecipeId(100 + random.nextInt(900));
                    recipe.setRecipeTitle( recipeTitle.getText().toString());
                    recipe.setRecipeContent(recipeContent.getText().toString());
                    recipe.setRecipeRating(0);

                    databaseHandler.addNewRecipe(recipe);

                    Toast.makeText(getApplicationContext(), "New recipe saved successfully",
                            Toast.LENGTH_LONG).show();
                    // end activity and return result
                    Intent returnIntent = new Intent();
                    setResult(Activity.RESULT_OK, returnIntent);
                    finish();
                }
            }
        });

    }
}
