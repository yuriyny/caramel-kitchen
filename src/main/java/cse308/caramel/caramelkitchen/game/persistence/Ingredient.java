package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Document(collection = "ingredient")
public class Ingredient extends SubprocedureComponent {
    @Id
    private String id;
    private String unitOfMeasure;
    private int quantity;
    //embedded document for ingredients, no need for referencing
    private Collection<Subprocedure> subprocedures = new ArrayList<>();
    private Collection<String> tags;

}
