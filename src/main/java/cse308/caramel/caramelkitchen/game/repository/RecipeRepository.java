package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.List;

@Repository
public interface RecipeRepository extends MongoRepository<Recipe, String> {
    @Query("{isPublished: { $eq:true }}")
    Collection<Recipe> findAllPublishedRecipes();
    @Query("{recipeName : {$regex: ?0, $options: 'i'},isPublished: { $eq:true }}")
    Collection<Recipe> findAllRecipesContainingString(String name);
    @Query("{isPublished: { $eq:true }}")
    List<Recipe> findTopRatedReceipes(Pageable pageable);
}
