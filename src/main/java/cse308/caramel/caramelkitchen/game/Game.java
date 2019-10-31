package cse308.caramel.caramelkitchen.game;

import lombok.Getter;
import lombok.Setter;
import org.springframework.data.mongodb.core.mapping.DBRef;

@Getter
@Setter
public class Game {
    private String gameName;
    private int score;
    @DBRef
    private Recipe recipe;
}
