package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;
import java.util.Optional;

@Repository
public interface KitchenToolRepository extends MongoRepository<KitchenTool, String> {
    @Query("{}")
    Collection<KitchenTool> findAllKitchenTools();
    @Query("{name : {$regex: ?0, $options: 'i'}}")
    Collection<KitchenTool> findAllEquipmentContainingString(String name);
    // insert query here to fetch all tool actions
    @Query("{_id: ?0}")
    Optional<KitchenTool> findById (String id);
    @Query("{name: ?0}")
    Optional<KitchenTool> findByName (String name);
}
