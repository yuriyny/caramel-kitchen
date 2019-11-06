package cse308.caramel.caramelkitchen.user.persistence;

import cse308.caramel.caramelkitchen.game.persistence.Game;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
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
    private Collection<Game> gamesPlayed;
    private Collection<Game> gamesInProgress;
    @DBRef
    private Collection<Recipe> recipesCreated;
    @DBRef
    private Set<Role> roles;
    private Collection<Feedback> feedback;
    @DBRef
    private Collection<Request> requests;

}