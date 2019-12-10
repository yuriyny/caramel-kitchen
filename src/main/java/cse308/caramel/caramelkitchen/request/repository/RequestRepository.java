package cse308.caramel.caramelkitchen.request.repository;

import cse308.caramel.caramelkitchen.request.persistence.Request;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Collection;

@Repository
public interface RequestRepository extends MongoRepository<Request, String> {
    @Query("{type : ?0}")
    Collection<Request> findAllOfType(String type);
    @Query("{username : ?0}")
    Collection<Request> findByUsername(String username);

}
