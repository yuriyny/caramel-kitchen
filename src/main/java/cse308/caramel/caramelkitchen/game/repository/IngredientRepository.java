package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface IngredientRepository extends MongoRepository<Ingredient, String> {
}
