package cse308.caramel.caramelkitchen.user.storage;

import cse308.caramel.caramelkitchen.user.Role;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface RoleRepository extends MongoRepository<Role,Long> {
    Role findByRole(String role);
}
