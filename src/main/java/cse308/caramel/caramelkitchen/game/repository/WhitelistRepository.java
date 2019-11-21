package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.persistence.Ingredient;
import org.springframework.data.mongodb.repository.MongoRepository;
import cse308.caramel.caramelkitchen.game.persistence.Whitelist;
import org.springframework.stereotype.Repository;

@Repository
public interface WhitelistRepository extends MongoRepository<Whitelist, String> {

}
