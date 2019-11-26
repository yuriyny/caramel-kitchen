package cse308.caramel.caramelkitchen.game.model;

import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Getter
@Setter
public class GameState {
    String gameId;
    String recipeId;
    Collection<String> stepsCompleted;
    Collection<Double> scores;
}
