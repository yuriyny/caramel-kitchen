package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
}
