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
public class ProcessedIngredient extends SubprocedureComponent {
    private Collection<HashMap<String, List<String>>> ingredientComponents;

    public ProcessedIngredient(){
        ingredientComponents = new ArrayList<>();
    }
}
