package cse308.caramel.caramelkitchen.game.persistence;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

import java.util.Collection;

@Getter
@Setter
public class Game {
    private int id;
    private String username;
    private Collection<Double> score;
    @DBRef
    private Recipe recipe;
}
