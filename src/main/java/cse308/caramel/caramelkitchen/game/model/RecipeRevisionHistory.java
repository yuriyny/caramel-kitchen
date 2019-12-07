package cse308.caramel.caramelkitchen.game.model;

import java.util.Collection;

public class RecipeRevisionHistory {
    String id; // id corresponds to recipe IDs in database
    RecipeRevisionHistory root;
    Collection<RecipeRevisionHistory> children;

    public void setRoot(RecipeRevisionHistory recipe){}
    public void addChild(RecipeRevisionHistory recipe){}

}
