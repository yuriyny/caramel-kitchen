package cse308.caramel.caramelkitchen.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.TypeAlias;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Document(collection = "items")
@TypeAlias("ingredient")
public class Ingredient extends Items{

    private int quantity;
    //embedded document for ingredients, no need for referencing
    private Collection<Procedure> procedures = new ArrayList<>();

}
