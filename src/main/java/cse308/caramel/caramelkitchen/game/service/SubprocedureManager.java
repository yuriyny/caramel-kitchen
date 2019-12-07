package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.model.GameApplication;
import cse308.caramel.caramelkitchen.game.model.GameState;
import cse308.caramel.caramelkitchen.game.persistence.Game;
import cse308.caramel.caramelkitchen.game.repository.GameRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Service;

@Service
public class SubprocedureManager {

    @Autowired
    GameRepository gameRepository;

    public GameApplication loadGame(String id){

        GameApplication gameApplication = new GameApplication();
        gameApplication.setId(id);
        gameApplication.setPresentationFilePath(new ClassPathResource("css/" + id + ".css").getPath());
        gameApplication.setJsFilePath(new ClassPathResource("js/" + id + ".js").getPath());
        return gameApplication;
    }

//    public void updateRecipeProgress(GameState gameState){
//
//    }
//
    public void restartGameProgress(Game game){
        game.setGameState(null);
        gameRepository.save(game);
    }
}
