package cse308.caramel.caramelkitchen.user.storage;

import cse308.caramel.caramelkitchen.user.persistence.Role;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends MongoRepository<Role,Long> {
    Role findByRole(String role);
}
