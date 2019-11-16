package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface RecipeRepository extends MongoRepository<Recipe, String> {
    @Query("{isInProgress: { $eq:false }}")
    Collection<Recipe> findAllPublishedRecipes();
    @Query("{recipeName : {$regex: ?0, $options: 'i'},isInProgress: { $eq:false }}")
    Collection<Recipe> findAllRecipesContainingString(String name);
}
