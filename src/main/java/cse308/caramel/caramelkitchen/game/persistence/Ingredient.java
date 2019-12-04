package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Getter
@Setter
@Document(collection = "ingredient")
public class Ingredient extends SubprocedureComponent {
    @Id
    private String id;
    private List<String> unitOfMeasure;
    private int quantity;
    private String type;//spice, liquid, meat, vegetable, fruit, etc.
    public Ingredient(){
        unitOfMeasure=new ArrayList<>();
    }
}
