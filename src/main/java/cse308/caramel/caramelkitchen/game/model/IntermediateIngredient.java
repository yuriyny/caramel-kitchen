package cse308.caramel.caramelkitchen.game.model;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.SubprocedureComponent;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;

@Getter
@Setter
public class IntermediateIngredient extends SubprocedureComponent {
    private Collection<HashMap<String, List<String>>> ingredientComponents;
    private Collection<Ingredient> ingredients;
    private Collection<IntermediateIngredient> intermediateIngredients;
    private String tag; // actions completed on this current
    private String instructionStep;

    public IntermediateIngredient(){
        ingredients=new ArrayList<>();
        intermediateIngredients=new ArrayList<>();
    }
}