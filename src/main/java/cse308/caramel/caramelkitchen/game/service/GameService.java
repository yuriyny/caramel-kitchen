package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.model.GameState;
import cse308.caramel.caramelkitchen.game.persistence.Game;
import cse308.caramel.caramelkitchen.game.persistence.Recipe;
import cse308.caramel.caramelkitchen.game.repository.GameRepository;
import cse308.caramel.caramelkitchen.user.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {
    @Autowired
    GameRepository gameRepository;
    public Game getGame(String gameId){
        return gameRepository.findById(gameId).get();
    }
    public Game createGame(Recipe recipe){
        Game game=new Game();
        game.setRecipe(recipe);
        gameRepository.save(game);
        return game;
    }
    public void saveGameProgress(String gameId, String recipeId, List<String>stepsCompleted,List<Double> scores){
        GameState gameState=new GameState();
        gameState.setGameId(gameId);
        gameState.setRecipeId(recipeId);
        gameState.setStepsCompleted(stepsCompleted);
        gameState.setScores(scores);
        Game game=getGame(gameId);
        game.setGameState(gameState);
        gameRepository.save(game);
    }

    public boolean isGameInProgress(Recipe recipe, User user) {
        return user.getGamesInProgress().stream().anyMatch(game -> game.getRecipe().getId() == recipe.getId());
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }

}
