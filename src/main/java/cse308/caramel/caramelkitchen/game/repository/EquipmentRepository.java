package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.Equipment;
import cse308.caramel.caramelkitchen.game.Items;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface EquipmentRepository extends MongoRepository<Equipment, String> {
    @Query("{}")
    Collection<Items> findAllEquipment();
    @Query("{name : {$regex: ?0, $options: 'i'}}")
    Collection<Items> findAllEquipmentContainingString(String name);
}
