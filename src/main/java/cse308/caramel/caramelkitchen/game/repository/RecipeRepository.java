package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    @Query("{}")
    Collection<Recipe> findAllRecipes();
    @Query("{recipeName : {$regex: ?0, $options: 'i'}}")
    Collection<Recipe> findAllRecipesContainingString(String name);
}
