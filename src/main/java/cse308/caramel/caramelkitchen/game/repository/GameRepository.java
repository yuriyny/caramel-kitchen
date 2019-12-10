package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.persistence.Game;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface GameRepository extends MongoRepository<Game, String> {
    @Query("{_id: ?0}")
    Optional<Game> findById (String id);
    @Query("{\"recipe.$id\":ObjectId(?0)}")
    Optional<Collection<Game>> findAllGamesPlayedByRecipeId (String id);
}
