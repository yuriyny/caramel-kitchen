package cse308.caramel.caramelkitchen.user.storage;

import cse308.caramel.caramelkitchen.user.persistence.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends  MongoRepository<User, String> {

}