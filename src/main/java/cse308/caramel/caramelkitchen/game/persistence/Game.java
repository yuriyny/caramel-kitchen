package cse308.caramel.caramelkitchen.game.persistence;

import cse308.caramel.caramelkitchen.game.model.GameState;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.Collection;

@Getter
@Setter
@Document(collection = "game")
public class Game {// created game repository just in case we want to do a leaderboard
    @Id
    private String id;
//    @DBRef
//    private String username; // not needed yet unless we want to do a leaderboard or something
    private GameState gameState;
    @DBRef
    private Recipe recipe;
    private Double score;

}
