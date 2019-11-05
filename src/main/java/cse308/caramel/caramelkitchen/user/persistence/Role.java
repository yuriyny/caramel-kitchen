package cse308.caramel.caramelkitchen.user.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@Getter
@Setter
@Document(collection = "role")
public class Role {
    @Id
    private Long id;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
//    private String name;
    private String role;
//    private Set<User> users;
}