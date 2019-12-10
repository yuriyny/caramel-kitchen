package cse308.caramel.caramelkitchen.game.model;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class GameState {
    String gameId;
    String recipeId;
    Collection<Ingredient> ingredients;
    Collection<KitchenTool> kitchenTools;
    Collection<IntermediateIngredient> intermediateIngredients; //This is to make it easier to retrieve if comments are used
    Collection<Boolean> scores;
    int mistakes;

    public GameState() {
        ingredients = new ArrayList<>();
        kitchenTools = new ArrayList<>();
        intermediateIngredients = new ArrayList<>();
        scores= new ArrayList<>();
    }
}
