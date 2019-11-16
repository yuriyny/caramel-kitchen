package cse308.caramel.caramelkitchen.user.storage;

import cse308.caramel.caramelkitchen.game.persistence.KitchenTool;
import cse308.caramel.caramelkitchen.user.persistence.User;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

// This will be AUTO IMPLEMENTED by Spring into a Bean called userRepository
// CRUD refers Create, Read, Update, Delete
@Repository
public interface UserRepository extends  MongoRepository<User, String> {
    @Query("{ recipesCreated: { $exists: true, $not: {$size: 0}} }")
    Collection<User> findAllCreators();
    @Query("{username : {$regex: ?0, $options: 'i'}, recipesCreated: { $exists: true, $not: {$size: 0} }}")
    Collection<User> findAllCreatorsContainingString(String username);
//    @Query("{username : {$regex: ?0, $options: 'i'}})")
//    Collection<User> findAllUsersContainingString(String username);
}