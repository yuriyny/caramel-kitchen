package cse308.caramel.caramelkitchen.game.repository;

import cse308.caramel.caramelkitchen.game.Equipment;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface EquipmentRepository extends MongoRepository<Equipment, String> {
}
