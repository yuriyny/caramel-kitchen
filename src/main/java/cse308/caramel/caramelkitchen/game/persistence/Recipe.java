package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Getter
@Setter
@Document(collection = "recipe")
public class Recipe {
    @Id
    private String id;
    private String recipeName;
    private String creator;//creator/user id
    private double rating;
    @DBRef
    private Collection<Ingredient> ingredients;
    @DBRef
    private Collection<KitchenTool> equipment;
    //unfinished recipe = true
    //completed recipe sets this field to false
    private Boolean isInProgress = true;

/*    should we create a list of <instruction class>
    which will have field like step_description,
    step_number, equipment/ingredients needed?*/
}
