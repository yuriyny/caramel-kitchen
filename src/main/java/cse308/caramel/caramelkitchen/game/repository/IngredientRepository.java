package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.Equipment;
import cse308.caramel.caramelkitchen.game.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    @Query("{}")
    Collection<Ingredient> findAllIngredients();
    @Query("{ingredientName : {$regex: ?0, $options: 'i'}}")
    Collection<Equipment> findAllIngredientsContainingString(String name);
}
