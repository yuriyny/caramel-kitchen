package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;

import java.util.Collection;

public interface EquipmentRepository extends MongoRepository<Equipment, String> {
    @Query("{}")
    Collection<Equipment> findAllEquipment();
    @Query("{equipmentName : {$regex: ?0}}")
    Collection<Equipment> findAllEquipmentContainingString(String name);


}
