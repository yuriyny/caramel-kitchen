package cse308.caramel.caramelkitchen.game.model;

import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
public class GameState {
    String gameId;
    String recipeId;
    Collection<IntermediateIngredient> intermediateIngredients; //This is to make it easier to retrieve if comments are used
    Collection<Double> scores;

    public GameState(){
        intermediateIngredients = new ArrayList<>();
        scores = new ArrayList<>();
    }
}
