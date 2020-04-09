package com.example.recipebook;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;

import java.util.List;

//This class is to arrange data in listview_item layout

public class RecipeAdapter extends ArrayAdapter<Recipe>
{
    // declare global variables
    List<Recipe> recipeList;
    TextView recipeId;
    TextView recipeTitle;
    TextView recipeRating;

    // define class constructor
    public RecipeAdapter(Context context, List<Recipe> recipeList) {
        super(context, R.layout.listview_item, recipeList);
        this.recipeList = recipeList;
    }

    @NonNull
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        LayoutInflater inflater = LayoutInflater.from(getContext());
        View customView = inflater.inflate(R.layout.listview_item, parent, false);

        // get recipe details
        Recipe item = getItem(position);
        recipeId = customView.findViewById(R.id.recipeIdLV);
        recipeTitle = customView.findViewById(R.id.recipeTitleLV);
        recipeRating = customView.findViewById(R.id.recipeRatingLV);

        // update UI element values
        recipeId.setText(Integer.toString(item.getRecipeId()));
        recipeTitle.setText(item.getRecipeTitle());
        recipeRating.setText(""+item.getRecipeRating());

        return customView;
    }
}
