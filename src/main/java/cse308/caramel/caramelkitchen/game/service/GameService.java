package cse308.caramel.caramelkitchen.game.service;

import cse308.caramel.caramelkitchen.game.model.GameState;
import cse308.caramel.caramelkitchen.game.model.Rating;
import cse308.caramel.caramelkitchen.game.persistence.*;
import cse308.caramel.caramelkitchen.game.repository.GameRepository;
import cse308.caramel.caramelkitchen.game.repository.RecipeRepository;
import cse308.caramel.caramelkitchen.user.persistence.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
public class GameService {

    @Autowired
    RecipeEditorService recipeEditorService;
    @Autowired
    GameRepository gameRepository;
    @Autowired
    RecipeRepository recipeRepository;

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
        if(currentGamestate != null) {
            currentGamestate.setIngredients(recipeEditorService.findImageIngredient((List) currentGamestate.getIngredients()));
            currentGamestate.setKitchenTools(recipeEditorService.findImageTool((List) currentGamestate.getKitchenTools()));
            currentGamestate.setIntermediateIngredients(recipeEditorService.findImage((List) currentGamestate.getIntermediateIngredients()));
            fetchedGame.setGameState(currentGamestate);
        }
        return fetchedGame;
    }

    public void saveGame(Game game) {
        gameRepository.save(game);
    }

    public void updateUserRecipeRating(Rating rating) {
        Game game = getGame(rating.getGameId());
        game.setUserRating(rating.getScore());
        gameRepository.save(game);
    }

    public Double fetchRecipeRating(String recipeId) {
        Collection<Game> g = gameRepository.findAllGamesPlayedByRecipeId(recipeId).orElse(null);
        if (g != null){
            Double recipeRating = g.stream().map(game -> game.getUserRating()).filter(Objects::nonNull).mapToDouble(rating->rating).average().orElse(0.0);
            Recipe recipe = recipeRepository.findById(recipeId).get();
            recipe.setRating(recipeRating);
            recipeRepository.save(recipe);
            return recipeRating;
        }
        return 0.0;
    }

    public Double fetchSingleUserGameRating(String gameId) {
        Game g = getGame(gameId);
        if (g.getUserRating() != null) return g.getUserRating();
        else return 0.0;
    }
}
