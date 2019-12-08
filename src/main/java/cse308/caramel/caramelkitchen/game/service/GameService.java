package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.model.GameState;
import cse308.caramel.caramelkitchen.game.model.IntermediateIngredient;
import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.GameRepository;
import cse308.caramel.caramelkitchen.user.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GameService {

    @Autowired
    RecipeEditorService recipeEditorService;
    @Autowired
    GameRepository gameRepository;
    public Game getGame(String gameId){
        return gameRepository.findById(gameId).orElse(null);
    }
    public Game createGame(Recipe recipe){
        Game game=new Game();
        game.setRecipe(recipe);
        gameRepository.save(game);
        Game g = gameRepository.findById(game.getId()).get();
        return game;
    }
    public void saveGameProgress(GameState gamestate){
        Game game = getGame(gamestate.getGameId());
        game.setGameState(gamestate);
        gameRepository.save(game);
    }

    public boolean isGameInProgress(Recipe recipe, User user) {
        return user.getGamesInProgress().stream().anyMatch(game -> game.getRecipe().getId().equals(recipe.getId()));
    }

    public Game fetchGameInprogress(Recipe recipe, User user) {
        Game fetchedGame = user.getGamesInProgress().stream().filter(game-> game.getRecipe().getId().equals(recipe.getId())).findAny().orElse(null);
        GameState currentGamestate = fetchedGame.getGameState();
        currentGamestate.setIngredients(recipeEditorService.findImageIngredient((List) currentGamestate.getIngredients()));
        currentGamestate.setKitchenTools(recipeEditorService.findImageTool((List) currentGamestate.getKitchenTools()));
        currentGamestate.setIntermediateIngredients(recipeEditorService.findImage((List) currentGamestate.getIntermediateIngredients()));
        fetchedGame.setGameState(currentGamestate);
        return fetchedGame;
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }

}
