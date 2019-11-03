package cse308.caramel.caramelkitchen.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Document(collection = "ingredient")
public class Ingredient extends Items{
//    @Id
//    private String id;
//    private String ingredientName;
//    private String unitOfMeasure;
    private int quantity;
    //embedded document for ingredients, no need for referencing
    private Collection<Procedure> procedures = new ArrayList<>();

}
