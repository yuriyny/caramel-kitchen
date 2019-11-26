package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface IngredientRepository extends MongoRepository<Ingredient, String> {
    @Query("{}")
    Collection<KitchenTool> findAllIngredients();
    @Query("{name : {$regex: ?0, $options: 'i'}}")
    Collection<KitchenTool> findAllIngredientsContainingString(String name);
}
