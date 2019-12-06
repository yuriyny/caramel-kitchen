package cse308.caramel.caramelkitchen.game.persistence;


import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Subprocedure {
    private String procedureName;//This is the action name. This is also the game to call for
    private String instructions;
    // TODO: Add collection of targetIngredients and changes to controllers/services
//    private String miniGame;
    //private int instructionNumber;//step number
}
