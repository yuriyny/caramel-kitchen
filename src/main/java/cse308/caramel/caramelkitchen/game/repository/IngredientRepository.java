package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.Ingredient;
import cse308.caramel.caramelkitchen.game.Items;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    @Query("{}")
    Collection<Items> findAllIngredients();
    @Query("{name : {$regex: ?0}}")
    Collection<Items> findAllIngredientsContainingString(String name);

}
