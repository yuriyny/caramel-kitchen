package cse308.caramel.caramelkitchen.game.model;

import cse308.caramel.caramelkitchen.game.persistence.Recipe;

import java.util.Collection;

public class GameState {
    String user;
    String gameName;
    Recipe recipe;
    int lastCompletedStep;
    Collection<Double> scores;
}
