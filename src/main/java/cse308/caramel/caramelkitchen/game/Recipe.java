package cse308.caramel.caramelkitchen.game;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;

@Document(collection = "recipe")
public class Recipe {
    @Id
    private String id;
    private String name;
    private String creator;//creator/user id
    private double rating;
    @DBRef
    private Collection<Ingredient> ingredients;
    @DBRef
    private Collection<Equipment> equipments;
}
