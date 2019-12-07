package cse308.caramel.caramelkitchen.game.persistence;


import lombok.Getter;
import lombok.Setter;

import java.util.Collection;

@Setter
@Getter
public class Subprocedure {
    private String procedureName;//This is the action name. This is also the game to call for
    private String instructions;
    private Collection<Ingredient> targetIngredients;
//    private String miniGame;
    //private int instructionNumber;//step number
}
