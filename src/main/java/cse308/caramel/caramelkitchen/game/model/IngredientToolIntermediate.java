package cse308.caramel.caramelkitchen.game.model;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class IngredientToolIntermediate {
    private List<String> ingredient;
    private List<String> tool;
    private List<IntermediateIngredient> intermediateIngredient;
}
