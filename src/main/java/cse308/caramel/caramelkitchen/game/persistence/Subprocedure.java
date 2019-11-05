package cse308.caramel.caramelkitchen.game.persistence;


import cse308.caramel.caramelkitchen.game.model.GameApplication;
import lombok.Getter;
import lombok.Setter;

@Setter
@Getter
public class Subprocedure {
    private String procedureName;//chopping
    private String instructions;
    private GameApplication game;
    //private int instructionNumber;//step number
}
