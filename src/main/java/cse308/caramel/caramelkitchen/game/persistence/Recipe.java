package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Document(collection = "recipe")
public class Recipe {
    @Id
    private String id;
    private String recipeName;
    private String creator;//creator/user id
    private String parentId;
    private double rating;
    @DBRef
    private Collection<Ingredient> ingredients;
    @DBRef
    private Collection<KitchenTool> kitchenTools;
    //completed recipe sets this field to false
    private Boolean isPublished = true;
    private List<Subprocedure> subprocedureList;

    // + maintain another list that contains subprocedures any playing user MUST complete to pass the recipe
    private List<Subprocedure> requiredSubproceduresList;
    public Recipe(){
        ingredients=new ArrayList<>();
        kitchenTools=new ArrayList<>();
        subprocedureList=new ArrayList<>();
    }

/*    should we create a list of <instruction class>
    which will have field like step_description,
    step_number, equipment/ingredients needed?*/
}
