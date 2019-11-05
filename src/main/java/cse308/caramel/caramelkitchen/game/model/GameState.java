package cse308.caramel.caramelkitchen.game.model;

import java.util.Collection;

public class GameState {
    String id;
    String user;
    String recipeId;
    int lastCompletedStep;
    Collection<Double> scores;
}
