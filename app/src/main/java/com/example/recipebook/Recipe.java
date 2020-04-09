package com.example.recipebook;

/*
* This class encapsulates the information of recipe
* */
public class Recipe
{
    // declare global variables
    private int recipeId;
    private String recipeTitle;
    private String recipeContent;
    private float recipeRating;
    Recipe(){

    }

    public Recipe(int recipeId, String recipeTitle, String recipeContent, float recipeRating) {
        this.recipeId = recipeId;
        this.recipeTitle = recipeTitle;
        this.recipeContent = recipeContent;
        this.recipeRating = recipeRating;
    }

    /*
    * Setter  and getters for Recipe Details
    * */

    public int getRecipeId() {
        return recipeId;
    }

    public void setRecipeId(int recipeId) {
        this.recipeId = recipeId;
    }

    public String getRecipeTitle() {
        return recipeTitle;
    }

    public void setRecipeTitle(String recipeTitle) {
        this.recipeTitle = recipeTitle;
    }

    public String getRecipeContent() {
        return recipeContent;
    }

    public void setRecipeContent(String recipeContent) {
        this.recipeContent = recipeContent;
    }

    public float getRecipeRating() {
        return recipeRating;
    }

    public void setRecipeRating(float recipeRating) {
        this.recipeRating = recipeRating;
    }
}
