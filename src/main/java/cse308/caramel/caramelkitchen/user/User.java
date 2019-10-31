package cse308.caramel.caramelkitchen.user;

import cse308.caramel.caramelkitchen.game.Game;
import cse308.caramel.caramelkitchen.game.Recipe;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.IndexDirection;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Collection;
import java.util.Set;


@Getter
@Setter
@Document(collection = "user")
public class User {
    @Id
    private String username;
    @Indexed(unique = true, direction = IndexDirection.DESCENDING, dropDups = true)
    private String password;
    private boolean enabled;
    private int active;
    private Collection<Game> gamesPlayed;
    @DBRef
    private Collection<Recipe> recipesCreated;
    @DBRef
    private Set<Role> roles;
    private Collection<Feedback> feedbacks;
    @DBRef
    private Collection<Request> requests;

}